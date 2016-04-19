package com.woniu.base.web.taglib;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class ImgTag extends AssetAwareTag {
    public void setSrc(String src) {
        setPath(src);
    }

    @Override
    protected void createTag(JspWriter out, String finalPath) throws IOException {
        StringBuilder ss = new StringBuilder("<img src=\"").append(finalPath).append("\"");
        fillDynamicAttributes(ss);
        ss.append(">");
        out.write(ss.toString());
    }
}
