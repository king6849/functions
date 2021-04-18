package com.king.functions.excel.Excel;

import com.king.functions.excel.Dao.SheetTable2Mapper;
import com.king.functions.excel.Utils.ObjectUtils;
import com.king.functions.excel.Utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;


public class DynamicExportExcelData extends BaseProcessExcelFiles {

    @Autowired
    private SheetTable2Mapper sheetTable2Mapper;

    private final BaseExportExcel baseExportExcel = new BaseExportExcel();

    //    @Async("asyncExcelExecutor")
//    public void exportExcel() {
//        String savePath = PathUtil.TEST_DEFAULT_EXPORT_BASE_PATH;
//        String[] rowName = {"学号", "姓名", "成绩标志", "平时成绩", "期末成绩"};
//        List<HashMap<String, String>> classNameList = sheetTable2Mapper.selectClassName();
//        for (HashMap<String, String> className : classNameList) {
//            List<Object[]> dataList = new ArrayList<>();
//            String profession = className.get("profession");
//            String class_name = className.get("class_name");
//            String course_title = className.get("course_title");
//            List<HashMap<String, Object>> hashMaps = sheetTable2Mapper.selectGrade(profession, class_name, course_title);
//            for (HashMap<String, Object> hashMap : hashMaps) {
//                Object[] data = new Object[5];
//                data[0] = hashMap.get("student_no");
//                data[1] = hashMap.get("student_name");
//                data[2] = hashMap.get("achievement_mark");
//                data[3] = hashMap.get("usual_grades");
//                data[4] = hashMap.get("final_exam");
//                dataList.add(data);
//                System.out.println(hashMap);
//            }
//            baseExportExcel.setTitle(course_title);
//            baseExportExcel.setRowName(rowName);
//            baseExportExcel.setDataList(dataList);
//            try {
//                baseExportExcel.exportExcelFile(savePath + "\\" + profession + "\\" + class_name + "\\" + course_title + PathUtil.EXCELlSuffix, false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }3


    /**
     * 导出简单结构数据
     *
     * @param sheetName
     * @param rowName
     * @param savePath
     * @param outerSQLConditions
     */
    public void exportSimpleExcelDate(String sheetName, String[] rowName, String savePath, LinkedHashMap<String, Object> outerSQLConditions) {
        List<LinkedHashMap<String, Object>> datalist = outSqlResult(outerSQLConditions);
        List<Object[]> rowData = new ArrayList<>();
        for (LinkedHashMap<String, Object> record : datalist) {
            int i = 0;
            Object[] rowValue = new Object[rowName.length];
            for (String next : record.keySet()) {
                rowValue[i] = record.get(next);
                ++i;
            }
            rowData.add(rowValue);
        }
        exportExcel(sheetName, rowName, rowData, savePath);
    }


    @Async("asyncExcelExecutor")
    public void dynamicExportExcel(String[] rowName, LinkedHashMap<String, Object> outerSQLConditions, LinkedHashMap<String, Object> innerSqlCondition) {
        String savePath = PathUtil.TEST_DEFAULT_EXPORT_BASE_PATH;
        int rowNameLength = rowName.length;
        //外层sql条件
        List<LinkedHashMap<String, Object>> classNameList = outSqlResult(outerSQLConditions);
        //内存sql查询条件  key是sql字段，value是条件值
        LinkedHashMap<String, Object> condition = new LinkedHashMap<>();
        //循环遍历SQL查询条件，再来查询实际想要的数据
        for (LinkedHashMap<String, Object> outResultItem : classNameList) {
            //正式开始内存sql查询，得到查询结果resultMaps
            condition.put("sql", innerSqlCondition.get("sql"));
            //拿到每张表的数据
            List<Object[]> dataList = inSqlResult(outResultItem, condition, rowNameLength);
            //导出到Excel
            exportExcel(null, rowName, dataList, condition, savePath);
            //清空内存SQL查询条件
            condition.clear();
        }
    }

    /**
     * 外层查询结果
     *
     * @param outerSQLConditions
     * @return
     */
    public List<LinkedHashMap<String, Object>> outSqlResult(LinkedHashMap<String, Object> outerSQLConditions) {
        //外层sql条件
        List<LinkedHashMap<String, Object>> datalist = sheetTable2Mapper.outerSQLCondition(outerSQLConditions);
        return datalist;
    }

    protected Map<String, String> sortHashMap(Map<String, String> map) {
        Map<String, String> sortedMap = new LinkedHashMap<>();
        List<String> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        for (String key : list) {
            sortedMap.put(key, map.get(key));
        }
        return sortedMap;
    }

    /**
     * 内存查询结果
     *
     * @param outResultItem
     * @param condition
     * @param rowNameLength
     * @return
     */
    public List<Object[]> inSqlResult(LinkedHashMap<String, Object> outResultItem, LinkedHashMap<String, Object> condition, int rowNameLength) {
        //构建内层sql查询条件
        for (String filedName : outResultItem.keySet()) {
            condition.put(filedName, outResultItem.get(filedName));
        }
        List<Object[]> dataList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> resultMaps = sheetTable2Mapper.selectMapResults(condition);
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
            System.out.println(record);
        }
        return dataList;
    }

    /**
     * 导出Excel表
     *
     * @param title
     * @param rowName
     * @param dataList
     * @param condition
     * @param baseSavePath
     */
    public void exportExcel(String title, String[] rowName, List<Object[]> dataList, LinkedHashMap<String, Object> condition, String baseSavePath) {
        //excel 抬头（标题）
        baseExportExcel.setTitle(title);
        //excel列名
        baseExportExcel.setRowName(rowName);
        //每一行的数据
        baseExportExcel.setDataList(dataList);
        try {
            StringBuilder dirPath = new StringBuilder();
            dirPath.append(baseSavePath);
            if (condition != null) {
                if (condition.size() > 1) {
                    //导出到Excel表
                    for (String dir : condition.keySet()) {
                        if (dir.equals("sql")) {
                            continue;
                        }
                        dirPath.append("\\").append(dir);
                    }
                }
            } else {
                dirPath.append("\\").append(title);
            }
            dirPath.append(PathUtil.EXCELlSuffix);
            baseExportExcel.exportExcelFile(dirPath.toString(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportExcel(String sheetName, String[] rowName, List<Object[]> dataList, String savePath) {
        exportExcel(sheetName, rowName, dataList, null, savePath);
    }


}
