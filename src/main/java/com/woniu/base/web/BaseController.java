package com.woniu.base.web;

import com.woniu.base.util.CaptchaUtils;
import com.woniu.base.web.auth.UserContext;
import com.woniu.base.web.taglib.Breadcrumb;
import org.patchca.background.BackgroundFactory;
import org.patchca.color.ColorFactory;
import org.patchca.color.RandomColorFactory;
import org.patchca.filter.ConfigurableFilterFactory;
import org.patchca.filter.library.AbstractImageOp;
import org.patchca.filter.library.WobbleImageOp;
import org.patchca.filter.predefined.*;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.text.renderer.BestFitTextRenderer;
import org.patchca.text.renderer.TextRenderer;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseController {
	private Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	protected HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra.getRequest();
	}
	
	protected HttpServletResponse getResponse() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra.getResponse();
	}

	protected String getRealIP() {
		return WebUtil.getRealIP(getRequest());
	}

	protected String getRealIP(HttpServletRequest request) {
		return WebUtil.getRealIP(request);
	}

	protected Cookie getCookie(HttpServletRequest request, String name) {
		return WebUtil.getCookie(request, name);
	}

	protected String getCookieValue(HttpServletRequest request, String name) {
		return WebUtil.getCookieValue(request, name);
	}

	protected void addPermanencyCookie(HttpServletResponse response,
			String name, String value) {
		addPermanencyCookie(response, name, value, "/");
	}

	protected void addPermanencyCookie(HttpServletResponse response,
			String name, String value, String path) {
		int expiry = 10 * 365 * 24 * 3600;
		addCookie(response, name, value, path, expiry, true);
	}

	protected void addCookie(HttpServletResponse response, String name,
							 String value, String path, int expiry) {
		addCookie(response, name, value, path, expiry, true);
	}

	protected void addCookie(HttpServletResponse response, String name,
			String value, String path, int expiry, boolean httpOnly) {
		WebUtil.addCookie(response, name, value, path, expiry, httpOnly);
	}

	protected String getMethod(HttpServletRequest request) {
		return request.getMethod();
	}

	@SuppressWarnings("unchecked")
	protected <T> T getCurrentUser(HttpServletRequest request) {
		return (T) request.getAttribute(UserContext.USER_ATTRIBUTE);
	}

	protected <T> T getCurrentUser() {
		return (T) getRequest().getAttribute(UserContext.USER_ATTRIBUTE);
	}
	
	protected UserContext getUserContext() {
		return (UserContext) getRequest().getAttribute(UserContext.CONTEXT_ATTRIBUTE);
	}

	protected QueryForm getQueryForm(HttpServletRequest request) {
		QueryForm queryForm = QueryForm.build(request);
        request.setAttribute("qf", queryForm);
		return queryForm;
	}

	protected boolean isGet(HttpServletRequest request) {
		return "GET".equalsIgnoreCase(getMethod(request));
	}

	protected boolean isPost(HttpServletRequest request) {
		return "POST".equalsIgnoreCase(getMethod(request));
	}

	protected boolean isPut(HttpServletRequest request) {
		return "PUT".equalsIgnoreCase(getMethod(request));
	}

	protected boolean isDelete(HttpServletRequest request) {
		return "DELETE".equalsIgnoreCase(getMethod(request));
	}

	public boolean isAjax(HttpServletRequest request) {
		return WebUtil.isAjax(request);
	}




	protected void setBreadcrumb(HttpServletRequest request, Breadcrumb breadcrumb) {
		request.setAttribute("breadcrumb", breadcrumb);
	}

	protected void setBreadcrumb(Breadcrumb breadcrumb) {
		getRequest().setAttribute("breadcrumb", breadcrumb);
	}

	protected void setBreadcrumb(HttpServletRequest request, String... nameLinks) {
		setBreadcrumb(request, new Breadcrumb(nameLinks));
	}

	protected void setBreadcrumb(String... nameLinks) {
		setBreadcrumb(getRequest(), new Breadcrumb(nameLinks));
	}

	@RequestMapping("/common/codeimg")
	public void codeimg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			session = request.getSession();
		}
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("cache", "no-cache");
		long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
		String token = CaptchaUtils.getSimpleCaptchaAndWriteImage(CaptchaUtils.PNG_Captcha, response.getOutputStream());
		session.setAttribute(CAPTCHA_TOKEN_SESSION_ATTRIBUTE, token);
		logger.debug("生成验证码图片成功，当前的SessionID=" + session.getId() + "，验证码=" + token);
	}
	protected final static String CAPTCHA_TOKEN_SESSION_ATTRIBUTE = "captchaToken";

}
