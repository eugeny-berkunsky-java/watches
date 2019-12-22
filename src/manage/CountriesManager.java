package manage;

import model.Country;
import model.Customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CountriesManager {
    private List<Country> countries = new ArrayList<>(
            Arrays.asList(
                    new Country(1, "Ukraine"),
                    new Country(2, "USA"),
                    new Country(3, "China"),
                    new Country(4, "Switzerland")
            )
    );




   public void showCountries() {
        System.out.println("-------- Countries --------------");
        for (Country country : countries) {
            System.out.println(country.getId() + " " + country.getName());
        }
        System.out.println("----------------------------------");
    }


}
