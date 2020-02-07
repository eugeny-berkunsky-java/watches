package com.company.watches.web;

import com.company.watches.manage.ManagersContainer;
import com.company.watches.manage.OrdersManager;
import com.company.watches.model.Order;
import com.company.watches.utils.JSONUtils;

import java.util.Optional;

import static com.company.watches.web.RequestWrapper.RequestMethod.*;

public class OrderController implements Controller {

    private OrdersManager manager;

    public OrderController(ManagersContainer container) {
        manager = container.getOrdersManager();
    }

    @Override
    public ResponseWrapper process(RequestWrapper rw) {
        if (rw.method == GET && rw.path.equals("order/")) {
            return getAll();
        } else if (rw.method == GET && rw.path.matches("order/\\d+$")) {
            return getById(rw);
        } else if (rw.method == POST && rw.path.equals("order/")) {
            return create(rw);
        } else if (rw.method == DELETE && rw.path.matches("order/\\d+$")) {
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
                    .map(o -> new ResponseWrapper(JSONUtils.toJSONString(o)))
                    .orElse(ResponseWrapper.NotFound());
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }

    private ResponseWrapper create(RequestWrapper rw) {
        return Optional.ofNullable(JSONUtils.toObject(rw.payload, Order.class))
                .flatMap(manager::addOrder)
                .map(o -> new ResponseWrapper(JSONUtils.toJSONString(o)))
                .orElse(ResponseWrapper.BadRequest("error adding order"));
    }

    private ResponseWrapper delete(RequestWrapper rw) {
        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);

            return manager.deleteOrder(id)
                    ? ResponseWrapper.OK()
                    : ResponseWrapper.NotFound();
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }
}
