package com.company.watches.web;

import com.company.watches.manage.CustomerManager;
import com.company.watches.manage.ManagersContainer;
import com.company.watches.model.Customer;
import com.company.watches.utils.JSONUtils;

import java.util.Optional;

import static com.company.watches.web.RequestWrapper.RequestMethod.*;

public class CustomerController implements Controller {

    private final CustomerManager manager;

    public CustomerController(ManagersContainer container) {
        manager = container.getCustomerManager();
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
        return new ResponseWrapper(JSONUtils.toJSONString(manager.getAll()));
    }

    private ResponseWrapper getById(RequestWrapper rw) {
        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);
            return manager.getById(id)
                    .map(c -> new ResponseWrapper(JSONUtils.toJSONString(c)))
                    .orElse(ResponseWrapper.NotFound("customer not found"));
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }

    private ResponseWrapper create(RequestWrapper rw) {

        return Optional.ofNullable(JSONUtils.toObject(rw.payload, Customer.class))
                .flatMap(c -> manager.addCustomer(c.getName()))
                .map(c -> new ResponseWrapper(JSONUtils.toJSONString(c)))
                .orElse(ResponseWrapper.BadRequest("error creating customer"));
    }

    private ResponseWrapper update(RequestWrapper rw) {
        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);

            return Optional.ofNullable(JSONUtils.toObject(rw.payload, Customer.class))
                    .flatMap(c -> manager.updateCustomer(id, c.getName(), c.getDiscountCard().getId())
                            ? manager.getById(id)
                            : Optional.empty())
                    .map(c -> new ResponseWrapper(JSONUtils.toJSONString(c)))
                    .orElse(ResponseWrapper.BadRequest("error updating customer"));
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }

    private ResponseWrapper delete(RequestWrapper rw) {

        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);

            return manager.deleteCustomer(id)
                    ? ResponseWrapper.OK()
                    : ResponseWrapper.NotFound();
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }
}
