package com.company.watches.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

public interface WebUtils {

    static String getPayload(HttpServletRequest request) {
        if (request == null) {
            return "";
        }

        try {
            final BufferedReader reader = request.getReader();
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return "";
        }
    }
}
