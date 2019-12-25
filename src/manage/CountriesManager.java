package manage;

import model.Country;
import model.DAO;
import model.DAOFactory;

import java.util.List;

public class CountriesManager {
    private DAO<Country> countryDAO;

    public CountriesManager() {
        countryDAO = DAOFactory.getCountriesDAO();
    }

    public List<Country> getAll() {
        return countryDAO.getAll();
    }

    public Country addCountry(String countryName) {
        if (countryName == null || countryName.trim().length() == 0) {
            return null;
        }

        return countryDAO.create(new Country(-1, countryName));
    }

    public boolean updateCountry(int countryId, String countryName) {
        if (countryName == null || countryName.trim().length() == 0) {
            return false;
        }

        return countryDAO.update(new Country(countryId, countryName));
    }

    public boolean deleteCountry(int countryId) {
        return countryDAO.delete(countryId);
    }
}

