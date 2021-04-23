package com.king.function.excel.Excel;

import com.king.function.excel.Excel.BaseProcess.BaseExportExcel;
import com.king.function.excel.Utils.ObjectUtils;
import com.king.function.excel.Utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class DynamicExportExcelData extends BaseExportExcel {


    /**
     * 导出简单结构数据
     *
     * @param sheetName
     * @param rowName
     * @param savePath
     * @param outerSQLConditions
     */
    @Async("asyncExcelExecutor")
    public void exportSimpleExcelDate(String sheetName, List<String> rowName, String savePath, LinkedHashMap<String, Object> outerSQLConditions) {
        List<LinkedHashMap<String, Object>> datalist = outSqlResult(outerSQLConditions);
        List<Object[]> rowData = new ArrayList<>();
        for (LinkedHashMap<String, Object> record : datalist) {
            int i = 0;
            Object[] rowValue = new Object[rowName.size()];
            for (String next : record.keySet()) {
                rowValue[i] = record.get(next);
                ++i;
            }
            rowData.add(rowValue);
        }
        exportExcel(sheetName, rowName, rowData, savePath);
    }


    /***
     * @description: 导出复杂的Excel数据
     * @param: [rowName, outerSQLConditions, innerSqlCondition]
     * @return: void
     */
    @Async("asyncExcelExecutor")
    public void dynamicExportExcel(List<String> rowName, String savePathRoot, LinkedHashMap<String, Object> outerSQLConditions, LinkedHashMap<String, Object> innerSqlCondition) {
        if (ObjectUtils.isNullStringObj(savePathRoot)) {
            savePathRoot = pathUtil.getBase_export_path();
        }
        int rowNameLength = rowName.size();
        //外层sql条件
        List<LinkedHashMap<String, Object>> classNameList = outSqlResult(outerSQLConditions);
        //内存sql查询条件  key是sql字段，value是条件值
        LinkedHashMap<String, Object> condition = new LinkedHashMap<>();
        //循环遍历SQL查询条件，再来查询实际想要的数据
        for (LinkedHashMap<String, Object> outResultItem : classNameList) {
            //正式开始内层sql查询，得到查询结果resultMaps
            condition.put("sql", innerSqlCondition.get("sql"));
            //拿到每张表的数据
            List<Object[]> dataList = inSqlResult(outResultItem, condition, rowNameLength);
            //导出到Excel
            exportExcel(null, rowName, dataList, condition, savePathRoot);
            //清空内存SQL查询条件
            condition.clear();
        }
    }

}
