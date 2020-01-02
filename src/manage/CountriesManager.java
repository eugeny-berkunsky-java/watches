package manage;

import model.Country;
import model.DAO;
import model.DAOFactory;
import utils.DBException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountriesManager {
    private Logger logger;

    private DAO<Country> countryDAO;

    public CountriesManager() {
        countryDAO = DAOFactory.getCountriesDAO();
        logger = Logger.getLogger(CountriesManager.class.getName());
    }

    public List<Country> getAll() {
        try {
            return countryDAO.getAll();
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
            return Optional.of(countryDAO.create(new Country(-1, countryName)));
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "add country error", e);
        }

        return Optional.empty();
    }

    public boolean updateCountry(int countryId, String countryName) {
        if (countryName == null || countryName.trim().length() == 0) {
            return false;
        }

        try {
            return countryDAO.update(new Country(countryId, countryName));
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "update country error", e);
        }

        return false;
    }

    public boolean deleteCountry(int countryId) {
        try {
            return countryDAO.delete(countryId);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete country error", e);
        }

        return false;
    }
}

