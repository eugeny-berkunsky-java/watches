package com.company.watches.web;

import com.company.watches.manage.ManagersContainer;
import com.company.watches.manage.WatchManager;
import com.company.watches.model.Watch;
import com.company.watches.utils.JSONUtils;

import java.util.Optional;

import static com.company.watches.web.RequestWrapper.RequestMethod.*;

public class WatchController implements Controller {

    private WatchManager manager;

    public WatchController(ManagersContainer container) {
        manager = container.getWatchManager();
    }

    @Override
    public ResponseWrapper process(RequestWrapper rw) {
        if (rw.method == GET && rw.path.equals("watch/")) {
            return getAll();
        } else if (rw.method == GET && rw.path.matches("watch/\\d+$")) {
            return getById(rw);
        } else if (rw.method == POST && rw.path.equals("watch/")) {
            return create(rw);
        } else if (rw.method == PUT && rw.path.matches("watch/\\d+$")) {
            return update(rw);
        } else if (rw.method == DELETE && rw.path.matches("watch/\\d+$")) {
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
                    .map(w -> new ResponseWrapper(JSONUtils.toJSONString(w)))
                    .orElse(ResponseWrapper.NotFound());
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }

    private ResponseWrapper create(RequestWrapper rw) {
        return Optional.ofNullable(JSONUtils.toObject(rw.payload, Watch.class))
                .flatMap(w -> manager.addWatch(w.getBrand(), w.getType(), w.getPrice(), w.getQty(),
                        w.getVendor().getId()))
                .map(w -> new ResponseWrapper(JSONUtils.toJSONString(w)))
                .orElse(ResponseWrapper.BadRequest("error creating watch"));
    }

    private ResponseWrapper update(RequestWrapper rw) {
        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);

            return Optional.ofNullable(JSONUtils.toObject(rw.payload, Watch.class))
                    .flatMap(w -> manager.updateWatch(id, w.getBrand(), w.getType(), w.getPrice(),
                            w.getQty(), w.getVendor().getId())
                            ? manager.getById(id)
                            : Optional.empty())
                    .map(w -> new ResponseWrapper(JSONUtils.toJSONString(w)))
                    .orElse(ResponseWrapper.BadRequest());
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }

    private ResponseWrapper delete(RequestWrapper rw) {
        try {
            int id = Integer.parseInt(rw.path.split("/")[1]);

            return manager.deleteWatch(id)
                    ? ResponseWrapper.OK()
                    : ResponseWrapper.NotFound("watch not found");
        } catch (NumberFormatException e) {
            return ResponseWrapper.BadRequest();
        }
    }
}
