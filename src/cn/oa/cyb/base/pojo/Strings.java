package cn.oa.cyb.base.pojo;

import java.util.LinkedList;
import java.util.List;

public class Strings {

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isBlank(CharSequence str) {
        if (str == null)
            return true;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            //判断字符串中是否有空格
            if (Character.isWhitespace(str.charAt(i)) == false)
                return false;
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static String lowerCase(String str) {
        if (str == null)
            return null;
        return str.toLowerCase();
    }

    public static String upperCase(String str) {
        if (str == null)
            return null;
        return str.toUpperCase();
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }


    public static String trim(CharSequence str) {
        if (str == null)
            return null;
        int length = str.length();
        if (length == 0)
            return str.toString();
        int l = 0;
        int last = length - 1;
        int r = last;
        for (; l < length; l++) {
            if (!Character.isWhitespace(str.charAt(l)))
                break;
        }
        for (; r > l; r--) {
            if (!Character.isWhitespace(str.charAt(r)))
                break;
        }
        if (l > r)
            return "";
        else if (l == 0 && r == last)
            return str.toString();
        return str.subSequence(l, r + 1).toString();
    }

    public static String[] splitIgnoreBlank(String str) {
        return splitIgnoreBlank(str, ",");
    }

    public static String[] splitIgnoreBlank(String str, String regex) {
        if (str == null)
            return null;
        String[] strs = str.split(regex);
        List<String> list = new LinkedList<String>();
        for (String s : strs) {
            if (isBlank(s))
                continue;
            list.add(trim(s));
        }
        return list.toArray(new String[list.size()]);
    }

    public static String before(String str, String spr) {
        if (isEmpty(str) || spr == null)
            return str;
        if (spr.length() == 0)
            return "";
        int pos = str.indexOf(spr);
        if (pos == -1)
            return str;
        return str.substring(0, pos);
    }

    public static String after(String str, String spr) {
        if (isEmpty(str))
            return str;
        if (spr == null)
            return "";
        int pos = str.indexOf(spr);
        if (pos == -1)
            return str;
        return str.substring(pos + spr.length());
    }

    public static String beforeLast(String str, String spr) {
        if (isEmpty(str) || isEmpty(spr))
            return str;
        int pos = str.lastIndexOf(spr);
        if (pos == -1)
            return str;
        return str.substring(0, pos);
    }

    public static String afterLast(String str, String spr) {
        if (isEmpty(str))
            return str;
        if (isEmpty(spr))
            return "";
        int pos = str.lastIndexOf(spr);
        if (pos == -1 || pos == str.length() - spr.length())
            return "";
        return str.substring(pos + spr.length());
    }

    public static String removeStart(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove))
            return str;
        if (str.startsWith(remove))
            return str.substring(remove.length());
        return str;
    }

    public static String removeEnd(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove))
            return str;
        if (str.endsWith(remove))
            return str.substring(0, str.length() - remove.length());
        return str;
    }
}
