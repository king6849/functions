package com.king.function.excel.Excel;

import com.king.function.excel.Dao.SheetTableMapper;
import com.king.function.excel.Excel.BaseProcess.BaseReadExcel;
import com.king.function.excel.Exception.SqlFiledException;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Set;


@Component
@DependsOn("springUtil")
public class ImportExcelData extends BaseReadExcel {

    @Autowired
    private SheetTableMapper sheetTableMapper;

    private LinkedHashMap<String, Object> sqlStructure;

    private String table;

    /**
     * 处理每一行的数据
     *
     * @param row   行
     * @param cells 列数量
     */
    @Override
    public void dealCells(Row row, int cells) {
        doSaveDate(row, cells);
    }

    /**
     * 保存Excel数据到数据库
     */
    @Transactional
    public void doSaveDate(Row row, int cells) {
        LinkedHashMap<String, Object> paramMap = putSqlFiledAndParamValue(row, cells);
        sheetTableMapper.saveExcelData(paramMap);
    }

    public LinkedHashMap<String, Object> putSqlFiledAndParamValue(Row row, int cells) {
        //列值
        Object[] cellValue = new Object[cells + 1];
        for (int i = 0; i < cells; i++) {
            cellValue[i] = getCellValue(row.getCell(i));
        }
        //数据库字段
        Set<String> filed = this.sqlStructure.keySet();
        //构建参数
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        int i = 0;
        for (String key : filed) {
            if (key.equals("id") || key.equals("table")) {
                continue;
            }
            params.put(key, cellValue[i]);
            ++i;
        }
        params.put("table", this.table);
        System.out.println(params);
        return params;
    }

    public void initTable(int cells) {
        sheetTableMapper.initTable(table, cells);
        this.sqlStructure = sheetTableMapper.sqlStructure(table);
    }

    @Override
    protected void dealCellsBefore(Row row, int physicalNumberOfCells) throws SqlFiledException {
        initTable(physicalNumberOfCells);
        int filedNum = this.sqlStructure.size() - 1;
        if (filedNum != physicalNumberOfCells) {
            throw new SqlFiledException("数据库字段数量不一致,数据库字段数量是" + this.sqlStructure.size() + " ,Excel列数是" + physicalNumberOfCells);
        }
    }

    @Override
    protected void dealExcelAfter() {
        sheetTableMapper.deleteTmpRecord(table, 1);
    }

    public void setTable(String table) {
        this.table = table;
    }
}
