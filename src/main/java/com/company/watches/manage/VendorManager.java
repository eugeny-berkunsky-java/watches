package com.company.watches.manage;

import com.company.watches.model.Country;
import com.company.watches.model.DAO;
import com.company.watches.model.DAOContainer;
import com.company.watches.model.Vendor;
import com.company.watches.utils.DBException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VendorManager {
    private static final Logger logger = Logger.getLogger(VendorManager.class.getName());

    private DAO<Vendor> dao;

    public VendorManager(DAOContainer container) {
        dao = container.getVendorsDAO();
    }

    public List<Vendor> getAll() {
        try {
            final List<Vendor> result = dao.getAll();
            return result == null ? Collections.emptyList() : result;
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "get all vendors error", e);
        }

        return Collections.emptyList();
    }

    public Optional<Vendor> addVendor(String vendorName, int countryId) {
        if (countryId == -1 || vendorName == null || vendorName.trim().length() == 0) {
            return Optional.empty();
        }

        try {
            return dao.create(new Vendor(-1, vendorName.trim(),
                    new Country(countryId, null)));

        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "add new vendor error", e);
        }

        return Optional.empty();
    }

    public boolean updateVendor(Vendor vendor) {
        if (vendor == null
                || vendor.getId() == -1
                || vendor.getVendorName() == null
                || vendor.getVendorName().trim().length() == 0
                || vendor.getCountry() == null
                || vendor.getCountry().getId() == -1) {
            return false;
        }
        try {
            vendor.setVendorName(vendor.getVendorName().trim());
            return dao.update(vendor);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "update vendor error", e);
        }

        return false;
    }

    public boolean deleteVendor(int vendorId) {

        try {
            return vendorId != -1 && dao.delete(vendorId);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete vendor error", e);
        }

        return false;
    }
}


