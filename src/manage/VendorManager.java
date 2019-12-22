package manage;

import model.Country;
import model.Vendor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VendorManager {
    private List<Vendor> vendors = new ArrayList<>(
            Arrays.asList(
                    new Vendor("Cartier", new Country(1, "Italy")),
                    new Vendor("Bell & Ross", new Country(2, "France")),
                    new Vendor("Bremont", new Country(3, "UK")),
                    new Vendor("Beijing Watch Factory", new Country(4, "China"))));

    public void showVendors() {
        System.out.println("------------------------Vendors-----------------------");
        for (Vendor vendor : vendors) {
            System.out.format("name: %s [%s]%n", vendor.getVendorName(),
                    vendor.getCountry().getName());
        }
        System.out.println("------------------------------------------------------");
    }
}


