package com.king.function.excel.Controller;

import com.king.function.excel.Exception.SqlFiledException;
import com.king.function.excel.Service.ExcelServiceImpl;
import com.king.function.excel.Utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/excel")
public class ExcelController {

    @Autowired
    private ExcelServiceImpl excelService;


    @RequestMapping(path = "/saveExcel", method = RequestMethod.POST)
    public String saveExcel(@RequestBody MultipartFile excel, @RequestParam String table) {
        try {
            excelService.saveExcel(excel, table);
        } catch (SqlFiledException e) {
            return "字段数量不一致";
        }
        return "已完成上传文件,正在解析";
    }

    //    导出单个Excel表格
    @RequestMapping(path = "/exportSimpleDate", method = RequestMethod.POST)
    public String exportSimpleDate(@RequestBody HashMap<String, Object> params) {
        String title = ObjectUtils.isNullStringObj(params.get("title"));
        List<String> rowHeader = (List<String>) params.get("rowHeader");
        String savePath = ObjectUtils.isNullStringObj(params.get("savePath"));
        LinkedHashMap<String, Object> out = (LinkedHashMap<String, Object>) params.get("out");
        excelService.exportSimpleExcelDate(title, rowHeader, savePath, out);
        return "正在生成文件";
    }

    //导出多个Excel表格
    @RequestMapping(path = "/exportMultipleExcel", method = RequestMethod.POST)
    public String exportExcel(@RequestBody HashMap<String, Object> params) {
        String savePathRoot = ObjectUtils.isNullStringObj(params.get("savePathRoot"));
        List<String> rowName = (List<String>) params.get("rowHeader");
        LinkedHashMap<String, Object> out = (LinkedHashMap<String, Object>) params.get("out");
        LinkedHashMap<String, Object> in = (LinkedHashMap<String, Object>) params.get("in");
        excelService.dynamicExportExcel(rowName, savePathRoot, out, in);
        return "正在生成文件";
    }


    /***
     * @description: 解析表头
     */
    @RequestMapping(path = "/parseHeader", method = RequestMethod.POST)
    public String exportExcel(@RequestBody MultipartFile excel) {
        excelService.parseHeader(excel);
        return "正在解析文件";
    }


}
