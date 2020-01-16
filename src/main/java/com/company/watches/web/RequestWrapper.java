package com.company.watches.web;

import java.util.Map;

public class RequestWrapper {
    public final RequestMethod method;
    public final String path;
    public final Map<String, String[]> parameters;
    public final String payload;

    public RequestWrapper(RequestMethod method, String path, Map<String, String[]> parameters, String payload) {
        this.method = method;
        this.path = path;
        this.parameters = parameters;
        this.payload = payload;
    }

    public enum RequestMethod {
        GET, POST, PUT, DELETE
    }

}
