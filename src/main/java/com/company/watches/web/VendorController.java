package com.company.watches.web;

import com.company.watches.manage.ManagersContainer;
import com.company.watches.manage.VendorManager;
import com.company.watches.model.Vendor;
import com.company.watches.utils.JSONUtils;

import java.util.Optional;

import static com.company.watches.web.RequestWrapper.RequestMethod.*;

public class VendorController implements Controller {
    private VendorManager manager;

    public VendorController(ManagersContainer container) {
        manager = container.getVendorManager();
    }

    @Override
    public ResponseWrapper process(RequestWrapper rw) {
        if (rw.method == GET && rw.path.equals("vendor/")) {
            return getAll();
        } else if (rw.method == GET && rw.path.matches("vendor/\\d+$")) {
            return getById(rw);
        } else if (rw.method == POST && rw.path.equals("vendor/")) {
            return create(rw);
        } else if (rw.method == PUT && rw.path.matches("vendor/\\d+$")) {
            return update(rw);
        } else if (rw.method == DELETE && rw.path.matches("vendor/\\d+$")) {
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
                    .map(v -> new ResponseWrapper(JSONUtils.toJSONString(v)))
                    .orElse(ResponseWrapper.NotFound());
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }

    }

    private ResponseWrapper create(RequestWrapper rw) {
        return Optional.ofNullable(JSONUtils.toObject(rw.payload, Vendor.class))
                .flatMap(v -> manager.addVendor(v.getName(), v.getCountry().getId()))
                .map(v -> new ResponseWrapper(JSONUtils.toJSONString(v)))
                .orElse(ResponseWrapper.BadRequest("error creating vendor"));
    }

    private ResponseWrapper update(RequestWrapper rw) {
        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);

            return Optional.ofNullable(JSONUtils.toObject(rw.payload, Vendor.class))
                    .flatMap(v -> manager.updateVendor(id, v.getName(), v.getCountry().getId())
                            ? manager.getById(id) : Optional.empty())
                    .map(v -> new ResponseWrapper(JSONUtils.toJSONString(v)))
                    .orElse(ResponseWrapper.BadRequest("error updating vendor"));
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }

    }

    private ResponseWrapper delete(RequestWrapper rw) {
        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);

            return manager.deleteVendor(id)
                    ? ResponseWrapper.OK()
                    : ResponseWrapper.NotFound();
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }
}
