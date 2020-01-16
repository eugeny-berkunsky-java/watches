package com.company.watches.manage;

import com.company.watches.dao.DAO;
import com.company.watches.dao.DAOContainer;
import com.company.watches.model.Country;
import com.company.watches.utils.DBException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountriesManager {
    private static Logger logger = Logger.getLogger(CountriesManager.class.getName());

    private DAO<Country> dao;

    public CountriesManager(DAOContainer container) {
        dao = container.getCountriesDAO();
    }

    public List<Country> getAll() {
        try {
            final List<Country> result = dao.getAll();
            return result == null ? Collections.emptyList() : result;
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "get all countries error", e);
        }

        return Collections.emptyList();
    }

    public Optional<Country> addCountry(String countryName) {
        if (countryName == null || countryName.trim().length() == 0) {
            return Optional.empty();
        }

        try {
            return dao.create(new Country(-1, countryName.trim()));
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "add country error", e);
        }

        return Optional.empty();
    }

    public boolean updateCountry(int countryId, String countryName) {
        if (countryId == -1 || countryName == null || countryName.trim().length() == 0) {
            return false;
        }

        try {
            return dao.update(new Country(countryId, countryName.trim()));
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "update country error", e);
        }

        return false;
    }

    public boolean deleteCountry(int countryId) {
        try {
            return countryId != -1 && dao.delete(countryId);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete country error", e);
        }

        return false;
    }

    public Optional<Country> getById(int countryId) {
        try {
            return countryId == -1 ? Optional.empty() : dao.getById(countryId);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "getById error", e);
        }

        return Optional.empty();
    }
}

