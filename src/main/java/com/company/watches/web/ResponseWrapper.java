package com.company.watches.web;

import static javax.servlet.http.HttpServletResponse.*;

public class ResponseWrapper {
    public final int statusCode;
    public final ContentType contentType;
    public final String data;

    public ResponseWrapper(String data) {
        this(SC_OK, data);
    }

    public ResponseWrapper(int statusCode, String data) {
        this.statusCode = statusCode;
        this.contentType = ContentType.JSON;
        this.data = data;
    }

    public static ResponseWrapper OK(String... message) {
        return new ResponseWrapper(SC_OK, message.length == 0 ? "" : String.join(", ", message));
    }

    public static ResponseWrapper NotFound(String... message) {
        return new ResponseWrapper(SC_NOT_FOUND, String.format("{\"message\": \"%s\"}",
                message.length == 0 ? "not found" : String.join(", ", message)));
    }

    public static ResponseWrapper BadRequest(String... message) {
        return new ResponseWrapper(SC_BAD_REQUEST, String.format("{\"message\": \"%s\"}",
                message.length == 0 ? "bad request" : String.join(", ", message)));
    }

    public enum ContentType {
        JSON("application/json");

        public final String typeName;

        ContentType(String typeName) {
            this.typeName = typeName;
        }
    }
}
