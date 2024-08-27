package com.taohansen.microarquivos.services.exceptions;

public class FileAccessException extends RuntimeException {
    public FileAccessException(String msg) {
        super(msg);
    }
}