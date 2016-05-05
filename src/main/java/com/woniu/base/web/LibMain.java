package com.woniu.base.web;

import com.woniu.base.util.PathUtils;
import com.woniu.base.util.Utils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liguoxiang on 2016-5-5.
 */
public class LibMain {
    public static void main(String[] args) {
        try{
            String workRootPath = PathUtils.workPath + "lib/";
            List<File> list = Utils.getSubFiles(new File(workRootPath));
            Set<String> libSet = new HashSet<>();
            for (File file : list){
                if(file != null && file.exists() && file.isFile()){
                    String name = file.getName();
                    if(name.endsWith(".jar")){
                        libSet.add("lib\\" + name);
                    }
                }
            }
            String[] a = new String[libSet.size()];
            String libStr = Utils.joinExcludeBlank(false, ";", libSet.toArray(a));
            if(!Utils.isBlankEmpty(libSet)){
                libStr = ";" + libStr;
            }
            System.out.println(libStr);
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
