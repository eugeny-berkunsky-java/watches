package manage;

import model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WatchManager {
    private DAO<Watch> dao;
    private Watch emptyWatch;

    public WatchManager() {
        dao = DAOFactory.getWatchDAO();
        emptyWatch = new Watch(-1, "<empty>", Watch.WatchType.ANALOGUE, BigDecimal.ZERO, 0,
                new Vendor(-1, "<empty>",
                        new Country(-1, "<empty>")));
    }

    public List<Watch> getAll() {
        try {
            return dao.getAll();
        } catch (SQLException | DBException e) {
            e.printStackTrace(System.err);
            return Collections.emptyList();
        }
    }

    public Watch addWatch(String brand, Watch.WatchType type, BigDecimal price, int qty,
                          int vendorId) {
        if (brand == null || type == null || price == null) {
            return emptyWatch;
        }

        try {
            Watch newWatch = new Watch(-1, brand, type, price, qty,
                    new Vendor(vendorId, null, null));
            return dao.create(newWatch);
        } catch (SQLException | DBException e) {
            e.printStackTrace(System.err);
            return emptyWatch;
        }
    }

    public boolean updateWatch(int watchId, String brand, Watch.WatchType type, BigDecimal price,
                               int qty,
                               int vendorId) {
        try {
            Watch watch = new Watch(watchId, brand, type, price, qty, new Vendor(vendorId, null,
                    new Country(-1, null)));
            return dao.update(watch);
        } catch (SQLException | DBException e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    public boolean deleteWatch(int watchId) {
        try {
            return dao.delete(watchId);
        } catch (SQLException | DBException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Watch> getByType(Watch.WatchType type) {
        return type == null ? Collections.emptyList() : getAll().stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Watch> getAnalogueWatchNotGreaterByPriceThan(BigDecimal upperLimit) {
        return getByType(Watch.WatchType.ANALOGUE).stream()
                .filter(w -> w.getPrice().compareTo(upperLimit) < 0)
                .collect(Collectors.toList());
    }

    public List<Watch> getByCountry(Country country) {
        return country == null ? Collections.emptyList()
                : getAll().stream()
                .filter(w -> w.getVendor().getCountry().equals(country))
                .collect(Collectors.toList());
    }

    public List<Vendor> getVendorByTotalSumNotGreaterThan(BigDecimal upperLimit) {
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

 /*   public void showWatches() {
        System.out.println("------------------------Watches-----------------------");
        watchDAO.getAll().stream().map(this::watchView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
    }*/


   /* private String watchView(Watch watch) {
        return String.format("%s - %s [%s]",
                watch.getBrand(),
                watch.getVendor().getVendorName(),
                watch.getVendor().getCountry().getName()
        );
    }*/

}
