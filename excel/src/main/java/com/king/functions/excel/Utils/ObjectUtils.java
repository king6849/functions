package com.king.functions.excel.Utils;

import java.util.Date;

public class ObjectUtils {
    public static boolean isNullStringObj(String string) {
        return string == null || "".equals(string.trim());
    }

    public static boolean isNullDateObj(Date date) {
        return date == null;
    }

    public static String stringToDouble(Object object) {
        if (object == null || isNullStringObj(object.toString())) {
            return "0";
        }
        return object.toString();

    }
}
