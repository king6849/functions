package com.king.functions.excel.Utils;

import java.io.File;
import java.io.IOException;

public class CreateFileUtil {

    /**
     * 创建文件，可覆盖
     *
     * @param destFileName 文件路径和文件名称（/com/filename.txt）
     * @return true:创建成功 false:创建失败
     */
    public static File createFile(String destFileName) {
        File file = new File(destFileName);
//        if (file.exists()) {
//            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
//            return null;
//        }
        if (destFileName.endsWith(File.separator)) {
            System.out.println("创建单个文件" + destFileName + "失败，目标不能是目录！");
        }
        if (!file.getParentFile().exists()) {
            System.out.println("目标文件所在路径不存在，准备创建。。。");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目录文件所在的目录失败！");
            }
        }
        // 创建目标文件
        try {
            if (file.createNewFile()) {
                System.out.println("创建单个文件" + destFileName + "成功！");
            } else {
                System.out.println("创建单个文件" + destFileName + "失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\96585\\Desktop\\exprotTest\\0101-2020级学前教育\\2020级学前教育1班\\20-基本乐理（一）.xlsx";
        CreateFileUtil.createFile(path);
    }

    /**
     * 创建文件夹（目录），存在不可覆盖
     *
     * @param destDirName 创建路径（D:/com/filePath）
     * @return
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        // 创建单个目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "成功！");
            return false;
        }
    }

    /**
     * 创建临时文件
     *
     * @param prefix  临时文件的拼凑名字前部分
     * @param suffix  文件后缀名
     * @param dirName 所在的目录
     * @return
     */
    public static String createTempFile(String prefix, String suffix, String dirName) {

        File tempFile = null;
        try {
            if (dirName == null) {
                // 在默认文件夹下创建临时文件
                tempFile = File.createTempFile(prefix, suffix);
                return tempFile.getCanonicalPath();
            } else {
                File dir = new File(dirName);
                //如果临时文件所在目录不存在，首先创建
                if (!dir.exists()) {
                    if (!CreateFileUtil.createDir(dirName)) {
                        System.out.println("创建临时文件失败，不能创建临时文件所在目录！");
                        return null;
                    }
                }
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建临时文件失败" + e.getMessage());
            return null;
        }
    }
}
