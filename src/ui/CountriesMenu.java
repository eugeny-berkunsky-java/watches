package ui;

import manage.CountriesManager;
import model.Country;
import utils.UserInput;

import java.util.Optional;
import java.util.function.Function;

public class CountriesMenu {

    private final static int SHOW_ALL_COUNTRIES = 1;
    private final static int ADD_COUNTRY = 2;
    private final static int UPDATE_COUNTRY = 3;
    private final static int DELETE_COUNTRY = 4;
    private final static int PREVIOUS_MENU = 0;


    private CountriesManager countriesManager = new CountriesManager();

    private void printMenu() {
        System.out.println("--------- Countries menu ---------");
        System.out.format("%d. Show all countries%n", SHOW_ALL_COUNTRIES);
        System.out.format("%d. Add country%n", ADD_COUNTRY);
        System.out.format("%d. Update country%n", UPDATE_COUNTRY);
        System.out.format("%d. Delete country%n", DELETE_COUNTRY);
        System.out.format("%d. return to main menu%n", PREVIOUS_MENU);
    }

    public void show(UserInput userInput) {
        int answer;

        do {
            printMenu();
            answer = userInput.getNumber("your choice", -1);

            switch (answer) {
                case SHOW_ALL_COUNTRIES: {
                    showCountries();
                    break;
                }

                case ADD_COUNTRY: {
                    addCountry(userInput);
                    break;
                }

                case UPDATE_COUNTRY: {
                    updateCountry(userInput);
                    break;
                }

                case DELETE_COUNTRY: {
                    deleteCountry(userInput);
                    break;
                }
            }
        } while (answer != PREVIOUS_MENU);
    }

    private void showCountries() {

        Function<Country, String> countryView =
                c -> String.format("%d - %s", c.getId(), c.getName());

        System.out.println("----------- Countries ------------");
        countriesManager.getAll().stream().map(countryView).forEach(System.out::println);
        System.out.println("----------------------------------");
    }

    private void addCountry(UserInput userInput) {
        String countryName = userInput.getString("new country name");
        Optional<Country> country = countriesManager.addCountry(countryName);
        if (country.isPresent()) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void updateCountry(UserInput userInput) {
        System.out.println("what country do you want to update?");
        int countryId = userInput.getNumber("id", -1);

        String countryName = userInput.getString("NEW name");

        if (countriesManager.updateCountry(countryId, countryName)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void deleteCountry(UserInput userInput) {
        System.out.println("what country do you want to delete?");
        int countryId = userInput.getNumber("id", -1);

        if (countriesManager.deleteCountry(countryId)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }
}
