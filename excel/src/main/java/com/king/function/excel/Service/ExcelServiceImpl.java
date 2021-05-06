package com.king.function.excel.Service;


import com.king.function.excel.Excel.DynamicExportExcelData;
import com.king.function.excel.Excel.ImportExcelData;
import com.king.function.excel.Excel.ParseExcelHeader;
import com.king.function.excel.Exception.SqlFiledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author king
 */
@Service
public class ExcelServiceImpl {

    @Autowired
    private ImportExcelData importExcelData;

    @Autowired
    private DynamicExportExcelData dynamicExportExcelData;

    @Autowired
    private ParseExcelHeader parseExcelHeader;

    /***
     * @description: 将Excel保存到数据库
     */
    @Async("asyncExcelExecutor")
    public String saveExcel(MultipartFile excel, String table) throws SqlFiledException {
        String saveUploadFile = "";
        try {
            importExcelData.setTable(table);
            saveUploadFile = importExcelData.saveUploadFile(excel);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SqlFiledException) {
                SqlFiledException e1 = (SqlFiledException) e;
                System.out.println(e1.toString());
            }
        }
        importExcelData.dealExcel(excel, saveUploadFile);
        File file = new File(saveUploadFile);
        if (file.exists()) {
            System.gc();
            if (file.delete()) {
                System.out.println("已删除文件 " + saveUploadFile);
            }
        }
        return "已完成解析文件";
    }

    //导出单张Excel数据
    public void exportSimpleExcelDate(String sheetName, List<String> rowName, String savePath, LinkedHashMap<String, Object> outerSQLConditions) {
        dynamicExportExcelData.exportSimpleExcelDate(sheetName, rowName, savePath, outerSQLConditions);
    }

    /**
     * 导出多张Excel文件，分层写出
     *
     * @param rowName            列头
     * @param outerSQLConditions 外层SQL语句以及查询参数
     * @param innerSqlCondition  内存SQL语句以及查询参数
     */
    public void dynamicExportExcel(List<String> rowName, String savePathRoot, LinkedHashMap<String, Object> outerSQLConditions, LinkedHashMap<String, Object> innerSqlCondition) {
        dynamicExportExcelData.dynamicExportExcel(rowName, savePathRoot, outerSQLConditions, innerSqlCondition);
    }

    /***
     * @description: 解析表头
     * @param: [excel] 文件
     */
    public void parseHeader(MultipartFile excel) {
        String saveUploadFile = "";
        try {
            saveUploadFile = importExcelData.saveUploadFile(excel);
            parseExcelHeader.dealExcel(excel, saveUploadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(saveUploadFile);
        if (file.exists()) {
            System.gc();
            if (file.delete()) {
                System.out.println("已删除文件 " + saveUploadFile);
            }
        }
    }
}
