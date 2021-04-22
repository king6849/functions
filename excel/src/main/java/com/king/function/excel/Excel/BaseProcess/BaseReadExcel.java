package com.king.function.excel.Excel.BaseProcess;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.king.function.excel.Exception.SqlFiledException;
import com.king.function.excel.Utils.DateTransferUtil;
import com.king.function.excel.Utils.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Objects;

public abstract class BaseReadExcel extends BaseSaveFile {

    protected DateTransferUtil dateTransferUtil = new DateTransferUtil();

    /***
     * @description: 循环处理Excel表格
     */
    public void dealExcel(MultipartFile file, String filePath) throws SqlFiledException {
        Workbook workbook = getWorkBook(file, filePath);
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            //根据第一行是否为空判断为空表
            Row row = sheet.getRow(0);
            if (row == null) {
                continue;
            }
            dealRows(sheet);
        }
    }

    /**
     * 获取 double类型值
     */
    public double getCellDoubleValue(Cell cell) {
        return Double.parseDouble(ObjectUtils.stringToDouble(getCellValue(cell).toString()));
    }

    public Object getCellValue(Cell cell) {
        return getCellValue(cell, null, null);
    }

    /**
     * 获取每个单元格的数据
     *
     * @param cell             单元格对象
     * @param numberExpression 数字表达式
     * @param dateExpression   日期格式
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

    /**
     * 获取工作簿
     */
    protected Workbook getWorkBook(MultipartFile file, String filePath) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = new FileInputStream(new File(Objects.requireNonNull(filePath)));
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (Objects.requireNonNull(fileName).endsWith(xls)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }


    /**
     * 如何具体处理每一行的数据
     */
    protected void dealRows(Sheet sheet) throws SqlFiledException {
        //遍历每一行
        Iterator<Row> iterator = sheet.iterator();
        iterator.next();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();
            dealCellsBefore(row, physicalNumberOfCells);
            dealCells(row, physicalNumberOfCells);
        }
    }

    protected void dealCellsBefore(Row row, int physicalNumberOfCells) throws SqlFiledException {
    }

    /**
     * 处理每一个单元格
     *
     * @param row 行对象
     */
    protected void dealCells(Row row, int physicalNumberOfCells) {
    }


}
