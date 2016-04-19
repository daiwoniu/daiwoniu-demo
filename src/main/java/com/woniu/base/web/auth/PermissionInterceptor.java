package com.woniu.base.web.auth;

import com.woniu.base.lang.Lists;
import com.woniu.base.web.auth.annotation.Permissions;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PermissionInterceptor extends HandlerInterceptorAdapter {
    private String noPermissionPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        UserContext userContext = (UserContext) request.getAttribute(UserContext.CONTEXT_ATTRIBUTE);
        if (userContext == null) {
            return true;
        }
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> clazz = handlerMethod.getBeanType();
        Permissions classPermissions = AnnotationUtils.findAnnotation(clazz, Permissions.class);
        Permissions methodPermissions = handlerMethod.getMethodAnnotation(Permissions.class);
        if (classPermissions == null && methodPermissions == null) {
            return true;
        }
        
        Set<String> permissionNames = new HashSet<>();
        mergePermissions(classPermissions, permissionNames);
        mergePermissions(methodPermissions, permissionNames);

//        if (!userContext.getPermissions().containsAll(permissionNames)) {
        if(!Lists.containAny(userContext.getPermissions(), permissionNames))        {
            redirectToNoPermissionPage(response);
        }

        return true;
    }

    private void mergePermissions(Permissions permissions, Set<String> permissionNames) {
        if (permissions != null) {
            permissionNames.addAll(Arrays.asList(permissions.value()));
        }
    }

    private void redirectToNoPermissionPage(HttpServletResponse resp) throws IOException {
        resp.sendRedirect(noPermissionPath);
    }

    public void setNoPermissionPath(String noPermissionPath) {
        this.noPermissionPath = noPermissionPath;
    }
}
