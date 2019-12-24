package manage;

import model.Country;
import model.DAO;
import model.DAOFactory;

public class CountriesManager {
    private DAO<Country> countryDAO;

    public CountriesManager() {
        countryDAO = DAOFactory.getCountriesDAO();
    }

    public void showCountries() {
        System.out.println("----------- Countries ------------");
        for (Country country : countryDAO.getAll()) {
            System.out.println(country.getId() + " " + country.getName());
        }
        System.out.println("----------------------------------");
    }

    public Country addCountry(String countryName) {
        return countryDAO.create(new Country(-1, countryName));
    }

    public Country updateCountry(int countryId, String countryName) {
        Country country = new Country(countryId, countryName);
        if (countryDAO.update(country)) {
            return country;
        }

        return null;
    }

    public boolean deleteCountry(int countryId) {
        return countryDAO.delete(countryId);
    }
}

