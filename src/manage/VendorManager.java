package manage;

import model.Country;
import model.DAO;
import model.DAOFactory;
import model.Vendor;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class VendorManager {
    private DAO<Vendor> dao;
    private Vendor emptyVendor;

    public VendorManager() {
        dao = DAOFactory.getVendorsDAO();
        emptyVendor = new Vendor(-1, "<empty>", new Country(-1, "<empty>"));
    }

    public List<Vendor> getAll() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return Collections.emptyList();
    }

    public Vendor addVendor(String vendorName, int countryId) {
        if (vendorName == null || vendorName.trim().length() == 0) {
            return emptyVendor;
        }

        try {
            return dao.create(new Vendor(-1, vendorName, new Country(countryId, null)));
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return emptyVendor;
    }

    public boolean updateVendor(Vendor vendor) {
        if (vendor == null
                || vendor.getVendorName() == null
                || vendor.getVendorName().trim().length() == 0
                || vendor.getCountry() == null) {
            return false;
        }
        try {
            return dao.update(vendor);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return false;
    }

    public boolean deleteVendor(int vendorId) {
        try {
            return dao.delete(vendorId);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return false;
    }
}


