package com.company.watches.utils;

public class WatchesException extends RuntimeException {
    public WatchesException(Exception e) {
        super(e);
    }

    public WatchesException(String message) {
        super(message);
    }
}
