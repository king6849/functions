package com.king.function.excel.Excel.BaseProcess;

import com.king.function.excel.Dao.SheetTableMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class BaseExportExcel extends BaseWriteXLSXExcel {

    @Autowired
    private SheetTableMapper sheetTableMapper;

    /**
     * 外层查询结果
     *
     * @param outerSQLConditions sql语句级参数
     */
    protected List<LinkedHashMap<String, Object>> outSqlResult(LinkedHashMap<String, Object> outerSQLConditions) {
        //外层sql条件
        List<LinkedHashMap<String, Object>> datalist = sheetTableMapper.outerSQLCondition(outerSQLConditions);
        return datalist;
    }

    /**
     * 内存查询结果
     *
     * @param outResultItem 条件集合
     * @param condition     sql语句，预设参数键值对
     * @param rowNameLength Excel列长
     */
    protected List<Object[]> inSqlResult(LinkedHashMap<String, Object> outResultItem, LinkedHashMap<String, Object> condition, int rowNameLength) {
        //构建内层sql查询条件
        for (String filedName : outResultItem.keySet()) {
            condition.put(filedName, outResultItem.get(filedName));
        }
        List<Object[]> dataList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> resultMaps = sheetTableMapper.selectMapResults(condition);
        if (resultMaps.size() == 0) {
            return null;
        }
        for (HashMap<String, Object> record : resultMaps) {
            //这个sheet有几列
            Object[] data = new Object[rowNameLength];
            //为每个列赋值
            int i = 0;
            for (String filed : record.keySet()) {
                data[i] = record.get(filed);
                ++i;
            }
            //将该行数据保存起来
            dataList.add(data);
        }
        return dataList;
    }

    protected void exportExcel(String sheetName, List<String> rowName, List<Object[]> dataList, String savePath) {
        exportExcel(sheetName, rowName, dataList, null, savePath);
    }

    /**
     * 导出Excel表
     *
     * @param title        标题
     * @param rowName      列头
     * @param dataList     每行数据集
     * @param condition    sql查询条件集，用于分层存储文件
     * @param savePathRoot 文件存储根目录
     */
    protected void exportExcel(String title, List<String> rowName, List<Object[]> dataList, LinkedHashMap<String, Object> condition, String savePathRoot) {
        //excel 抬头（标题）
        setTitle(title);
        //excel列名
        setRowName(rowName);
        //每一行的数据
        setDataList(dataList);
        try {
            StringBuilder dirPath = new StringBuilder();
            dirPath.append(savePathRoot);
            if (condition != null) {
                //导出到Excel表
                for (String dir : condition.keySet()) {
                    if (dir.equals("sql")) {
                        continue;
                    }
                    dirPath.append("\\").append(condition.get(dir));
                }
            } else {
                dirPath.append("\\").append(title);
            }
            dirPath.append(".").append(BaseSaveFile.xlsx);
            exportExcelFile(dirPath.toString(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSheetTableMapper(SheetTableMapper mapper) {
        this.sheetTableMapper = mapper;
    }
}
