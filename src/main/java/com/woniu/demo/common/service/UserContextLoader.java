package com.woniu.demo.common.service;

import com.woniu.base.db.DB;
import com.woniu.base.web.auth.IUserContextLoader;
import com.woniu.base.web.auth.UserContext;
import com.woniu.demo.admin.user.entity.User;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserContextLoader implements IUserContextLoader {
    @Inject
    private DB db;

    public void setDb(DB db) {
        this.db = db;
    }

    @Override
    public UserContext load(String id) {
        User user = db.from(User.class).where("id", id).first(User.class, false);
        if(user == null){
            return null;
        }
        UserContext userContext = new UserContext();
        userContext.setId(user.getId().toString());
        userContext.setUser(user);
        return userContext;
    }
}
