package manage;

import model.DAO;
import model.DAOFactory;
import model.Vendor;

public class VendorManager {
    private DAO<Vendor> dao;

    public VendorManager() {
        dao = DAOFactory.getVendorsDAO();
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


