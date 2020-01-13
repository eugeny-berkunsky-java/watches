package com.company.watches.utils;

import java.io.BufferedReader;

public class UserInput {

    private final BufferedReader reader;

    public UserInput(BufferedReader reader) {
        if (reader == null) {
            throw new WatchesException("reader is null");
        }
        this.reader = reader;
    }

    public int getNumber(String message, int defaultNumber) {
        if (message == null) {
            return defaultNumber;
        }

        try {
            System.out.format("%s: ", message);
            return Integer.parseInt(reader.readLine().trim());
        } catch (Exception e) {
            return defaultNumber;
        }
    }

    public String getString(String message) {
        if (message == null) {
            return "";
        }

        System.out.format("%s: ", message);
        try {
            return reader.readLine().trim();
        } catch (Exception e) {
            return "";
        }
    }

}
