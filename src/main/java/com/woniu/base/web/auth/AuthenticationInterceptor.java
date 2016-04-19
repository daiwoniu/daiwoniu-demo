package com.woniu.base.web.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(AuthenticationInterceptor.class);
	public static final String DEFAULT_LOGIN_PATH = "/sessions/new";

	private String pathPrefix = "/";
	private Set<String> ignorePaths = new HashSet<String>();
	private String loginPath = DEFAULT_LOGIN_PATH;
	private IUserContextLoader userContextLoader;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String path = request.getRequestURI();
		if (isIgnorePath(path) || isLoginPath(path)
				|| !path.startsWith(pathPrefix)) {
			return true;
		}
		HttpSession session = request.getSession();
		Object uid = session.getAttribute(UserContext.USER_ID_SESSION_ATTRIBUTE);
		if (uid != null) {
			UserContext userContext = userContextLoader.load(uid.toString());
			if (userContext == null) {
				redirectToLogin(response);
				return false;
			}

			request.setAttribute(UserContext.CONTEXT_ATTRIBUTE, userContext);
			request.setAttribute(UserContext.USER_ATTRIBUTE, userContext.getUser());
			return true;
		}

		redirectToLogin(response);
		return false;

	}

	private void redirectToLogin(HttpServletResponse resp) throws IOException {
		resp.sendRedirect(loginPath);
	}

    /**
     * pattern:
     * ^/session/new$
     * ^/api
     *
     * @param path
     * @return
     */
    private boolean isIgnorePath(String path) {
        for(String ignorePath : ignorePaths)
        {
            Pattern pattern = Pattern.compile(ignorePath);
            Matcher m = pattern.matcher(path);
            if(m.find()) return true;
        }
        return false;
    }

	private boolean isLoginPath(String path) {
		return loginPath.equals(path);
	}

	public void setIgnorePaths(Set<String> ignorePaths) {
		this.ignorePaths = ignorePaths;
	}

	public void setLoginPath(String loginPath) {
		this.loginPath = loginPath;
	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	public void setUserContextLoader(IUserContextLoader userContextLoader) {
		this.userContextLoader = userContextLoader;
	}
}
