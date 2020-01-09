package manage;

import model.Country;
import model.DAO;
import model.DAOContainer;
import utils.DBException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountriesManager {
    private static Logger logger = Logger.getLogger(CountriesManager.class.getName());

    private DAO<Country> countryDAO;

    public CountriesManager(DAOContainer container) {
        countryDAO = container.getCountriesDAO();
    }

    public List<Country> getAll() {
        try {
            final List<Country> result = countryDAO.getAll();
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
            final Country country = countryDAO.create(new Country(-1, countryName.trim()));
            return country == null ? Optional.empty() : Optional.of(country);
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
            return countryDAO.update(new Country(countryId, countryName.trim()));
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "update country error", e);
        }

        return false;
    }

    public boolean deleteCountry(int countryId) {
        try {
            return countryId != -1 && countryDAO.delete(countryId);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete country error", e);
        }

        return false;
    }
}

