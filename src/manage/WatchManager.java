package manage;

import model.*;
import utils.DBException;

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
    private DAO<Watch> dao;
    private Logger logger;

    public WatchManager() {
        dao = DAOFactory.getWatchDAO();
        logger = Logger.getLogger(WatchManager.class.getName());
    }

    public List<Watch> getAll() {
        try {
            return dao.getAll();
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "get all watches error");
        }

        return Collections.emptyList();
    }

    public Optional<Watch> addWatch(String brand, Watch.WatchType type, BigDecimal price, int qty,
                                    int vendorId) {
        if (brand == null || type == null || price == null) {
            return Optional.empty();
        }

        try {
            Watch newWatch = new Watch(-1, brand, type, price, qty,
                    new Vendor(vendorId, null, null));
            return Optional.of(dao.create(newWatch));
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "add watch error", e);
        }

        return Optional.empty();
    }

    public boolean updateWatch(int watchId, String brand, Watch.WatchType type, BigDecimal price,
                               int qty,
                               int vendorId) {

        if (brand == null || type == null || price == null) {
            return false;
        }

        try {
            Watch watch = new Watch(watchId, brand, type, price, qty,
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
            return dao.delete(watchId);
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
                .filter(w -> w.getPrice().compareTo(upperLimit) < 0)
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
}
