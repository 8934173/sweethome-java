package com.sweethome.utils;

public class BaseRuntimeException extends RuntimeException{

    public static RuntimeException getException(Exception e) {
        return new RuntimeException(e);
    }

    public static RuntimeException getException(String e) {
        throw new RuntimeException(e);
    }
}
