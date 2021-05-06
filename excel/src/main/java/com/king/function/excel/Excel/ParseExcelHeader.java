package com.king.function.excel.Excel;

import com.king.function.excel.Excel.BaseProcess.BaseReadExcel;
import com.king.function.excel.Exception.SqlFiledException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Iterator;


/**
 * 解析excel文件表头
 *
 * @author king
 */
@Component
public class ParseExcelHeader extends BaseReadExcel {


    @Override
    protected void dealRows(Sheet sheet) throws SqlFiledException {
        //遍历每一行
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            System.out.println(row.toString());
        }
    }

}
