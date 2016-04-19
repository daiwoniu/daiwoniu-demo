package com.woniu.base.web.taglib;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class AssetTag extends AssetAwareTag {

    protected void createTag(JspWriter out, String finalPath) throws IOException {
        out.write(finalPath);
    }

}
