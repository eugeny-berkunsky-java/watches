package model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorDAO {
    private Map<String, Vendor> vendors;

    public VendorDAO() {
        vendors = new HashMap<>();
    }

    public void generateVendors() {
        add(new Vendor("Cartier", new Country(1, "Italy")));
        add(new Vendor("Bell & Ross", new Country(2, "France")));
        add(new Vendor("Bremont", new Country(3, "UK")));
    }

    public void add(Vendor vendor) {
        if (vendor == null
                || vendor.getVendorName() == null
                || vendor.getVendorName().trim().length() == 0
        ) {
            return;
        }

        vendors.putIfAbsent(vendor.getVendorName().toLowerCase().trim(), vendor);
    }

    public List<Vendor> getAll() {
        return new ArrayList<>(vendors.values());
    }

    public void load(CountryDAO dao) {
        throw new NotImplementedException();
    }

    public void save() {
        throw new NotImplementedException();
    }
}
