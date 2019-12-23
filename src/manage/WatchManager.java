package manage;

import model.Country;
import model.Vendor;
import model.Watch;
import model.WatchDAO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static model.Watch.WatchType.ANALOGUE;

public class WatchManager {
    private WatchDAO watchDAO;

    public WatchManager() {
        watchDAO = new WatchDAO();
        watchDAO.generateWatches();
    }

    public void showWatches() {
        System.out.println("------------------------Watches-----------------------");
        watchDAO.getAll().stream().map(this::watchView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
    }

    public List<Watch> getByType(Watch.WatchType type) {
        return watchDAO.getAll().stream()
                .filter(w -> w.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Watch> getAnalogueWatchNotGreaterByPriceThan(BigDecimal upperLimit) {
        return watchDAO.getAll().stream()
                .filter(w -> w.getType() == ANALOGUE
                        && w.getPrice().compareTo(upperLimit) < 0)
                .collect(Collectors.toList());
    }

    public List<Watch> getByCountry(Country country) {
        return watchDAO.getAll().stream()
                .filter(w -> w.getVendor().getCountry().equals(country))
                .collect(Collectors.toList());
    }

    public List<Vendor> getVendorByTotalSumNotGreaterThan(BigDecimal upperLimit) {

        Predicate<List<Watch>> predicate = l -> totalSum(l).compareTo(upperLimit) < 0;

        Map<Vendor, List<Watch>> collect = watchDAO.getAll().stream()
                .collect(Collectors.groupingBy(Watch::getVendor, Collectors.toList()));

        return collect.entrySet().stream()
                .filter(e -> predicate.test(e.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

    }

    private String watchView(Watch watch) {
        return String.format("%s - %s [%s]",
                watch.getBrand(),
                watch.getVendor().getVendorName(),
                watch.getVendor().getCountry().getName()
        );
    }

    private BigDecimal totalSum(List<Watch> watches) {
        BigDecimal result = BigDecimal.ZERO;

        for (Watch watch : watches) {

            result = result.add(watch.getPrice().multiply(new BigDecimal(watch.getQuantity(),
                    new MathContext(2))));
        }

        return result;
    }
}
