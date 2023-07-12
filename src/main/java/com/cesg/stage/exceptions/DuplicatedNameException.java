package com.cesg.stage.exceptions;

public class DuplicatedNameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicatedNameException(String msg) {
        super(msg);
    }

}