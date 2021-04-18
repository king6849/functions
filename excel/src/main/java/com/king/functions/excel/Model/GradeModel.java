package com.king.functions.excel.Model;

import lombok.Data;

@Data
public class GradeModel {
    private int id;

    private String studentName;

    private String studentNo;
    //    院系

    private String faculty;
    //    专业

    private String profession;
    //    班级名称

    private String className;
    //   年级

    private String grade;
    //    学期

    private String term;
    //    考试类型

    private String typeOfExam;
    //    课程代码

    private String courseCode;
    //    课程名称

    private String courseTitle;
    //    成绩审核状态

    private String resultsReviewStatus;
    //    平时成绩

    private double usualGrades;
    //    期中成绩

    private double midTermGrades;
    //    期末成绩

    private double finalExam;
    //    成绩

    private double score;
    //    实作成绩

    private double implementationResults;
    //    积分法成绩

    private double pointsMethodScore;
    //    成绩标志

    private String achievementMark;


}
