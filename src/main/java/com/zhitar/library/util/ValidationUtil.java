package com.zhitar.library.util;

public class ValidationUtil {

    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while ((cause = result.getCause()) != null && (result != cause)) {
            result = cause;
        }
        return cause;
    }
}
