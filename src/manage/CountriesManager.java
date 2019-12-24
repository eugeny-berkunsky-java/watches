package manage;

import model.Country;
import model.CountryDAO;

public class CountriesManager {
    private CountryDAO countryDAO;

    public CountriesManager() {
        countryDAO = new CountryDAO();
    }

    public void showCountries() {
        System.out.println("----------- Countries ------------");
        for (Country country : countryDAO.getAll()) {
            System.out.println(country.getId() + " " + country.getName());
        }
        System.out.println("----------------------------------");
    }

    public Country addCountry(String countryName) {
        return countryDAO.create(countryName);
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

