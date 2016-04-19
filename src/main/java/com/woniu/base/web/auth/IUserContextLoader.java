package com.woniu.base.web.auth;

public interface IUserContextLoader {
    
    UserContext load(String id);
    
}
