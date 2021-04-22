package com.king.function.excel.Mapper;


import java.util.HashMap;
import java.util.LinkedHashMap;

public class ExcelSqlProvider {

    /**
     * @param params table:tableName 字段1:value, 字段2:value2
     * @return
     */
    public String saveExcelDatas(LinkedHashMap<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").
                append(params.get("table").toString())
                .append("(");
//        构建插入的属性
        for (String key : params.keySet()) {
            if (key.equals("id") || key.equals("table")) {
                continue;
            }
            sql.append(key).append(",");
        }
        sql.replace(sql.length() - 1, sql.length() , "");
        sql.append(")").append(" values(");
//        预设参数
        for (String key : params.keySet()) {
            if (key.equals("table")) {
                continue;
            }
            sql.append("#{").append(key).append("},");
        }
        sql.replace(sql.length() - 1, sql.length() , "");
        sql.append(");");
        return sql.toString();
    }

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
