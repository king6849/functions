package com.king.function.excel.Exception;

public class CommonException extends Exception {

    public CommonException() {
    }

    //带有详细信息的构造器，信息存储在message中
    public CommonException(String message) {
        super(message);
    }
}
