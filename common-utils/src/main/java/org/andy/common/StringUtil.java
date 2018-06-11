package org.andy.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串Util类。
 * <p>字符串通用处理</p>
 */
public final class StringUtil {

    private static final String CHARSET_UTF_8 = "UTF-8";

    /**
     * 验证为空（不对入参去空格处理，" " 也表示有字符）
     *
     * @param str 输入字符串
     * @return true 空；false 非空
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 验证非空（不对入参去空格处理，" " 也表示有字符）
     *
     * @param str 输入字符串
     * @return true 非空；false 空
     */
    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    /**
     * 验证为空（会对入参去空格处理）
     *
     * @param str 输入字符串
     * @return true 空；false 非空
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * 验证非空（会对入参去空格处理）
     *
     * @param str 输入字符串
     * @return true 非空；false 空
     */
    public static boolean isNotBlank(String str) {
        return str != null && str.trim().length() > 0;
    }

    /**
     * 将年月日合并为给定分隔符的字符串
     * 例：year=2013，month=1，day=31，toten=/
     * 合并结果 2013/01/31
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日 1-31
     * @param token 分割符号
     * @return 日期字符串
     */
    public static String combinateDate2String(int year,
                                              int month, int day, String token) {
        String m = (month > 9 ? String.valueOf(month) : "0" +
            month);
        return year + token + m + token + day;
    }

    /**
     * null 转  ""
     */
    public static String null2Empty(Object val) {
        return val == null ? "" : val.toString();
    }

    /**
     * 字符串是否是纯数字
     *
     * @param str 输入字符串
     * @return true 数字； false 非数字
     */
    public static boolean isNumber(String str) {
        if (isBlank(str)) {
            return false;
        }
        char c;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串为非纯数字
     *
     * @param str 输入字符串
     * @return true 非数字； false 数字
     */
    public static boolean isNotNumber(String str) {
        return !isNumber(str);
    }

    /**
     * 字符是否是数字
     *
     * @param c 输入字符
     * @return true 数字； false 非数字
     */
    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isNotNumber(char c) {
        return !isNumber(c);
    }

    /**
     * 将首字符转换为大写
     *
     * @param str
     * @return
     */
    public static final String initcap(String str) {
        if (isBlank(str)) {
            return null;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 输出异常的完整信息
     *
     * @param e 异常
     * @return 异常堆栈信息
     */
    public static final String getTrace(Throwable e) {
        StringWriter stringWriter = null;
        try {
            stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            e.printStackTrace(writer);
            StringBuffer buffer = stringWriter.getBuffer();
            return buffer.toString();
        } finally {
            if (stringWriter != null) {
                try {
                    stringWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取字符串编码
     *
     * @param str 输入字符串
     * @return
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    /**
     * 字符串编码转换为 CHARSET_UTF_8
     *
     * @param value       输入字符串
     * @param charsetName 字符编码
     * @return
     */
    public static String getStrFormat(String value, String charsetName) {
        try {
            return new String(value.getBytes(charsetName), CHARSET_UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getListToString(List<String> addList) {
        StringBuffer buffer = new StringBuffer();
        for (int i = addList.size() - 1; i > -1; i--) {
            if (i > addList.size() - 1 - i || i > 0) {
                buffer.append(addList.get(i)).append(",");
            } else {
                buffer.append(addList.get(i));
            }
        }
        if (buffer.length() > 0) {
            String address = buffer.substring(0, buffer.lastIndexOf(","));
            return address;
        }
        return buffer.toString();
    }

    public static String listToString(Set<String> list) {
        StringBuilder builder = null;
        if (null == list || list.size() < 1) {
            return null;
        }
        builder = new StringBuilder();
        for (Object obj : list) {
            String value = (String)obj;
            if (isBlank(value)) {
                continue;
            }
            builder.append(value).append(",");
        }

        if (builder.toString().length() > 0 && ((builder.toString().length() - 1) == builder.lastIndexOf(","))) {
            builder.deleteCharAt(builder.lastIndexOf(","));
        }
        return builder.toString();
    }

    public static String getListIntToString(List<?> addList) {
        StringBuffer buffer = new StringBuffer();
        for (int i = addList.size() - 1; i > -1; i--) {
            if (i > addList.size() - 1 - i || i > 0) {
                buffer.append(addList.get(i)).append(",");
            } else {
                buffer.append(addList.get(i));
            }
        }
        return buffer.toString();
    }

    public static String getStringForNum(String value) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(value);
        return m.replaceAll("").trim();
    }

    /**
     * 判断字符数组中是否包含某个字符
     *
     * @param arr         字符数组
     * @param targetValue 字符
     */
    public static boolean isContains(String[] arr, String targetValue) {
        for (String s : arr) {
            if (s.equals(targetValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数组装换为字符串
     */
    public static String arrayToString(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (String anArr : arr) {
            sb.append(anArr);
        }
        return sb.toString();
    }

    /**
     * 字符串 转 List
     */
    public static List<String> stringToList(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String[] arr = str.split(",");
        return java.util.Arrays.asList(arr);
    }

    /**
     * 将字符串转移为ASCII码
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

    public static void main(String[] args) {
    }

}
	
