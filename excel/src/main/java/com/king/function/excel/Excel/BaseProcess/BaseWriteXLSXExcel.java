package com.king.function.excel.Excel.BaseProcess;

import com.king.function.excel.Utils.CreateFileUtil;
import com.king.function.excel.Utils.ExcelUtil;
import com.king.function.excel.Utils.ObjectUtils;
import com.king.function.excel.Utils.PathUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseWriteXLSXExcel {

    // 显示的导出表的标题
    private String title;
    // 导出表的列名
    private List<String> rowName;
    private List<Object[]> dataList = new ArrayList<>();
    private final ExcelUtil excelUtil = new ExcelUtil();

    public BaseWriteXLSXExcel() {
    }

    // 构造函数，传入要导出的数据
    public BaseWriteXLSXExcel(String title, List<String> rowName, List<Object[]> dataList) {
        this.dataList = dataList;
        this.rowName = rowName;
        this.title = title;
    }

    // 导出数据
    private void export(OutputStream out, boolean header) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            //工作页名
            if (title == null) {
                title = "工作表一";
            }
            XSSFSheet sheet = workbook.createSheet(title);
            //sheet样式定义 列头样式
            XSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);
            //单元格样式
            XSSFCellStyle style = this.getStyle(workbook);
            int columnIndex = 0;
            if (header) {
                //设置表头
                columnIndex = 2;
                putExcelHeader(sheet, columnTopStyle);
            }
            // 定义所需列数
            int columnNum = rowName.size();
            // 将列头设置到sheet的单元格中
            putColumnHeader(sheet, columnTopStyle, columnIndex, columnNum);
            // 将查询到的数据设置到sheet对应的单元格中
            putColumnData(sheet, style, columnIndex);
            // 让列宽随着导出的列长自动适应
            columnAutomaticWidth(sheet, columnNum);
            //写出工作Excel表
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * @description: 设置Excel表头
     */
    protected void putExcelHeader(XSSFSheet sheet, XSSFCellStyle columnTopStyle) {
        //合并几个单元格形成表抬头
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.size() - 1)));
        // 产生表格标题行
        XSSFRow header = sheet.createRow(0);
        XSSFCell cellTitle = header.createCell(0);
        cellTitle.setCellStyle(columnTopStyle);
        cellTitle.setCellValue(title);
    }

    /***
     * @description: 设置列头
     */
    protected void putColumnHeader(XSSFSheet sheet, XSSFCellStyle columnTopStyle, int columnNameIndex, int columnNum) {
        XSSFRow rowRowName = sheet.createRow(columnNameIndex);
        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            XSSFCell cellRowName = rowRowName.createCell(n);
            cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);
            XSSFRichTextString text = new XSSFRichTextString(rowName.get(n));
            cellRowName.setCellValue(text);
            cellRowName.setCellStyle(columnTopStyle);
        }
    }

    /***
     * @description: 填充数据
     */
    protected void putColumnData(XSSFSheet sheet, XSSFCellStyle style, int startIndex) {
        // 将查询到的数据设置到sheet对应的单元格中
        int dataSize = dataList.size();
        for (int i = 0; i < dataSize; i++) {
            // 遍历每列
            Object[] columnValues = dataList.get(i);
            // 创建所需的行数
            XSSFRow row = sheet.createRow(i + startIndex + 1);
            int colSize = columnValues.length;
            for (int j = 0; j < colSize; j++) {
                XSSFCell cell = row.createCell(j, XSSFCell.CELL_TYPE_STRING);
                if (!"".equals(columnValues[j]) && columnValues[j] != null) {
                    cell.setCellValue(columnValues[j].toString());
                }
                cell.setCellStyle(style);
            }
        }
    }

    /***
     * @description: 列宽自动宽度
     */
    protected void columnAutomaticWidth(XSSFSheet sheet, int columnNum) {
        //遍历每一列
        for (int colNum = 0; colNum < columnNum; colNum++) {
            //默认宽度
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            //数据行数
            int lastRowNum = sheet.getLastRowNum();
            //遍历该列的每一行，取宽度最大值
            for (int rowNum = 0; rowNum < lastRowNum; rowNum++) {
                XSSFRow currentRow = null;
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    XSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }
    }

    /**
     * 列头单元格样式
     *
     * @param workbook 工作簿
     * @return 样式表
     */
    protected XSSFCellStyle getColumnTopStyle(XSSFWorkbook workbook) {
        // 设置字体
        XSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置低边框
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置低边框颜色
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式中应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐；
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;

    }

    /**
     * 单元格样式
     *
     * @param workbook 工作簿
     * @return 样式表
     */
    protected XSSFCellStyle getStyle(XSSFWorkbook workbook) {
        // 设置字体
        XSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 字体加粗
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getRowName() {
        return rowName;
    }

    public void setRowName(List<String> rowName) {
        this.rowName = rowName;
    }

    public List<Object[]> getDataList() {
        return dataList;
    }

    public void setDataList(List<Object[]> dataList) {
        this.dataList = dataList;
    }

    public ExcelUtil getExcelUtil() {
        return excelUtil;
    }

    /**
     * 导出Excel
     *
     * @param filePath     存储路径
     * @param outputStream 输出流
     */
    public void exportExcelFile(String filePath, OutputStream outputStream, boolean header) throws Exception {
        if (ObjectUtils.isNullStringObj(filePath)) {
            filePath = PathUtil.DEFAULT_EXPORT_BASE_PATH + "\\" + excelUtil.randomFileName() + "." + BaseSaveFile.xlsx;
        }
        if (outputStream == null) {
            export(new FileOutputStream(CreateFileUtil.createFile(filePath)), header);
        } else {
            export(outputStream, header);
        }
    }

    /**
     * @param filePath 存储路径
     * @param header   是否需要写入Excel标题，抬头
     */
    public void exportExcelFile(String filePath, boolean header) throws Exception {
        exportExcelFile(filePath, null, header);
    }

}