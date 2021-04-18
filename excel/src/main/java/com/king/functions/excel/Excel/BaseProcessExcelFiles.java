package com.king.functions.excel.Excel;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.king.functions.excel.Exception.IllegalFileException;
import com.king.functions.excel.Utils.DateTransferUtil;
import com.king.functions.excel.Utils.ObjectUtils;
import com.king.functions.excel.Utils.PathUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

public abstract class BaseProcessExcelFiles {

    protected DateTransferUtil dateTransferUtil = new DateTransferUtil();


    public String saveUploadFile(MultipartFile file) throws IOException, IllegalFileException {
        return saveUploadFile(file, null, null);
    }

    /***
     * @description: 将上传的文件存储到指定位置
     * @param: [file, saveRootPath, fileName]
     * @return: String 文件存储路径
     */
    public String saveUploadFile(MultipartFile file, String saveRootPath, String fileName) throws IOException, IllegalFileException {
        if (ObjectUtils.isNullStringObj(fileName)) {
            fileName = file.getOriginalFilename();
        }
        if (ObjectUtils.isNullStringObj(saveRootPath)) {
            saveRootPath = PathUtil.DEFAULT_EXPORT_BASE_PATH;
        }
        String savePath = saveRootPath + "\\" + fileName;
        if (isLegalDocument()) {
            file.transferTo(new File(savePath));
        } else {
            throw new IllegalFileException("文件格式不对");
        }
        return savePath;
    }

    /***
     * @description: 循环处理Excel表格
     * @param: [filePath]
     * @return: void
     */
    public void dealExcel(String filePath) {
        Workbook workbook = getWorkBook(filePath);
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            //根据第一行是否为空判断为空表
            org.apache.poi.ss.usermodel.Row row = sheet.getRow(0);
            if (row == null) {
                continue;
            }
            dealRows(sheet);
        }
    }

    public double getCellDoubleValue(Cell cell) {
        return Double.parseDouble(ObjectUtils.stringToDouble(getCellValue(cell).toString()));
    }

    public Object getCellValue(Cell cell) {
        return getCellValue(cell, null, null);
    }

    /**
     * 获取每个单元格的数据
     *
     * @param cell
     * @param numberExpression
     * @param dateExpression
     * @return
     */
    public Object getCellValue(Cell cell, String numberExpression, String dateExpression) {
        //判断是否为null或空串
        if (ObjectUtils.isNullStringObj(cell.toString())) {
            return "";
        }
        String cellValue = "";
        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_STRING:  //字符串类型
                cellValue = cell.getStringCellValue().trim();
                cellValue = StringUtils.isEmpty(cellValue) ? "" : cellValue;
                break;
            case Cell.CELL_TYPE_BOOLEAN:   //布尔类型
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:  //数值类型
                //判断日期类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    cellValue = dateTransferUtil.transferDateToString(cell.getDateCellValue(), dateExpression);
                } else {
                    //是普通数值类型
                    if (ObjectUtils.isNullStringObj(numberExpression)) {
                        numberExpression = "#.######";
                    }
                    cellValue = new DecimalFormat(numberExpression).format(cell.getNumericCellValue());
                }
                break;
            default:
                //其它类型，取空串吧
                cellValue = "";
                break;
        }
        return cellValue;
    }

    /***
     * @description: 将Excel文件解析成WorkBook对象
     * @param: [path]
     * @return: org.apache.poi.ss.usermodel.Workbook
     */
    protected Workbook getWorkBook(String path) {
        Workbook workbook = null;
        // 获得工作簿
        try {
            workbook = WorkbookFactory.create(new File(path));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * 上传文件检查
     * 是否是合法的文件
     *
     * @return
     */
    protected boolean isLegalDocument() {
        return true;
    }

    /**
     * 如何具体处理每一行的数据
     *
     * @param sheet
     */
    protected void dealRows(Sheet sheet) {
        //遍历每一行
        Iterator<Row> iterator = sheet.iterator();
        iterator.next();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();
            dealCells(row, physicalNumberOfCells);
        }
    }

    /**
     * @param row
     */
    public void dealCells(Row row, int physicalNumberOfCells) {
    }

}
