package com.woniu.base.web.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liguoxiang on 2016-3-1.
 */
public abstract class BaseFilter implements Filter {
    protected FilterConfig filterConfig;

    public BaseFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        this.doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }

    protected abstract void doFilter(HttpServletRequest var1, HttpServletResponse var2, FilterChain var3) throws IOException, ServletException;

    public void destroy() {
    }
}
