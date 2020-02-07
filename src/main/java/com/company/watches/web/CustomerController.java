package com.company.watches.web;

import com.company.watches.manage.CustomerManager;
import com.company.watches.manage.ManagersContainer;
import com.company.watches.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.company.watches.web.RequestWrapper.RequestMethod.*;

public class CustomerController implements Controller {
    private static final Logger logger = Logger.getLogger(CustomerController.class.getName());

    private final CustomerManager manager;

    private final ObjectMapper objectMapper;

    public CustomerController(ManagersContainer container) {
        manager = container.getCustomerManager();
        objectMapper = new ObjectMapper();
    }

    @Override
    public ResponseWrapper process(RequestWrapper rw) {
        if (rw.method == GET && rw.path.equals("customer/")) {
            return getAll();
        } else if (rw.method == GET && rw.path.matches("customer/\\d+$")) {
            return getById(rw);
        } else if (rw.method == POST && rw.path.equals("customer/")) {
            return create(rw);
        } else if (rw.method == PUT && rw.path.matches("customer/\\d+$")) {
            return update(rw);
        } else if (rw.method == DELETE && rw.path.matches("customer/\\d+$")) {
            return delete(rw);
        } else {
            return ResponseWrapper.BadRequest();
        }
    }

    private ResponseWrapper getAll() {
        return new ResponseWrapper(toJSONString(manager.getAll()));
    }

    private ResponseWrapper getById(RequestWrapper rw) {
        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);
            return manager.getById(id)
                    .map(c -> new ResponseWrapper(toJSONString(c)))
                    .orElse(ResponseWrapper.NotFound("customer not found"));
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "bad id", e);
        }

        return ResponseWrapper.BadRequest();
    }

    private ResponseWrapper create(RequestWrapper rw) {

        return Optional.ofNullable(toObject(rw.payload))
                .flatMap(c -> manager.addCustomer(c.getName()))
                .map(c -> new ResponseWrapper(toJSONString(c)))
                .orElse(ResponseWrapper.BadRequest("error creating customer"));
    }

    private ResponseWrapper update(RequestWrapper rw) {
        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);

            return Optional.ofNullable(toObject(rw.payload))
                    .flatMap(c -> manager.updateCustomer(id, c.getName(), c.getDiscountCard().getId())
                            ? manager.getById(id)
                            : Optional.empty())
                    .map(c -> new ResponseWrapper(toJSONString(c)))
                    .orElse(ResponseWrapper.BadRequest("error updating customer"));
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "bad id", e);
        }

        return ResponseWrapper.BadRequest();
    }

    private ResponseWrapper delete(RequestWrapper rw) {

        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);

            return manager.deleteCustomer(id)
                    ? ResponseWrapper.OK()
                    : ResponseWrapper.NotFound();
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "bad id", e);
        }

        return ResponseWrapper.BadRequest();
    }

    private Customer toObject(String data) {
        try {
            final Customer customer = objectMapper.readValue(data, Customer.class);
            return customer;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "deserialize error", e);
            return null;
        }
    }

    private String toJSONString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "serialize error", e);
            return "";
        }
    }

}
