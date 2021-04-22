package com.king.function.excel.Exception;

public class SqlFiledException extends Exception {

    public SqlFiledException() {
    }

    //带有详细信息的构造器，信息存储在message中
    public SqlFiledException(String message) {
        super(message);
    }
}
