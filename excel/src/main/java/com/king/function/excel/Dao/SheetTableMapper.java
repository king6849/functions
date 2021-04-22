package com.king.function.excel.Dao;

import com.king.function.excel.Mapper.ExcelSqlProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface SheetTableMapper {

    /**
     * 将Excel数据保存到数据库
     *
     * @param params table:tableName 字段1:value, 字段2:value2
     */
    @SelectProvider(type = ExcelSqlProvider.class, method = "saveExcelDatas")
    void saveExcelData(LinkedHashMap<String, Object> params);

    /**
     * 获取数据库所有字段
     *
     * @param table 表名
     * @return 表字段
     */
    @Select("select * from ${table} limit 1;")
    LinkedHashMap<String, Object> sqlStructure(String table);

    /**
     * 外层SQL查询条件
     *
     * @param params 参数以及sql语句
     */
    @SelectProvider(type = ExcelSqlProvider.class, method = "outerSQLCondition")
    List<LinkedHashMap<String, Object>> outerSQLCondition(HashMap<String, Object> params);

    /**
     * 内存实际查询结果
     *
     * @param params 参数以及sql语句
     */
    @SelectProvider(type = ExcelSqlProvider.class, method = "selectMapResults")
    List<LinkedHashMap<String, Object>> selectMapResults(LinkedHashMap<String, Object> params);


}
