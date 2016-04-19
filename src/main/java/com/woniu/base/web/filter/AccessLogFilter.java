package com.woniu.base.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liguoxiang on 2016-3-1.
 */
public class AccessLogFilter extends BaseFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        AccessLog accessLog = new AccessLog(request);
        Exception exception = null;
        try {
            chain.doFilter(request, response);
        } catch (IOException e) {
            exception = e;
            throw e;
        } catch (ServletException e) {
            exception = e;
            throw e;
        } catch (RuntimeException e) {
            exception = e;
            throw e;
        } finally {
            accessLog.end(response, exception);
        }
    }
}
