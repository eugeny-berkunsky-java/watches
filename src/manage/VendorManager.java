package manage;

import model.Vendor;
import model.VendorDAO;

public class VendorManager {
    private VendorDAO dao;

    public VendorManager() {
        dao = new VendorDAO();
        dao.generateVendors();
    }

    public void showVendors() {
        System.out.println("------------------------Vendors-----------------------");
        dao.getAll().stream().map(this::vendorView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");
    }

    private String vendorView(Vendor vendor) {
        return vendor == null ? "" : String.format("name: %s [%s]",
                vendor.getVendorName(), vendor.getCountry().getName());
    }
}


