package com.woniu.base.web.taglib;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class ScriptTag extends AssetAwareTag {
    @Override
    protected void createTag(JspWriter out, String finalPath) throws IOException {
        StringBuilder ss = new StringBuilder("<script type=\"text/javascript\" charset=\"UTF-8\" src=\"");
        ss.append(finalPath).append("\"");
        fillDynamicAttributes(ss);
        ss.append("></script>");
        out.write(ss.toString());
    }
}
