package com.king.functions.excel.Mapper;


import java.util.HashMap;
import java.util.LinkedHashMap;

public class ExcelSqlProvider {

    /**
     * 外层SQL查询条件
     *
     * @param params 参数
     */
    public String outerSQLCondition(HashMap<String, Object> params) {
        return params.get("sql").toString();
    }

    /**
     * 内存实际查询结果
     *
     * @param params 参数
     */
    public String selectMapResults(LinkedHashMap<String, Object> params) {
        return params.get("sql").toString();
    }


}
