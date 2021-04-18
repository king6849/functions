package com.king.functions.excel.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.functions.excel.Mapper.ExcelSqlProvider;
import com.king.functions.excel.Model.SheetTable2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface SheetTable2Mapper extends BaseMapper<SheetTable2> {

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


    List<HashMap<String, Object>> selectGrade(HashMap<String, Object> params);

    List<HashMap<String, String>> conditions();

}
