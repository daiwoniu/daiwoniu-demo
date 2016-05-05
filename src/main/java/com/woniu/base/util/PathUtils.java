package com.woniu.base.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by liguoxiang on 2015/6/29.
 */
public class PathUtils {
    public static String workPath;
    public static String webRootPath;
    public static String webInfoPath;
    public static String uploadPath;
    public static String downloadPath;
    public static String classesPath;

    static {
        try {
            webRootPath = (new File("webapp")).getAbsolutePath() + "/";
            webRootPath = URLDecoder.decode(webRootPath, "UTF-8");
        } catch (UnsupportedEncodingException var1) {
            throw new Error("核心工具类PathUtils出错,系统启动失败");
        }

        webRootPath = webRootPath.replace("\\", "/");

        workPath = ((new File(webRootPath)).getParent() + "/").replaceAll("\\\\", "/");

        webInfoPath = webRootPath + "WEB-INF/";
        uploadPath = webRootPath + "upload/";
        downloadPath = webRootPath + "download/";
        classesPath = PathUtils.class.getResource("/").getPath();
    }

    private PathUtils() {
    }

    public static void main(String[] args) {
        System.out.println(workPath);
        System.out.println(webRootPath);
        System.out.println(classesPath);
    }
}
