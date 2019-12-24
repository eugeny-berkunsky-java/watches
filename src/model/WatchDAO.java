package model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.Watch.WatchType.ANALOGUE;
import static model.Watch.WatchType.DIGITAL;

public class WatchDAO {
    private Map<String, Watch> watches;


    public WatchDAO() {
        watches = new HashMap<>();
    }

    public void generateWatches() {
        Vendor vendor1 = new Vendor(1, "Cartier", new Country(1, "Italy"));
        Vendor vendor2 = new Vendor(2, "Bell & Ross", new Country(2, "France"));
        Vendor vendor3 = new Vendor(3, "Bremont", new Country(3, "UK"));

        add(new Watch("Model 1", ANALOGUE, new BigDecimal("10.00"), 10, vendor1));
        add(new Watch("Model 2", DIGITAL, new BigDecimal("200.00"), 10, vendor1));

        add(new Watch("Model 3", ANALOGUE, new BigDecimal("50.00"), 10, vendor2));
        add(new Watch("Model 4", DIGITAL, new BigDecimal("200.00"), 10, vendor2));

        add(new Watch("Model 5", ANALOGUE, new BigDecimal("500.00"), 10, vendor3));
        add(new Watch("Model 6", DIGITAL, new BigDecimal("100.00"), 10, vendor3));
        add(new Watch("Model 7", DIGITAL, new BigDecimal("1000.00"), 10, vendor3));
    }

    public void load(VendorDAO vendorDAO) {
        throw new NotImplementedException();
    }

    public void save() {
        throw new NotImplementedException();
    }

    public List<Watch> getAll() {
        return new ArrayList<>(watches.values());
    }

    public void add(Watch watch) {
        if (watch == null || watch.getBrand() == null) {
            return;
        }

        watches.putIfAbsent(watch.getBrand().toLowerCase().trim(), watch);
    }
}
