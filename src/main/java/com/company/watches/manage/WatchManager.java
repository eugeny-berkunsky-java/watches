package com.company.watches.manage;

import com.company.watches.model.*;
import com.company.watches.utils.DBException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class WatchManager {
    private static Logger logger = Logger.getLogger(WatchManager.class.getName());
    private DAO<Watch> dao;

    public WatchManager(DAOContainer container) {
        dao = container.getWatchDAO();
    }

    public List<Watch> getAll() {
        try {
            final List<Watch> result = dao.getAll();
            return result == null ? Collections.emptyList() : result;
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "get all watches error");
        }

        return Collections.emptyList();
    }

    public Optional<Watch> addWatch(String brand, Watch.WatchType type, BigDecimal price, int qty,
                                    int vendorId) {
        if (brand == null
                || brand.trim().length() == 0
                || type == null
                || price == null
                || qty < 1
                || vendorId == -1
        ) {
            return Optional.empty();
        }

        try {
            Watch watch = new Watch(-1, brand.trim(), type, price, qty,
                    new Vendor(vendorId, null, null));

            return dao.create(watch);

        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "add watch error", e);
        }

        return Optional.empty();
    }

    public boolean updateWatch(int watchId, String brand, Watch.WatchType type, BigDecimal price,
                               int qty,
                               int vendorId) {

        if (watchId == -1
                || brand == null
                || brand.trim().length() == 0
                || type == null
                || price == null
                || qty < 1
                || vendorId == -1
        ) {
            return false;
        }

        try {
            Watch watch = new Watch(watchId, brand.trim(), type, price, qty,
                    new Vendor(vendorId, null,
                            new Country(-1, null)));
            return dao.update(watch);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "update watch error", e);
        }

        return false;
    }

    public boolean deleteWatch(int watchId) {
        try {
            return watchId != -1 && dao.delete(watchId);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete watch error", e);
        }

        return false;
    }

    public List<Watch> getByType(Watch.WatchType type) {
        return type == null
                ? Collections.emptyList()
                : getAll().stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Watch> getAnalogueWatchNotGreaterByPriceThan(BigDecimal upperLimit) {
        return upperLimit == null ? Collections.emptyList()
                : getByType(Watch.WatchType.ANALOGUE).stream()
                .filter(w -> w.getPrice().compareTo(upperLimit) <= 0)
                .collect(Collectors.toList());
    }

    public List<Watch> getByCountry(String countryName) {
        return countryName == null || countryName.trim().length() == 0
                ? Collections.emptyList()
                : getAll().stream()
                .filter(w -> w.getVendor().getCountry().getName().trim().equalsIgnoreCase(countryName))
                .collect(Collectors.toList());
    }

    public List<Vendor> getVendorByTotalSumNotGreaterThan(BigDecimal upperLimit) {
        if (upperLimit == null) {
            return Collections.emptyList();
        }

        Predicate<List<Watch>> predicate = l -> totalSum(l).compareTo(upperLimit) <= 0;

        Map<Vendor, List<Watch>> collect = getAll().stream()
                .collect(Collectors.groupingBy(Watch::getVendor, Collectors.toList()));

        return collect.entrySet().stream()
                .filter(e -> predicate.test(e.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private BigDecimal totalSum(List<Watch> watches) {
        BigDecimal result = BigDecimal.ZERO;

        for (Watch watch : watches) {
            result = result.add(watch.getPrice().multiply(new BigDecimal(watch.getQty())));
        }

        return result;
    }

    public Optional<Watch> getById(int watchId) {
        try {
            return watchId == -1 ? Optional.empty() : dao.getById(watchId);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "getById error", e);
        }

        return Optional.empty();
    }
}
