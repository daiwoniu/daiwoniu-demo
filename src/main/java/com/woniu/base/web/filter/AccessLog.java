package com.woniu.base.web.filter;

import com.woniu.base.util.JsonUtil;
import com.woniu.base.web.auth.UserContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liguoxiang on 2016-3-1.
 */
public class AccessLog {
    private static final String SESSION_COOKIE_NAME = "JSESSIONID";
    public static final String SESSION_USER_ID_KEY = UserContext.USER_ID_SESSION_ATTRIBUTE;
    public static final String ACCESS_LOG_KEY = "ACCESS_LOG";
    public static final Pattern QUERY_STRING_PASSWORD_PATTERN = Pattern.compile("((?:password|pwd)[^=&]*)=([^=&]*)", Pattern.CASE_INSENSITIVE);
    public static final Pattern PARAM_PASSWORD_PATTERN = Pattern.compile("(password|pwd)", Pattern.CASE_INSENSITIVE);

    public static final String STATUS_KEY = "accesslog.status";
    public static final String EXCEPTION_KEY = "accesslog.exception";

    private static final  String IGNORE_HEADER_NAMES = "User-Agent,X-Real-Ip,X-Forwarded-For,X-Forwarded-Proto," +
            "Connection,Accept-Encoding,Cookie,Host," +
            "Pragma,Cache-Control,Accept-Language,Accept-Encoding,Referer";
    private static final Set<String> excludeHeaderNames = new HashSet(
            Arrays.asList(IGNORE_HEADER_NAMES.toUpperCase().split(","))
    );

    private static Logger logger = LoggerFactory.getLogger(AccessLog.class);

    private static ThreadLocal<AccessLog> holder = new ThreadLocal<AccessLog>();

    private final HttpServletRequest request;
    private final DateTime startTime;
    private DateTime endTime;
    private String userId;

    private Throwable ex;

    private Map<String, String> appVars;
    private HttpServletResponse response;

    public AccessLog(HttpServletRequest request) {
        this.request = request;
        startTime = DateTime.now();
        request.setAttribute(ACCESS_LOG_KEY, this);

        buildUserId();
        holder.set(this);
    }

    private void buildUserId() {
        if (userId != null) {
            return;
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object value = session.getAttribute(SESSION_USER_ID_KEY);
            if (value != null) {
                userId = value.toString();
            }
        }
    }

    public void end(HttpServletResponse response, Exception ex) {
        holder.set(null);

        this.response = response;
        if (request.getAttribute(EXCEPTION_KEY) != null) {
            ex = (Exception) request.getAttribute(EXCEPTION_KEY);
        }
        this.ex = ex;

        buildUserId();//login时，userId只有在这里才有
        endTime = DateTime.now();

        String json = toJson();
        logger.info(json);
    }

    private HashMap<String, String> buildCookies() {
        Cookie[] httpCookies = request.getCookies();
        if (httpCookies == null) {
            return null;
        }

        HashMap<String, String> cookies = new HashMap();
        for (Cookie cookie : httpCookies) {
            cookies.put(cookie.getName(), cookie.getValue());
        }
        return cookies;
    }

    private String getSessionId() {
        Cookie cookie = getCookie(SESSION_COOKIE_NAME);
        if (cookie == null) {
            return null;
        }
        return cookie.getValue();
    }

    private Cookie getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    private String rewriteQueryString(String queryString) {
        if (queryString == null) {
            return null;
        }

        Matcher m = QUERY_STRING_PASSWORD_PATTERN.matcher(queryString);
        return m.replaceAll("$1=******");
    }

    private Map<String, String[]> rewriteParams(Map<String, String[]> params) {
        Map<String, String[]> result = new HashMap(params);
        for (Map.Entry<String, String[]> entry : result.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (value == null || value.length == 0) {
                continue;
            }

            Matcher m = PARAM_PASSWORD_PATTERN.matcher(key);
            if (m.find()) {
                for (int i = 0; i < value.length; i++) {
                    value[i] = "******";
                }
                entry.setValue(value);
            }
        }
        return params;
    }

    private String toJson() {
        double duration = (endTime.getMillis() - startTime.getMillis()) / 1000.0;//seconds

        Map<String, Object> data = new HashMap();
        data.put("startTime", startTime.toString());
        data.put("endTime", endTime.toString());
        data.put("duration", duration);
        data.put("ip", request.getRemoteAddr());
        data.put("serverIp", request.getLocalAddr());
        data.put("scheme", request.getScheme());
        data.put("method", request.getMethod());
        data.put("path", request.getRequestURI());
        data.put("queryString", rewriteQueryString(request.getQueryString()));
        data.put("cookie", request.getHeader("Cookie"));
        data.put("sessionId", getSessionId());
        data.put("cookies", buildCookies());

        data.put("host", request.getHeader("Host"));
        data.put("referer", request.getHeader("Referer"));
        data.put("userAgent", request.getHeader("User-Agent"));

        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            params = rewriteParams(params);
            data.put("params2", params);

            Map<String, String> params0 = new HashMap();
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                String[] value = entry.getValue();
                if (value == null || value.length == 0) {
                    continue;
                }
                params0.put(entry.getKey(), value[0]);
            }
            data.put("params", params0);
        }

        Object status = request.getAttribute(STATUS_KEY);
        if (status == null) {
            status = response.getStatus();
        }
        data.put("status", status);

        if (ex != null) {
            Map<String, Object> exceptionData = new HashMap();
            exceptionData.put("message", ex.getMessage());
            exceptionData.put("stackTrace", ex.getStackTrace());
            data.put("exception", exceptionData);
        }

        if (appVars != null) {
            data.put("appVars", appVars);
        }

        data.put("userId", userId);

        Map<String, String> validHeaders = extractValidHeaders(request);
        if (!validHeaders.isEmpty()) {
            data.put("headers", validHeaders);
        }

        return JsonUtil.dump(data);
    }

    private Map<String, String> extractValidHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap();
        Enumeration<String> names = request.getHeaderNames();
        while(names.hasMoreElements()) {
            String name = names.nextElement();
            if (excludeHeaderNames.contains(name.toUpperCase())) {
                continue;
            }
            String value = request.getHeader(name);
            headers.put(name, value);
        }
        return headers;
    }

    public static void setAppVar(String name, String value) {
        AccessLog accessLog = holder.get();
        if (accessLog == null) {
            return;
        }

        if (accessLog.appVars == null) {
            accessLog.appVars = new HashMap();
        }
        accessLog.appVars.put(name, value);
    }
}
