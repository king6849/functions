package com.king.function.excel.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * 时间格式化工具
 */
public class DateTransferUtil {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

    private final DateTimeFormatter simpleFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter sqlSimpleFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

    private final static HashMap<String, SimpleDateFormat> simpleDateFormatMap = new HashMap<>();
    private final static HashMap<String, DateTimeFormatter> dateTimeFormatterMap = new HashMap<>();

    /**
     * @description: 将date对象转换成字符串
     */
    public String transferDateToString(Date date, String expression) {
        if (ObjectUtils.isNullDateObj(date)) {
            return null;
        }
        String dateString = "";
        //使用默认的格式化方式
        if (expression == null || expression.trim().equals("")) {
            try {
                dateString = dateToString(date, simpleFormat);
            } catch (Exception e) {
                dateString = dateToString(date, sqlSimpleFormat);
            }
        } else {
            //自定义格式化格式
            dateString = simpleFormatCustomizeDTS(date, expression);
        }
        return dateString;
    }

    public String simpleFormatCustomizeDTS(Date date, String expression) {
        String dateString = "";
        SimpleDateFormat simpleDateFormat = simpleDateFormatMap.get(expression);
        if (simpleDateFormat == null) {
            if (ObjectUtils.isNullStringObj(expression)) {
                simpleDateFormat = this.dateFormat;
            } else {
                simpleDateFormat = new SimpleDateFormat(expression);
                simpleDateFormatMap.put(expression, simpleDateFormat);
            }
        }
        dateString = simpleDateFormat.format(date);
        return dateString;
    }

    /**
     * @description: 将字符串转换成时间对象
     */
    public Date transferStringTotDate(Object date, String expression) throws ParseException {
        if (date == null || "".equals(date.toString().trim())) {
            return null;
        }
        Date resultDate = null;
        if (ObjectUtils.isNullStringObj(expression)) {
            try {
                //一般的格式
                resultDate = stringTotDate(date, dateFormat);
            } catch (ParseException e) {
                //解析sql的时间格式
                resultDate = stringTotDate(date, sqlDateFormat);
            }
        } else {
            //自定义格式化格式
            SimpleDateFormat simpleDateFormat = simpleDateFormatMap.get(expression);
            if (simpleDateFormat == null) {
                simpleDateFormat = new SimpleDateFormat(expression);
                simpleDateFormatMap.put(expression, simpleDateFormat);
            }
            resultDate = simpleDateFormat.parse(date.toString());
        }
        return resultDate;
    }


    /**
     * @description: 将date对象转换成字符串
     */
    private String dateToString(Date date, DateTimeFormatter formatter) {
        TemporalAccessor parseDate = formatter.parse(date.toString());
        return simpleFormat.format(parseDate);
    }

    /**
     * @description: 将字符串转换成时间对象
     */
    private Date stringTotDate(Object date, SimpleDateFormat format) throws ParseException {
        return format.parse(date.toString());
    }

    public static HashMap<String, SimpleDateFormat> getSimpleFormatMap() {
        return simpleDateFormatMap;
    }

    public static HashMap<String, DateTimeFormatter> getSqlSimpleFormatMap() {
        return dateTimeFormatterMap;
    }
}
