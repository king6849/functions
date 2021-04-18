package com.king.functions.excel.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ExcelUtil {
    /**
     * 生成随机图片文件名，"年月日时分秒"格式
     */
    public String randomFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(date);
    }
}
