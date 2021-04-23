package com.king.function.excel.Excel.BaseProcess;

import com.king.function.excel.Exception.IllegalFileException;
import com.king.function.excel.Utils.ObjectUtils;
import com.king.function.excel.Utils.PathUtil;
import com.king.function.excel.Utils.SpringUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class BaseSaveFile {
    public final static String xls = "xls";
    public final static String xlsx = "xlsx";
    protected PathUtil pathUtil = SpringUtil.getBean(PathUtil.class);

    public String saveUploadFile(MultipartFile file) throws Exception {
        return saveUploadFile(file, null, null);
    }

    /***
     * @description: 将上传的文件存储到指定位置
     * @param: [file, saveRootPath, fileName]
     * @return: String 文件存储路径
     */
    public String saveUploadFile(MultipartFile file, String saveRootPath, String fileName) throws Exception {
        isLegalDocument(file);
        if (ObjectUtils.isNullStringObj(fileName)) {
            fileName = file.getOriginalFilename();
        }
        if (ObjectUtils.isNullStringObj(saveRootPath)) {
            saveRootPath = pathUtil.getBase_save_path();
        }
        String savePath = saveRootPath + "\\" + fileName;
        try {
            File dest = new File(savePath);
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        afterSaveUploadFile(file);
        return savePath;
    }

    protected void afterSaveUploadFile(MultipartFile file) throws Exception {
    }

    /**
     * 上传文件检查
     * 是否是合法的文件
     *
     * @return
     */
    protected void isLegalDocument(MultipartFile file) throws IllegalFileException {
        //判断文件是否存在
        if (null == file) {
            System.out.println("文件不存在！");
            throw new IllegalFileException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            System.out.println(fileName + "不是excel文件");
            throw new IllegalFileException(fileName + "不是excel文件");
        }
    }

}
