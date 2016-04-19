package com.woniu.base.util;

import com.woniu.base.lang.Strings;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by woniu on 2015/4/27 0027.
 */
public class Utils {
    private Utils(){}

    /**
     * 生成token  暂时直接用UUID生成的
     * @param login
     * @return
     */
    public static String generateToken(String login){
        String uid = UUID.randomUUID().toString();
        uid = uid.toUpperCase();
        uid = uid.replaceAll("-", "");
        return uid;
    }

    public static String generateCode(Integer length){
        int le = 6;
        if(length != null && length > 0)
            le = length;
        String uid = UUID.randomUUID().toString();
        uid = uid.toUpperCase();
        uid = uid.replaceAll("-", "");
        if(le > uid.length()){
            le = uid.length();
        }
        uid = uid.substring(0, le);
        return uid;
    }

    /**
     * 生成唯一编号， yyyyMMdd + 8位毫秒数
     * @param length 长度，默认32位, 暂时没使用
     * @return
     */
    public static synchronized String generateNumber(Integer length){
        int le = 32;
        if(length != null && length > 0)
            le = length;
        try{
            Thread.sleep(new Long(1));
        }catch (Exception e){
        }
        // 生成编号
        Date date = new Date();
        String number = date.getTime() + "";
        number = number.substring(5);
        String dateStr = formatDate(date, "yyyyMMdd");
        return dateStr + number;
    }
    /**
     * 获取String类型，如果是null返回""
     * @param obj
     * @return
     */
    public static String toString(Object obj){
        if(obj != null){
            return obj.toString();
        }
        return "";
    }

    /**
     * null或空字符或全是空格
     */
    public static boolean isBlankEmpty(Object obj){
        return Strings.isBlank(toString(obj).trim());
    }

    /**
     * 根据指定字符串的结束索引开始，截取原字符串
     * 如：sourceStr=abcde ; str=bc ;返回：de
     * @param sourceStr
     * @param str
     * @return
     */
    public static String indexOfSubString(String sourceStr, String str){
        if(!isBlankEmpty(sourceStr) && !isBlankEmpty(str) && sourceStr.indexOf(str) >= 0){
            int startIndex = sourceStr.indexOf(str) + str.length();
            return sourceStr.substring(startIndex);
        }
        return null;
    }

    /**
     * 以指定的字符，拼接数组，排除数组中null和""
     * @param noDuplicate 是否排除重复
     * @param separator 字符
     * @param args 数组
     * @return
     */
    public static String joinExcludeBlank(boolean noDuplicate, String separator, String... args){
        if(separator == null) {
            separator = "";
        }
        if(args == null) {
            return null;
        }
        int bufSize = args.length;
        if(bufSize <= 0) {
            return "";
        }
        if(noDuplicate){
            Set<String> set = new HashSet<String>(Arrays.asList(args));
            args = set.toArray(new String[0]);
        }
        bufSize *= (args[0] == null?16:args[0].toString().length()) + separator.length();
        StringBuffer buf = new StringBuffer(bufSize);
        for(int i=0; i < args.length; i++){
            if(!isBlankEmpty(args[i])) {
                if(!isBlankEmpty(buf.toString())) {
                    buf.append(separator);
                }
                buf.append(args[i]);
            }
        }
        return buf.toString();
    }
    /**
     * 全角转半角:
     * @param fullStr
     * @return
     */
    public static String full2Half(String fullStr) {
        if(isBlankEmpty(fullStr)){
            return fullStr;
        }
        char[] c = fullStr.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] >= 65281 && c[i] <= 65374) {
                c[i] = (char) (c[i] - 65248);
            } else if (c[i] == 12288) { // 空格
                c[i] = (char) 32;
            }
        }
        return new String(c);
    }

    /**
     * 半角转全角
     * @param halfStr
     * @return
     */
    public static String half2Full(String halfStr) {
        if(isBlankEmpty(halfStr)){
            return halfStr;
        }
        char[] c = halfStr.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
            } else if (c[i] < 127) {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }


    /**
     * 获取布尔类型
     * @param obj
     * @return
     */
    public static Boolean toBoolean(Object obj){
        if(isBlankEmpty(obj)){
            return false;
        }
        return Boolean.valueOf(obj.toString());
    }

    /**
     * 获取Integer类型，如果是null或转换失败，返回0
     * @param obj
     * @return
     */
    public static Integer toInteger(Object obj){
        Integer result = 0;
        if(obj != null){
            try{
                BigDecimal bigDecimal = toBigDecimalRigor(obj);
                if(!isZeroOrEmpty(bigDecimal)){
                    return bigDecimal.intValue();
                }
//                return Integer.valueOf(obj.toString());
            }catch (NumberFormatException e){}
        }
        return result;
    }
    /**
     * 获取Long类型，如果是null或转换失败，返回0
     * @param obj
     * @return
     */
    public static Long toLong(Object obj){
        Long result = new Long(0);
        if(obj != null){
            try{
                BigDecimal bigDecimal = toBigDecimalRigor(obj);
                if(!isZeroOrEmpty(bigDecimal)){
                    return bigDecimal.longValue();
                }
//                return Long.valueOf(obj.toString());
            }catch (NumberFormatException e){}
        }
        return result;
    }
    /**
     * 获取BigDecimal类型，如果存在%,直接去掉，传再,千位分隔符，直接去掉
     * 如果是null或转换失败，返回0
     * @param obj
     * @return
     */
    public static BigDecimal toBigDecimal(Object obj){
        BigDecimal result = BigDecimal.ZERO;
        if(obj != null){
            try{
                String str = obj.toString();
                if(str.contains("%")){
                    str = str.replaceAll("%", "");
//                    str = divide(str, 100, 2);
                }
                if(str.contains(",")){
                    str = str.replaceAll(",","");
                }
                return new BigDecimal(str);
            }catch (NumberFormatException e){}
        }
        return result;
    }
    /**
     * 获取BigDecimal类型，精确转换，如果格式错误，返回null
     * @param obj
     * @return
     */
    public static BigDecimal toBigDecimalRigor(Object obj){
        BigDecimal result = null;
        if(obj != null){
            try{
                String str = obj.toString();
                return new BigDecimal(str);
            }catch (NumberFormatException e){}
        }
        return result;
    }

    /**
     * 是否是数字并且值等于0或者空字符，
     * @param obj
     * @return
     */
    public static boolean isZeroOrEmpty(Object obj){
        if(isBlankEmpty(obj)){
            return true;
        }
        BigDecimal result;
        try{
            result = toBigDecimal(obj);
            if(result.compareTo(BigDecimal.ZERO) == 0){
                return true;
            }
        }catch (NumberFormatException e){ }
        return false;
    }

    /**
     * 比较两个数字的大小，必须是数字字符串，否则返回0
     * @param num1
     * @param num2
     * @return 0相等，1 num1>num2， -1 num1 < num2
     */
    public static int compareBigDecimal(Object num1, Object num2){
        BigDecimal bd1 = toBigDecimal(num1);
        BigDecimal bd2 = toBigDecimal(num2);
        return bd1.compareTo(bd2);
    }
    /**
     * 是否注册号(全是数字，或者 长度18位并且字母和数字都有)
     * @param str
     * @return
     */
    public static boolean isRegNo(String str)
    {
        boolean result = false;
        try{
            Matcher ma;
            Pattern pa = Pattern.compile("[0-9]*");
            ma = pa.matcher(str);
            result = ma.matches();
            if(!result){
                pa = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{18}$");
                ma = pa.matcher(str);
                result = ma.matches();
            }
        }catch (Exception e){}
        return result;
    }
    /**
     * 全是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str)
    {
        try{
            Matcher ma;
            Pattern pa = Pattern.compile("[0-9]*");
            ma = pa.matcher(str);
            return ma.matches();
        }catch (Exception e){}
        return false;
    }
    public static boolean isNumeric1(String str)
    {
        try{
            if(!isBlankEmpty(str)){
                str = str.replaceAll(",","");
            }
            Matcher ma;
            Pattern pa = Pattern.compile("[-]?[0-9]*[.]?[0-9]*");
            ma = pa.matcher(str);
            return ma.matches();
        }catch (Exception e){}
        return false;
    }

    /**
     * 是否手机号码
     * @param str
     * @return
     */
    public static boolean isMobile(String str)
    {
        if(isBlankEmpty(str)){
            return false;
        }
        try{
            if(!isBlankEmpty(str)){
                str = str.replaceAll(" ","");
            }
            Matcher ma;
            Pattern pa = Pattern.compile("^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1})|(14[0-9]{1}))+\\d{8})$");
            ma = pa.matcher(str);
            boolean tmp = ma.matches();
            if(tmp && str.length() == 11){
                return true;
            }
        }catch (Exception e){}
        return false;
    }

    /**
     * 是否邮箱
     * @param str
     * @return
     */
    public static boolean isEmail(String str)
    {
        if(isBlankEmpty(str)){
            return false;
        }
        try{
            Matcher ma;
            Pattern pa = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
            ma = pa.matcher(str);
            return ma.matches();
        }catch (Exception e){}
        return false;
    }

    /**
     * 小数点后为零的自动忽略
     * @param number
     * @return
     */
    public static String formatNumbersToString0(Object number){
        if(isBlankEmpty(number)){
            return "";
        }
        String retStr = number.toString();
        retStr = retStr.replaceAll(",", "");
        DecimalFormat format = (DecimalFormat) NumberFormat.getPercentInstance();
        format.applyPattern("##################.#############");
        try{
            retStr = format.format(Double.parseDouble(number.toString()));
        }catch (Exception e){}
        return retStr;
    }
    /**
     * 四舍五入，强制忽略小数， -0返回0
     * @param number
     * @return
     */
    public static String formatNumbers00(Object number){
        if(isBlankEmpty(number)){
            return "";
        }
        DecimalFormat format = (DecimalFormat) NumberFormat.getPercentInstance();
        format.applyPattern("###############0");
        format.setRoundingMode(RoundingMode.HALF_UP);
        String retStr = number.toString();
        try{
            retStr = format.format(Double.parseDouble(number.toString()));
            if("-0".equalsIgnoreCase(retStr)){
                retStr = "0";
            }
        }catch (Exception e){}
        return retStr;
    }
    /**
     * 四舍五入，如果小数点后都是零，最少保留两位小数0.00
     * @param number
     * @return
     */
    public static String formatNumbersToString(Object number){
        if(isBlankEmpty(number)){
            return "";
        }
        String retStr = number.toString();
        retStr = retStr.replaceAll(",", "");
        DecimalFormat format = (DecimalFormat) NumberFormat.getPercentInstance();
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.applyPattern("###############0.00######");
        try{
            retStr = format.format(Double.parseDouble(number.toString()));
        }catch (Exception e){}
        return retStr;
    }
    /**
     * 四舍五入，强制保留两位小数0.00
     * @param number
     * @return
     */
    public static String formatNumbersToString02(Object number){
        if(isBlankEmpty(number)){
            return "";
        }
        String retStr = number.toString();
        retStr = retStr.replaceAll(",", "");
        DecimalFormat format = (DecimalFormat) NumberFormat.getPercentInstance();
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.applyPattern("###############0.00");
        try{
            retStr = format.format(Double.parseDouble(number.toString()));
        }catch (Exception e){}
        return retStr;
    }
    /**
     * 四舍五入，强制保留两位小数，千位，好分隔0.00
     * @param number
     * @return
     */
    public static String formatNumbers(Object number){
        if(isBlankEmpty(number)){
            return "";
        }
        DecimalFormat format = (DecimalFormat) NumberFormat.getPercentInstance();
        format.applyPattern("####,###,###,###,##0.00");
        format.setRoundingMode(RoundingMode.HALF_UP);
        String retStr = number.toString();
        try{
            retStr = format.format(Double.parseDouble(number.toString()));
            if("-0.00".equalsIgnoreCase(retStr)){
                retStr = "0.00";
            }
        }catch (Exception e){}
        return retStr;
    }
    /**
     * 四舍五入，强制忽略小数，千位，好分隔
     * @param number
     * @return
     */
    public static String formatNumbers0(Object number){
        if(isBlankEmpty(number)){
            return "";
        }
        DecimalFormat format = (DecimalFormat) NumberFormat.getPercentInstance();
        format.applyPattern("####,###,###,###,##0");
        format.setRoundingMode(RoundingMode.HALF_UP);
        String retStr = number.toString();
        try{
            retStr = format.format(Double.parseDouble(number.toString()));
            if("-0".equalsIgnoreCase(retStr)){
                retStr = "0";
            }
        }catch (Exception e){}
        return retStr;
    }

    /**
     * num1 加 num2
     * @param num1
     * @param num2
     * @return
     */
    public static String add(Object num1, Object num2){
        String result = "";
        if(!isBlankEmpty(num1) || !isBlankEmpty(num2)){
            return toBigDecimal(num1).add(toBigDecimal(num2)).toPlainString();
        }
        return result;
    }
    /**
     * num1 减去 num2
     * @param num1
     * @param num2
     * @return
     */
    public static String subtract(Object num1, Object num2){
        String result = "";
        if(!isBlankEmpty(num1) || !isBlankEmpty(num2)){
            return toBigDecimal(num1).subtract(toBigDecimal(num2)).toPlainString();
        }
        return result;
    }
    /**
     * num1 乘以 num2
     * @param num1
     * @param num2
     * @return
     */
    public static Long multiply(Long num1, Long num2){
        Long result = new Long(0);
        if(num1 != null && num2 != null){
            return num1 * num2;
        }
        return result;
    }
    /**
     * num1 乘以 num2
     * @param num1
     * @param num2
     * @return
     */
    public static String multiply(Object num1, Object num2){
        String result = "";
        if(!isBlankEmpty(num1) || !isBlankEmpty(num2)){
            return toBigDecimal(num1).multiply(toBigDecimal(num2)).toPlainString();
        }
        return result;
    }
    /**
     * num1 除以 num2 结果四色五入取整
     * @param num1
     * @param num2
     * @return
     */
    public static Long divide(Long num1, Long num2){
        Long result = new Long(0);
        if(num1 != null && num2 != null){
            if(num1 != 0){
                try {
                    BigDecimal arg1 = new BigDecimal(num1);
                    BigDecimal arg2 = new BigDecimal(num2);
                    BigDecimal arg3 = arg1.divide(arg2, 0, BigDecimal.ROUND_CEILING);
                    return arg3.longValue();
                }catch (ArithmeticException e){}
            }
        }
        return result;
    }
    /**
     * num1 除以 num2 结果四色五入取整
     * @param num1
     * @param num2
     * @return
     */
    public static String divide(Object num1, Object num2){
        String result = "";
        if(!isBlankEmpty(num1) && !isBlankEmpty(num2)){
            BigDecimal arg2 = toBigDecimal(num2);
            if(arg2.compareTo(BigDecimal.ZERO) != 0){
                try {
                    BigDecimal arg1 = toBigDecimal(num1);
                    BigDecimal arg3 = arg1.divide(arg2, 0, BigDecimal.ROUND_CEILING);
                    return arg3.toPlainString();
                }catch (ArithmeticException e){}
            }else{
                return "0";
            }
        }
        return result;
    }
    /**
     * num1 除以 num2 结果四色五入,保留指定小数位
     * @param num1
     * @param num2
     * @param scale 保留小数位数，默认0只保留整数
     * @return 计算出错返回空字符
     */
    public static String divide(Object num1, Object num2, int scale){
        String result = "";
        if(!isBlankEmpty(num1) && !isBlankEmpty(num2)){
            BigDecimal arg2 = toBigDecimal(num2);
            if(arg2.compareTo(BigDecimal.ZERO) != 0){
                try {
                    if(scale < 0){
                        scale = 0;
                    }
                    BigDecimal arg1 = toBigDecimal(num1);
                    BigDecimal arg3 = arg1.divide(arg2, scale, BigDecimal.ROUND_HALF_UP);
                    return arg3.toPlainString();
                }catch (ArithmeticException e){}
            }else{
                return result;
            }
        }
        return result;
    }

    /**
     * 文件路径重命名,如果存在，文件名末尾加1
     * "d://xxx/xxxx/xx.xxxx" to "d://xxx/xxxx/xx_1.xxxx"
     * 或 "d://xxx/xxxx/xx_1.xxxx" to "d://xxx/xxxx/xx_2.xxxx"
     * @param filePath
     * @param newFileName 不包含后缀 xxx
     * @return
     */
    public static String reFilePathName(String filePath, String newFileName){

        if(isBlankEmpty(filePath)){
            return "";
        }
        String path = ""; //  "d://xxx/xxxx/"
        String name = filePath; // "xx"
        String suffix = "";  // ".xxx"
        if(filePath.indexOf("//") > -1){
            path = filePath.substring(0, filePath.indexOf("//") + 1);
            name = filePath.substring(filePath.indexOf("//") + 1);
        }
        if(filePath.indexOf(".") > -1){
            suffix = filePath.substring(filePath.indexOf("."));
            name = name.substring(0, name.indexOf("."));
        }
        String newName = newFileName;
        if(isBlankEmpty(newFileName)){
            newName = name;
        }
        String newFilePath = path + newName + suffix;
        File file = new File(newFilePath);
        if(file.exists()){
            int count = 1;
            if(newName.indexOf("_") > -1){
                String countTmp = newName.substring(newName.indexOf("_") + 1);
                if(isNumeric(countTmp)){
                    newName = newName.substring(0, name.indexOf("_"));
                    count = toInteger(countTmp);
                    count++;
                }
            }
            newName = newName + "_" + count;
            return reFilePathName(filePath, newName);
        }
        return newFilePath;
    }

    /**
     * 文件重命名,根据当前毫秒数
     * @param fileName
     * @return
     */
    public static String reFileName(String fileName){

        if(isBlankEmpty(fileName)){
            return "";
        }
        if(fileName.indexOf(".")> -1){
            return System.currentTimeMillis()+fileName.substring(fileName.indexOf("."),fileName.length());
        }
        return "";
    }
    /**
     * 删除指定地址下的文件和文件夹
     * @param path
     */
    public static void delDirAndFile(String path){
        try {
            File file = new File(path);
            if(file.exists()){
                deleteDir(file);
            }
        } catch (Exception e) {}
    }
    /**
     * 递归删除文件夹中的文件
     * @param file
     * @return
     */
    public static boolean deleteDir(File file){
        if (file.isDirectory()) {
            String[] children = file.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(file, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return file.delete();
    }
    /**
     * byte(字节)转成kb(千字节)
     *
     * @param bytes
     * @return
     */
    public static  float bytesTokb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
//		BigDecimal megabyte = new BigDecimal(1024 * 1024);
//		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
//				.floatValue();
//		if (returnValue > 1)
//			return returnValue;
//			return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        float returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
//		return (returnValue + "KB");
        return returnValue;
    }
    /**
     * 获取文件夹下所有文件,递归获取
     *
     * @param baseDir
     *            文件夹路径
     * @return 文件集合
     */
    public static List<File> getSubFiles(File baseDir) {
        List<File> ret = new ArrayList<File>();
        File[] tmp = baseDir.listFiles();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].isFile())
                ret.add(tmp[i]);
            if (tmp[i].isDirectory())
                ret.addAll(getSubFiles(tmp[i]));
        }
        return ret;
    }
    /**
     * 指定目录下，根据文件名获取文件的集合
     * @param baseDir 文件目录
     * @param fileNames 目录下文件名的集合
     * @return
     */
    public static List<File> getSubFiles(String baseDir, List<String> fileNames) {
        List<File> ret = new ArrayList<File>();
        if(fileNames == null){
            return ret;
        }
        for (int f = 0; f < fileNames.size(); f++) {
            String fileName = fileNames.get(f);
            String filePath = Paths.get(baseDir, fileName).toString();
            File file = new File(filePath);
            if(file.exists()){
                ret.add(file);
            }
        }
        return ret;
    }
    /**
     * 初始化目录结构，只初始化文件夹或父文件夹，不会创建文件
     * @param path 地址
     */
    public static void initAllDir(String path){
        try {
            File file = new File(path);
            if(!file.exists()){
                if(file.isDirectory()){
                    file.mkdirs();
                }else if(file.isFile() && !file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
            }
        } catch (Exception e) {}
    }



    /**
     * 获取昨天
     * @param pattern 要返回的日期格式, 如果格式错误，默认[yyyy-MM-dd]
     * @return
     */
    public static String getCurrentLastDay(String pattern) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.DATE, -1);
        Date dt = rightNow.getTime();
        String reStr;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            reStr = sdf.format(dt);
        }catch (Exception e){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            reStr = sdf.format(dt);
        }
        return reStr;
    }
    /**
     * 获取上一个月今天
     * @param pattern 要返回的日期格式, 如果格式错误，默认[yyyy-MM-dd]
     * @return
     */
    public static String lastMonth(String pattern) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.MONTH, -1);
        Date dt = rightNow.getTime();
        String reStr;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            reStr = sdf.format(dt);
        }catch (Exception e){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            reStr = sdf.format(dt);
        }
        return reStr;
    }
    /**
     * 获取下一个月今天
     * @param pattern 要返回的日期格式, 如果格式错误，默认[yyyy-MM-dd]
     * @return
     */
    public static String nextMonth(String pattern) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.MONTH, 1);
        Date dt = rightNow.getTime();
        String reStr;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            reStr = sdf.format(dt);
        }catch (Exception e){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            reStr = sdf.format(dt);
        }
        return reStr;
    }
    /**
     * 比较两个日期字符串的大小
     * @param dateStr1
     * @param date1Pattern 日期字符串的格式，默认[yyyy-MM-dd]
     * @param dateStr2
     * @param date2Pattern 日期字符串的格式，默认[yyyy-MM-dd]
     * @return 0相等，1 dateStr1 > dateStr2， -1 dateStr1 < dateStr2
     */
    public static int compareDate(String dateStr1, String date1Pattern,  String dateStr2, String date2Pattern){
        Date date1 = parseDate(dateStr1, date1Pattern);
        Date date2 = parseDate(dateStr2, date2Pattern);
        if(date1 == null && date2 == null){
           return 0;
        }else if(date1 == null){
            return -1;
        }else if(date2 == null){
            return 1;
        }else{
            return date1.compareTo(date2);
        }
    }
    /**
     * 获取传入日期字符串的年份
     * @param dateStr 日期字符串
     * @param pattern 日期字符串的格式，默认[yyyy-MM-dd]
     * @return
     */
    public static String getYear(String dateStr, String pattern){
        try {
            if(isBlankEmpty(dateStr)){
               return null;
            }
            return formatDate(parseDate(dateStr, pattern), "yyyy");
        }catch (Exception e){}
        return null;
    }
    /**
     * 获取传入日期字符串的上一年
     * @param dateStr 日期字符串
     * @param pattern 日期字符串的格式，默认[yyyy-MM-dd]
     * @param resultPattern 返回日期字符串的格式，默认[yyyy-MM-dd]
     * @return
     */
    public static String getLastYear(String dateStr, String pattern, String resultPattern){
        try {
            if(isBlankEmpty(dateStr)){
                return null;
            }
            Date date = parseDate(dateStr, pattern);
            if(date != null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.YEAR, -1);
                Date dt = calendar.getTime();
                return formatDate(dt, resultPattern);
            }
        }catch (Exception e){}
        return null;
    }
    /**
     * 获取日期对象， 失败返回null
     * @param dateStr 日期字符串
     * @param pattern 字符串格式，默认[yyyy-MM-dd]
     * @return
     */
    public static Date parseDate(String dateStr, String pattern){
        try {
            if(isBlankEmpty(dateStr)){
                return null;
            }
            if(isBlankEmpty(pattern)){
                pattern = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        }catch (Exception e){}
        return null;
    }
    /**
     * 将日期格式字符串，转换成新的格式
     * @param dateStr 日期字符串
     * @param pattern 原字符串格式，默认[yyyy-MM-dd]
     * @param newPattern 新字符串格式，默认[yyyy-MM-dd]
     * @return
     */
    public static String formatDate(String dateStr, String pattern, String newPattern){
        try {
            Date date = parseDate(dateStr, pattern);
            if(date != null){
                return formatDate(date, newPattern);
            }
        }catch (Exception e){}
        return null;
    }
    /**
     * 获取当前日期字符串， 失败返回null
     * @param resultPattern 返回日期字符串格式，默认[yyyy-MM-dd]
     * @return
     */
    public static String getCurrentFormatDate(String resultPattern){
        return formatDate(new Date(), resultPattern);
    }
    /**
     * 将日期格式字符串，转换成新的格式
     * @param dateStr 日期字符串
     *                支持的格式 yyyy-MM-dd、yyyy-MM
     *                 yyyy/MM/dd、yyyy/MM
     *                 yyyyMMdd、yyyyMM
     *                 yyyy
     * @param resultPattern 新字符串格式，默认[yyyy-MM-dd]
     * @return
     */
    public static String formatDate(String dateStr, String resultPattern){
        try {
            String pattern;
            if(isBlankEmpty(dateStr)){
                return null;
            }
            if(dateStr.contains("-")){
                if(dateStr.length() == 10){
                    pattern = "yyyy-MM-dd";
                }else{
                    pattern = "yyyy-MM";
                }
            }else if(dateStr.contains("/")){
                if(dateStr.length() == 10){
                    pattern = "yyyy/MM/dd";
                }else{
                    pattern = "yyyy/MM";
                }
            }else{
                if(dateStr.length() == 8){
                    pattern = "yyyyMMdd";
                }else if(dateStr.length() == 6){
                    pattern = "yyyyMM";
                }else{
                    pattern = "yyyy";
                }
            }
            Date date = parseDate(dateStr, pattern);
            if(date != null){
                return formatDate(date, resultPattern);
            }
        }catch (Exception e){}
        return null;
    }
    /**
     * 获取日期字符串， 失败返回null
     * @param date 日期
     * @param pattern 字符串格式，默认[yyyy-MM-dd]
     * @return
     */
    public static String formatDate(Date date, String pattern){
        try {
            if(date==null){
                return null;
            }
            if(isBlankEmpty(pattern)){
                pattern = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }catch (Exception e){}
        return null;
    }
    /**
     * 毫秒转换为日期格式字符串， 失败返回null
     * @param timeObj 毫秒对象
     * @param pattern 字符串格式，默认[yyyy-MM-dd]
     * @return
     */
    public static String formatTime(Object timeObj, String pattern){
        try {
            if(isBlankEmpty(pattern)){
                pattern = "yyyy-MM-dd";
            }
            Long time = toLong(timeObj);
            if(time <= 0){
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(time);
        }catch (Exception e){}
        return null;
    }


    /**
     * Map 的value是否都为空字符
     * @param map
     * @return true 是，false 不是
     */
    public static boolean isMapBlankValue(Map map){
        if(map == null){
            return true;
        }
        for (Object value : map.values()){
            if(value != null){
                if(!isBlankEmpty(value.toString())){
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * 移除Map中 value是空字符的键值对
     * @param map
     * return 移除的Key的集合
     */
    public static List removeMapBlankValue(Map map){
        List result = new ArrayList<>();
        if(map != null && map.size() > 0){
            Iterator<Map.Entry> it = map.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry=it.next();
                Object value = entry.getValue();
                if(value == null){
                    it.remove();
                    result.add(entry.getKey());
                }else{
                    if(isBlankEmpty(value.toString())){
                        it.remove();
                        result.add(entry.getKey());
                    }
                }
            }
        }
        return result;
    }
    /**
     * 移除Map中 value是空字符的键值对
     * @param list
     * return 移除的Key的集合
     */
    public static List removeListMapBlankValue(List<Map<String, String>> list){
        List result = new ArrayList<>();
        if(list != null && list.size() > 0){
            Map<Object, Integer> blankKey = new HashMap();
            // 获取为空的key的数量
            for (int i = 0; i < list.size(); i++) {
                Map map = list.get(i);
                Iterator<Map.Entry> it = map.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry entry=it.next();
                    Object value = entry.getValue();
                    if(isBlankEmpty(value)){
                        Object key = entry.getKey();
                        Integer count = 0;
                        if(blankKey.containsKey(key)){
                            count = blankKey.get(key);
                        }
                        count = count + 1;
                        blankKey.put(key, count);
                    }
                }
            }
            // 获取数量等于集合数量的空字符key
            for (Object key : blankKey.keySet()){
                Integer count = blankKey.get(key);
                if(count == list.size()){
                    result.add(key);
                }
            }
            removeListMapByKeys(list, result.iterator());
        }
        return result;
    }
    /**
     * 根据key，移除Map中的键值对
     * @param map
     * @param keys
     * @return 移除的数量
     */
    public static int removeMapByKeys(Map map, Iterator keys){
        int count = 0;
        if(map != null && map.size() > 0 && keys != null){
            while (keys.hasNext()){
                Object key = keys.next();
                if(map.containsKey(key)){
                    map.remove(key);
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * 根据key，移除Map中的键值对
     * @param list
     * @param keys
     * @return 移除的数量
     */
    public static int removeListMapByKeys(List<Map<String, String>> list, Iterator keys){
        int count = 0;
        if(list != null && list.size() > 0 && keys != null){
            List keyList = com.google.common.collect.Lists.newArrayList(keys);
            for (int i = 0; i < list.size(); i++) {
                Map map = list.get(i);
                if(map != null && map.size() > 0){
                    for (int j = 0; j < keyList.size(); j++) {
                        if(map.containsKey(keyList.get(j))){
                            map.remove(keyList.get(j));
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }
    /**
     * 根据key获取map集合value的值
     * @param list
     * @param key
     * @return
     */
    public static <K, V> List<V> getValueListByKey(List<Map<K, V>> list, String key){
        List result = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            Iterator<Map<K, V>> iter = list.iterator();
            while (iter.hasNext()){
                Map map = iter.next();
                result.add(map.get(key));
            }
        }
        return result;
    }

    public static void main(String[] arg){
        System.out.println(formatNumbersToString0("111.0000"));

        BigDecimal hundred = new BigDecimal(-0.1);
//        System.out.println(Utils.compareBigDecimal(hundred, 0));
//        System.out.println(toBigDecimal("123,123.12").toPlainString());
//       String value = Utils.formatNumbersToString("5.7708E7"); // 避免过长科学计数法，转为实际值
//        System.out.println(formatNumbersToString(value));
//            value = Utils.divide(value, 1000, 4);
//        System.out.println(formatNumbersToString(value));
//        value = Utils.formatNumbers0(value);
//        System.out.println(formatNumbersToString(value));
//        System.out.println(formatDate("2015/11/01", "yyyy-MM-dd"));
    /*    List<Map<String, String>> list = new ArrayList<>();
        Map map1 = new HashMap();
        map1.put("a", "11");
        map1.put("b", "");
        map1.put("c", "11");
        list.add(map1);

        Map map2 = new HashMap();
        map2.put("a", "11");
        map2.put("b", "");
        map2.put("c", "11");
        list.add(map2);

        Map map3 = new HashMap();
        map3.put("a", "");
        map3.put("b", "");
        map3.put("c", "11");
        list.add(map3);*/
        List a = getLastTenYear();
//        System.out.print(a.get(0));
//        List result = removeListMapBlankValue(list);

//        System.out.println(joinExcludeBlank(true, " / ", "", "ccc",null, "123", "ccc"));
//        System.out.println(formatTime("730569600000", null));
//        System.out.println(formatNumbersToString("110,570.0"));
//        System.out.println(divide("11057059", 10000, 2));

//        System.out.println(getLastYear("2015", "yyyy", "yyyy"));
//        System.out.println(compareDate("2013", "yyyy", "2014-12-13", "yyyy"));

//        System.out.println(toBigDecimal("7.00"));
//        System.out.println(formatNumbers("00111111"));
//        System.out.println(new Date().getTime());
//        System.out.println(new Date().getTime());
//        System.out.println(new Date().getTime());

//        System.out.println(full2Half("倒计时hh哈哈ｋｋｋｄ（aa）　ｋ (ｄｄ)"));
//        System.out.println(half2Full("倒计时hh哈哈ｋｋｋｄ（aa）　ｋ (ｄｄ)"));
//        System.out.println(full2Half("倒计时sjdfkjd(jsdfkjd)kdjfkdjf kdjf    "));
    }

    /**
     * 得到最新十年的年份
     * @return
     */
    public static List getLastTenYear(){
        List years = new ArrayList();
        Calendar c = Calendar.getInstance();
        int year=  c.get(Calendar.YEAR);
        for(int i = 0;i<10;i++){
            years.add(year);
            year--;
        }

        return years;

    }
}
