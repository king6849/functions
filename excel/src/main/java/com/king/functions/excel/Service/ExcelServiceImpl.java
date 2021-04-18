package com.king.functions.excel.Service;


import com.king.functions.excel.Excel.DynamicExportExcelData;
import com.king.functions.excel.Excel.ExportExcelDataImpl;
import com.king.functions.excel.Excel.SaveExcelDate;
import com.king.functions.excel.Exception.IllegalFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ExcelServiceImpl extends DealExcelBaseService {

    @Autowired
    private SaveExcelDate studentGradeProcessExcel;

    @Autowired
    private ExportExcelDataImpl exportExcelDataImpl;

    @Async("asyncExcelExecutor")
    public void saveExcel(MultipartFile excel) {
        String saveUploadFile = "";
        try {
            saveUploadFile = studentGradeProcessExcel.saveUploadFile(excel);
        } catch (IOException | IllegalFileException e) {
            e.printStackTrace();
        }
        studentGradeProcessExcel.dealExcel(saveUploadFile);
        new File(saveUploadFile).delete();
    }

    //导出Excel数据
    public void exportSimpleExcelDate(String sheetName, String[] rowName, String savePath, LinkedHashMap<String, Object> outerSQLConditions) {
        exportExcelDataImpl.exportSimpleExcelDate(sheetName, rowName, savePath, outerSQLConditions);
    }


    public List<Object[]> inSqlResult(LinkedHashMap<String, Object> outResultItem, LinkedHashMap<String, Object> condition, int rowNameLength) {
        return exportExcelDataImpl.inSqlResult(outResultItem, condition, rowNameLength);
    }

    public void dynamicExportExcel(String[] rowName, LinkedHashMap<String, Object> outerSQLConditions, LinkedHashMap<String, Object> innerSqlCondition) {
        exportExcelDataImpl.dynamicExportExcel(rowName, outerSQLConditions, innerSqlCondition);
    }
}
