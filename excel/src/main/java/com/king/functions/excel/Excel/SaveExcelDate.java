package com.king.functions.excel.Excel;

import com.king.functions.excel.Dao.SheetTable2Mapper;
import com.king.functions.excel.Model.SheetTable2;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SaveExcelDate extends BaseProcessExcelFiles {

    @Autowired
    private SheetTable2Mapper sheetTable2Mapper;

    /**
     * 处理每一行的数据
     *
     * @param row
     * @param cells
     */
    @Override
    public void dealCells(Row row, int cells) {
        saveDate(row, cells);
    }

    /**
     * 保存Excel数据到数据库
     */
    @Transactional
    public void saveDate(Row row, int cells) {
        SheetTable2 gradeModel = new SheetTable2();
        //姓名
        gradeModel.setStudentName(getCellValue(row.getCell(0)).toString());
        //学号
        gradeModel.setStudentNo(getCellValue(row.getCell(1)).toString());
        //院系
        gradeModel.setFaculty(getCellValue(row.getCell(2)).toString());
        //专业
        gradeModel.setProfession(getCellValue(row.getCell(3)).toString());
        //班级名称
        gradeModel.setClassName(getCellValue(row.getCell(4)).toString());
        //年级
        gradeModel.setGrade(getCellValue(row.getCell(5)).toString());
        //学期
        gradeModel.setTerm(getCellValue(row.getCell(6)).toString());
        //考试类型
        gradeModel.setTypeOfExam(getCellValue(row.getCell(7)).toString());
        //课程代码
        gradeModel.setCourseCode(getCellValue(row.getCell(8)).toString());
        //课程名称
        gradeModel.setCourseTitle(getCellValue(row.getCell(9)).toString());
        //成绩审核状态
        gradeModel.setResultsReviewStatus(getCellValue(row.getCell(10)).toString());
        //平时成绩
        gradeModel.setUsualGrades(getCellDoubleValue(row.getCell(11)));
        //期中成绩
        gradeModel.setMidTermGrades(getCellDoubleValue(row.getCell(12)));
        //实作成绩
        gradeModel.setImplementationResults(getCellDoubleValue(row.getCell(13)));
        //期末成绩
        gradeModel.setFinalExam(getCellDoubleValue(row.getCell(14)));
        //成绩
        gradeModel.setScore(getCellDoubleValue(row.getCell(15)));
        //计分法成绩
        gradeModel.setPointsMethodScore(getCellValue(row.getCell(16)).toString());
        //成绩标志
        gradeModel.setAchievementMark(getCellValue(row.getCell(17)).toString());
        sheetTable2Mapper.insert(gradeModel);
    }

}
