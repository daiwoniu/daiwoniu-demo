package com.woniu.base.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class WebUtil {
    private WebUtil() {
    }

    public static String getRealIP(HttpServletRequest request) {
        String ip = request.getHeader("HTTP_X_REAL_IP");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static boolean isAjax(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestType);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie c = getCookie(request, name);
        if (c == null) {
            return null;
        }
        return c.getValue();
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

    public static void addPermanencyCookie(HttpServletResponse response,
                                       String name, String value) {
        addPermanencyCookie(response, name, value, "/");
    }

    public static void addPermanencyCookie(HttpServletResponse response,
                                       String name, String value, String path) {
        int expiry = 10 * 365 * 24 * 3600;
        addCookie(response, name, value, path, expiry, true);
    }

    public static void addCookie(HttpServletResponse response, String name,
                             String value, String path, int expiry, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expiry);
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }
}
