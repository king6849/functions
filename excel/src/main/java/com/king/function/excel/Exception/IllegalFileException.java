package com.king.function.excel.Exception;

public class IllegalFileException extends Exception {

    public IllegalFileException() {
    }

    //带有详细信息的构造器，信息存储在message中
    public IllegalFileException(String message) {
        super(message);
    }
}
