package manage;

import model.Country;
import model.DAO;
import model.DAOFactory;
import model.Vendor;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VendorManager {
    private DAO<Vendor> dao;
    private Logger logger;

    public VendorManager() {
        dao = DAOFactory.getVendorsDAO();
        logger = Logger.getLogger(VendorManager.class.getName());
    }

    public List<Vendor> getAll() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "get all vendors error", e);
        }

        return Collections.emptyList();
    }

    public Optional<Vendor> addVendor(String vendorName, int countryId) {
        if (vendorName == null || vendorName.trim().length() == 0) {
            return Optional.empty();
        }

        try {
            return Optional.of(dao.create(new Vendor(-1, vendorName, new Country(countryId, null))));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "add new vendor error", e);
        }

        return Optional.empty();
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
            logger.log(Level.SEVERE, "update vendor error", e);
        }

        return false;
    }

    public boolean deleteVendor(int vendorId) {
        try {
            return dao.delete(vendorId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "delete vendor error", e);
        }

        return false;
    }
}


