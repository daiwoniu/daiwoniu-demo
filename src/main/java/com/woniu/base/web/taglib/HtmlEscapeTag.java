package com.woniu.base.web.taglib;

import com.google.common.html.HtmlEscapers;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class HtmlEscapeTag extends TagSupport {
    private Object value;

    public void setValue(Object value) {
        this.value = value;
    }

    public int doStartTag() throws JspException {
        try {
            if (value == null) {
                return SKIP_BODY;
            }
            String s = value.toString();
            JspWriter out = pageContext.getOut();
            out.write(HtmlEscapers.htmlEscaper().escape(s));
        } catch (IOException e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }
}
