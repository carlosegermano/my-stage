package com.cesg.stage.exceptions;

public class DuplicatedUserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicatedUserException(String msg) {
        super(msg);
    }

}