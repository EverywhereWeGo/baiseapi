package com.bfd.utils;

public class i_StringUtil {
    public static String getLastSubString(String str, String startstr, String endstr) throws Exception {
        int start = str.lastIndexOf(startstr);
        int end = str.lastIndexOf(endstr);
        if ((-1 == start) || (-1 == end)) {
            throw new Exception("未找到起始子串或结束子串");
        }
        return str.substring(start + startstr.length(), end);
    }

    public static String getFirstSubString(String str, String startstr, String endstr) throws Exception {
        int start = str.indexOf(startstr);
        int end = str.indexOf(endstr);
        if ((-1 == start) || (-1 == end)) {
            throw new Exception("未找到起始子串或结束子串");
        }
        return str.substring(start + startstr.length(), end);
    }

    //首字母变大写
    public static String firsttoUpperCase(String str) {
        char[] strChar = str.toCharArray();
        strChar[0] -= 32;
        return String.valueOf(strChar);
    }

    public static String stringJoin(String[] strs, String charset) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length - 1; i++) {
            sb.append(strs[i]).append(charset);
        }
        sb.append(strs[strs.length - 1]);
        return sb.toString();
    }
}
