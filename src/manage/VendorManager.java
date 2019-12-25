package manage;

import model.Country;
import model.DAO;
import model.DAOFactory;
import model.Vendor;

import java.util.List;

public class VendorManager {
    private DAO<Vendor> dao;

    public VendorManager() {
        dao = DAOFactory.getVendorsDAO();
    }

    public List<Vendor> getAll() {
        return dao.getAll();
    }

    public Vendor addVendor(String vendorName, int countryId) {
        if (vendorName == null || vendorName.trim().length() == 0) {
            return null;
        }

        return dao.create(new Vendor(-1, vendorName, new Country(countryId, null)));
    }

    public boolean updateVendor(Vendor vendor) {
        if (vendor == null
                || vendor.getVendorName() == null
                || vendor.getVendorName().trim().length() == 0
                || vendor.getCountry() == null) {
            return false;
        }
        return dao.update(vendor);
    }

    public boolean deleteVendor(int vendorId) {
        return dao.delete(vendorId);
    }
}


