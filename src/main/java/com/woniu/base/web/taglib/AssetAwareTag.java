package com.woniu.base.web.taglib;

import com.google.common.html.HtmlEscapers;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
abstract class AssetAwareTag extends TagSupport implements DynamicAttributes {
    private String path;
    private Map<String, Object> dynamicAttributes;

    protected Map<String, Object> getDynamicAttributes() {
        return this.dynamicAttributes;
    }

    protected boolean isValidDynamicAttribute(String localName, Object value) {
        return true;
    }

    public void setDynamicAttribute(String uri, String localName, Object value ) throws JspException {
        if (dynamicAttributes == null) {
            dynamicAttributes = new HashMap<String, Object>();
        }
        if (!isValidDynamicAttribute(localName, value)) {
            throw new IllegalArgumentException(
                    "Attribute " + localName + "=\"" + value + "\" is not allowed");
        }
        dynamicAttributes.put(localName, value);
    }

    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();

        try {
            String contextPath = pageContext.getServletContext().getContextPath();
            String fullPath = contextPath + path;
            String finalPath = AssetManifest.getDefaultAssetManifest().rewriteAsset(fullPath);
            createTag(out, finalPath);
        } catch (IOException e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    protected StringBuilder fillDynamicAttributes(StringBuilder ss) {
        if (dynamicAttributes == null) {
            return ss;
        }

        for (Map.Entry<String, Object> entry : dynamicAttributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                ss.append(" ").append(key);
            } else {
                String textValue = value.toString();
                textValue = HtmlEscapers.htmlEscaper().escape(textValue);
                ss.append(" ").append(key).append("=\"").append(textValue).append("\"");
            }
        }
        return ss;
    }

    protected abstract void createTag(JspWriter out, String finalPath) throws IOException;

    public void setPath(String path) {
        this.path = path;
    }

}
