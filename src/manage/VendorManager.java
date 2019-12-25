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
        return dao.create(new Vendor(-1, vendorName, new Country(countryId, null)));
    }

    public Vendor updateVendor(Vendor vendor) {
        if (dao.update(vendor)) {
            return vendor;
        }

        return null;
    }

    public boolean deleteVendor(int vendorId) {
        return dao.delete(vendorId);
    }
}


