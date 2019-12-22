package manage;

import model.Country;
import model.CountryDAO;

import java.util.List;

public class CountriesManager {
private List<Country> countries;
private CountryDAO countryDAO;

    public CountriesManager() {
       countryDAO=new CountryDAO();
       countries=countryDAO.generateCountries();

    }
   public void showCountries() {
        System.out.println("-------- Countries --------------");
        for (Country country : countries) {
            System.out.println(country.getId() + " " + country.getName());
        }
        System.out.println("----------------------------------");
    }
    public void addCountry(Country country){
    countries.add(country);
    }


}

