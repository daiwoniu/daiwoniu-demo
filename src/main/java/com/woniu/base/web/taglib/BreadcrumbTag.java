package com.woniu.base.web.taglib;

import com.google.common.html.HtmlEscapers;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class BreadcrumbTag extends TagSupport {
    private Breadcrumb breadcrumb;

    public void setBreadcrumb(Breadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public int doStartTag() throws JspException {
        if (breadcrumb == null) {
            return SKIP_BODY;
        }
        
        JspWriter out = pageContext.getOut();

        try {
            StringBuilder buf = new StringBuilder();
            buf.append("<ul class=\"breadcrumb\">");
            List<Breadcrumb.Item> items = breadcrumb.getItems();
            int len = items.size();
            for (int i = 0; i < len; i++) {
                Breadcrumb.Item item = items.get(i);
                boolean isLast = i == len - 1;
                
                if (isLast) {
                    buf.append("<li class=\"active\">").append(escape(item.getName())).append("</li>");
                } else {
                    buf.append("<li><a href=\"").append(item.getLink())
                            .append("\">").append(escape(item.getName()))
                            .append("</a> <span class=\"divider\">/</span></li>");
                }
            }
            buf.append("</ul>");
            out.append(buf);
        } catch (IOException e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }
    
    private String escape(String s) {
        return HtmlEscapers.htmlEscaper().escape(s);
    }
}
