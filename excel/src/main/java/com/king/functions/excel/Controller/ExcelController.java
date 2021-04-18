package com.king.functions.excel.Controller;

import com.alibaba.fastjson.JSONObject;
import com.king.functions.excel.Service.ExcelServiceImpl;
import com.king.functions.excel.Utils.JSONObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
public class ExcelController {

    @Autowired
    private ExcelServiceImpl excelService;

    @Autowired
    private JSONObjectUtil jsonObjectUtil;


    @RequestMapping(path = "/saveExcel", method = RequestMethod.POST)
    public String saveExcel(@RequestBody MultipartFile excel) {
        excelService.saveExcel(excel);
        return "已完成上传文件,正在解析";
    }

    @RequestMapping(path = "/exportSimpleDate", method = RequestMethod.POST)
    public String saveExcel(@RequestBody HashMap<String, Object> params) {
        JSONObject jsonObject = jsonObjectUtil.parseStringToJSONObject(params);
        String title = jsonObject.getString("title");
        String[] row = jsonObjectUtil.JSONArrayToStrings(jsonObject.getJSONArray("rowHeader"));
        String savePath = jsonObject.getString("savePath");
        LinkedHashMap<String, Object> out = jsonObjectUtil.jsonToLinkedHashMap(jsonObject.getJSONObject("out"));
        excelService.exportSimpleExcelDate(title, row, savePath, out);
        return "已完成上传文件,正在解析";
    }


    @RequestMapping(path = "/exportExcel", method = RequestMethod.POST)
    public String parseExcel(@RequestBody HashMap<String, Object> params) {
        JSONObject jsonObject = jsonObjectUtil.parseStringToJSONObject(params);
        String[] rowName = jsonObjectUtil.JSONArrayToStrings(jsonObject.getJSONArray("rowHeader"));
        LinkedHashMap<String, Object> out = jsonObjectUtil.jsonToLinkedHashMap(jsonObject.getJSONObject("out"));
        LinkedHashMap<String, Object> in = jsonObjectUtil.jsonToLinkedHashMap(jsonObject.getJSONObject("in"));
        excelService.dynamicExportExcel(rowName, out, in);
        return "正在生成文件";
    }


}
