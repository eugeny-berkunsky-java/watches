package com.company.watches.web;


import com.alibaba.fastjson.JSON;
import com.company.watches.manage.CountriesManager;
import com.company.watches.manage.ManagersContainer;
import com.company.watches.model.Country;

import java.util.List;
import java.util.Optional;

import static com.company.watches.web.RequestWrapper.RequestMethod.*;

public class CountryController implements Controller {

    private CountriesManager manager;

    public CountryController(ManagersContainer container) {
        manager = container.getCountriesManager();
    }

    @Override
    public ResponseWrapper process(RequestWrapper rw) {
        if (rw.method == DELETE && rw.path.matches("country/\\d+$")) {
            return delete(rw);
        } else if (rw.method == PUT && rw.path.matches("country/\\d+$")) {
            return update(rw);
        } else if (rw.method == POST && rw.path.equals("country/")) {
            return create(rw);
        } else if (rw.method == GET && rw.path.equals("country/")) {
            return getAll();
        } else if (rw.method == GET && rw.path.matches("country/\\d+$")) {
            return getById(rw);
        } else {
            return ResponseWrapper.BadRequest();
        }
    }

    private ResponseWrapper getAll() {
        final List<Country> countries = manager.getAll();

        return new ResponseWrapper(JSON.toJSONString(countries));
    }

    private ResponseWrapper getById(RequestWrapper request) {
        try {
            int id = Integer.parseInt(request.path.split("/")[1]);

            return manager.getById(id)
                    .map(c -> new ResponseWrapper(JSON.toJSONString(c)))
                    .orElse(ResponseWrapper.NotFound("country not found"));

        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }

    private ResponseWrapper create(RequestWrapper request) {
        return Optional.ofNullable(JSON.parseObject(request.payload, Country.class))
                .flatMap(c -> manager.addCountry(c.getName()))
                .map(c -> new ResponseWrapper(JSON.toJSONString(c)))
                .orElse(ResponseWrapper.BadRequest("error creating country"));
    }

    private ResponseWrapper update(RequestWrapper request) {
        try {
            int id = Integer.parseInt(request.path.split("/")[1]);

            return Optional.ofNullable(JSON.parseObject(request.payload, Country.class))
                    .flatMap(c -> manager.updateCountry(id, c.getName()) ?
                            manager.getById(id) : Optional.empty())
                    .map(c -> new ResponseWrapper(JSON.toJSONString(c)))
                    .orElse(ResponseWrapper.BadRequest());

        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }

    private ResponseWrapper delete(RequestWrapper request) {
        try {
            int id = Integer.parseInt(request.path.split("/")[1]);
            return manager.deleteCountry(id)
                    ? ResponseWrapper.OK()
                    : ResponseWrapper.NotFound();

        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }
}