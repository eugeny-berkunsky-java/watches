package com.company.watches.web;


import com.alibaba.fastjson.JSON;
import com.company.watches.manage.CountriesManager;
import com.company.watches.manage.ManagersContainer;
import com.company.watches.model.Country;
import com.company.watches.model.DAOContainer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.*;

public class CountriesServlet extends HttpServlet {

    private CountriesManager countriesManager;

    @Override
    public void init() {
        final ManagersContainer container = ManagersContainer.getInstance(DAOContainer.getInstance());
        countriesManager = container.getCountriesManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        final String pathInfo = request.getPathInfo();

        // get all
        if (pathInfo == null || pathInfo.length() == 1) {
            sendJSON(response, ResponseWrapper.getOkRW(JSON.toJSONString(countriesManager.getAll())));
            return;
        }

        // get by id
        if (pathInfo.matches("^/\\d+$")) {

            final int countryId = Integer.parseInt(pathInfo.substring(1));

            sendJSON(response, countriesManager.getById(countryId)
                    .map(c -> ResponseWrapper.getOkRW(JSON.toJSONString(c)))
                    .orElse(ResponseWrapper.getNotFoundRW("country not found")));
        } else {
            sendJSON(response, ResponseWrapper.getBR());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String payload = getRequestPayload(request);

        final Optional<Country> country = Optional.ofNullable(JSON.parseObject(payload, Country.class));

        sendJSON(response, country.flatMap(c -> countriesManager.addCountry(c.getName()))
                .map(rc -> new ResponseWrapper(SC_CREATED, JSON.toJSONString(rc)))
                .orElse(ResponseWrapper.getBR()));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String pathInfo = request.getPathInfo();

        if (pathInfo == null || !pathInfo.matches("^/\\d+$")) {
            sendJSON(response, ResponseWrapper.getBR());
            return;
        }

        final String payload = getRequestPayload(request);

        int countryId = Integer.parseInt(pathInfo.substring(1));

        sendJSON(response, Optional.ofNullable(JSON.parseObject(payload, Country.class))
                .flatMap(c -> countriesManager.updateCountry(countryId, c.getName())
                        ? countriesManager.getById(countryId)
                        : Optional.empty())
                .map(cr -> ResponseWrapper.getOkRW(JSON.toJSONString(cr)))
                .orElse(ResponseWrapper.getBR()));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {

        final String pathInfo = request.getPathInfo();

        if (pathInfo == null || !pathInfo.matches("^/\\d+$")) {
            sendJSON(response, ResponseWrapper.getBR());
            return;
        }

        int countryId = Integer.parseInt(pathInfo.substring(1));

        sendJSON(response, countriesManager.deleteCountry(countryId) ?
                new ResponseWrapper(SC_NO_CONTENT, "") : ResponseWrapper.getBR());
    }


    private void sendJSON(HttpServletResponse response, ResponseWrapper wrapper) {
        response.setStatus(wrapper.statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().write(wrapper.jsonString);
        } catch (IOException e) {
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private String getRequestPayload(HttpServletRequest request) throws IOException {
        final String header = request.getHeader("Content-Type");
        if (header == null || !header.equalsIgnoreCase("application/json")) {
            return "";
        }

        final StringBuilder buffer = new StringBuilder();
        final BufferedReader reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        return buffer.toString();
    }

    private static class ResponseWrapper {
        private final int statusCode;
        private final String jsonString;

        public ResponseWrapper(int statusCode, String jsonString) {
            this.statusCode = statusCode;
            this.jsonString = jsonString;
        }

        public static ResponseWrapper getOkRW(String json) {
            return new ResponseWrapper(SC_OK, json);
        }

        public static ResponseWrapper getNotFoundRW(String... message) {
            return new ResponseWrapper(SC_NOT_FOUND, String.format("{\"message\": \"%s\"}",
                    message.length == 0 ? "not found" : String.join(", ", message)));
        }

        public static ResponseWrapper getBR(String... message) {
            return new ResponseWrapper(SC_BAD_REQUEST, String.format("{\"message\": \"%s\"}",
                    message.length == 0 ? "bad request" : String.join(", ", message)));
        }
    }
}