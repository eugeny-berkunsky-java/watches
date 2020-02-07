package com.company.watches.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONUtils {
    private static final Logger logger = Logger.getLogger(JSONUtils.class.getName());
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T toObject(String data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "deserialize error", e);
            return null;
        }
    }

    public static String toJSONString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "serialize error", e);
            return "";
        }
    }
}
