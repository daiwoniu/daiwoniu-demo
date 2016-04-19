package com.woniu.base.web.displaytag;

import org.displaytag.util.DefaultHref;
import org.displaytag.util.DefaultRequestHelper;
import org.displaytag.util.Href;
import org.displaytag.util.RequestHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

public class RequestHelperFactory implements
		org.displaytag.util.RequestHelperFactory {

	@Override
	public RequestHelper getRequestHelperInstance(PageContext pageContext) {
		return new CustomRequestHelper(
				(HttpServletRequest) pageContext.getRequest(),
				(HttpServletResponse) pageContext.getResponse());
	}

	public static class CustomRequestHelper extends DefaultRequestHelper {
		private HttpServletRequest request;
		@SuppressWarnings("unused")
		private HttpServletResponse response;

		public CustomRequestHelper(HttpServletRequest servletRequest,
				HttpServletResponse servletResponse) {
			super(servletRequest, servletResponse);
			this.request = servletRequest;
			this.response = servletResponse;
		}

		public Href getHref() {
			String requestURI = (String) request
					.getAttribute("javax.servlet.forward.request_uri");
			if (requestURI == null) {
				requestURI = request.getRequestURI();
			}
			Href href = new DefaultHref(requestURI);
			href.setParameterMap(getParameterMap());
			return href;
		}
	}
}
