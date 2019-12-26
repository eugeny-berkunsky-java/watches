package manage;

import model.Country;
import model.DAO;
import model.DAOFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CountriesManager {
    private DAO<Country> countryDAO;

    public CountriesManager() {
        countryDAO = DAOFactory.getCountriesDAO();
    }

    public List<Country> getAll() {
        try {
            return countryDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return Collections.emptyList();
    }

    public Optional<Country> addCountry(String countryName) {
        if (countryName == null || countryName.trim().length() == 0) {
            return Optional.empty();
        }

        try {
            return Optional.of(countryDAO.create(new Country(-1, countryName)));
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return Optional.empty();
    }

    public boolean updateCountry(int countryId, String countryName) {
        if (countryName == null || countryName.trim().length() == 0) {
            return false;
        }

        try {
            return countryDAO.update(new Country(countryId, countryName));
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return false;
    }

    public boolean deleteCountry(int countryId) {
        try {
            return countryDAO.delete(countryId);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return false;
    }
}

