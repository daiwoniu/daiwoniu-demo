package com.woniu.base.web.auth;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liguoxiang on 2016-3-1.
 */
public class UserContext {
    public static final String USER_ID_SESSION_ATTRIBUTE = "uid";
    public static final String CONTEXT_ATTRIBUTE = "userContext";
    public static final String USER_ATTRIBUTE = UserContext.class.getName() + ".user";

    private String id;
    private Object user;
    private Set<String> permissions = new HashSet();

    public Long getLongId() {
        if (id == null) {
            return null;
        }

        return Long.valueOf(id);
    }

    public Integer getIntId() {
        if (id == null) {
            return null;
        }

        return Integer.valueOf(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public <T> T getUser() {
        return (T) user;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public boolean hasPermission(String permission) {
        return this.permissions.contains(permission);
    }
}
