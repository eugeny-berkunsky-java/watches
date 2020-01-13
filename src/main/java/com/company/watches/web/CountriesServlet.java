package com.company.watches.web;


import com.alibaba.fastjson.JSON;
import com.company.watches.manage.CountriesManager;
import com.company.watches.manage.ManagersContainer;
import com.company.watches.model.Country;
import com.company.watches.model.DAOContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class CountriesServlet extends HttpServlet {

    private CountriesManager countriesManager;

    @Override
    public void init() throws ServletException {
        final ManagersContainer container = ManagersContainer.getInstance(DAOContainer.getInstance());
        countriesManager = container.getCountriesManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();

        // get all
        if (pathInfo == null || pathInfo.length() == 1) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            JSON.writeJSONString(response.getWriter(), countriesManager.getAll());
            return;
        }

        // get by id
        if (pathInfo.matches("^/\\d+$")) {
            final Optional<Country> country = countriesManager.getById(Integer.parseInt(pathInfo.substring(1)));
            if (country.isPresent()) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
                JSON.writeJSONString(response.getWriter(), country.get());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
                response.getWriter().format("{message: \"%s\"}", "country id not found");
            }
            return;
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.getWriter().format("{message: \"%s\"}", "bad request");

    }
}