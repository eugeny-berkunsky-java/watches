package manage;

import model.Country;
import model.CountryDAO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CountriesManager {
    private CountryDAO countryDAO;

    public CountriesManager() {
        countryDAO = new CountryDAO();
    }

    public void showCountries() {
        System.out.println("-------- Countries --------------");
        for (Country country : countryDAO.getAll()) {
            System.out.println(country.getId() + " " + country.getName());
        }
        System.out.println("----------------------------------");
    }

    public void addCountry(Country country) {
        throw new NotImplementedException();
    }

}

