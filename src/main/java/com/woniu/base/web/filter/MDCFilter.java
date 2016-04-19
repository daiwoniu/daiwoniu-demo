package com.woniu.base.web.filter;

import com.woniu.base.web.auth.UserContext;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class MDCFilter extends BaseFilter {

    public static final String LOCAL_IP_KEY = "localIp";
    public static final String REMOTE_IP_KEY = "remoteIp";
    public static final String USER_ID_KEY = UserContext.USER_ID_SESSION_ATTRIBUTE;
    public static final String REQUEST_ID_KEY = "requestId";

    private AtomicLong requestId = new AtomicLong(1);

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            addMDCContext(request);
            chain.doFilter(request, response);
        } finally {
            clearMDC();
        }
    }

    //http://logback.qos.ch/manual/layouts.html#ClassicPatternLayout
    private void addMDCContext(HttpServletRequest request) {
        MDC.put(LOCAL_IP_KEY, request.getLocalAddr());
        MDC.put(REQUEST_ID_KEY, String.valueOf(requestId.getAndIncrement()));
        MDC.put(REMOTE_IP_KEY, request.getRemoteAddr());

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object userId = session.getAttribute(UserContext.USER_ID_SESSION_ATTRIBUTE);
            if (userId != null) {
                MDC.put(USER_ID_KEY, userId.toString());
            }
        }
    }

    private void clearMDC() {
        MDC.remove(LOCAL_IP_KEY);
        MDC.remove(REQUEST_ID_KEY);
        MDC.remove(REMOTE_IP_KEY);
        MDC.remove(USER_ID_KEY);
    }

}
