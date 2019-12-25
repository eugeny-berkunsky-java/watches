package ui;

import manage.CountriesManager;
import model.Country;

import java.util.Scanner;
import java.util.function.Function;

public class CountriesMenu {

    private final static int SNOW_ALL_COUNTRIES = 1;
    private final static int ADD_COUNTRY = 2;
    private final static int UPDATE_COUNTRY = 3;
    private final static int DELETE_COUNTRY = 4;


    private CountriesManager countriesManager = new CountriesManager();

    private void printMenu() {
        System.out.println("--------- Countries menu ---------");
        System.out.println("1. Show all countries");
        System.out.println("2. Add country");
        System.out.println("3. Update country");
        System.out.println("4. Delete country");
        System.out.println("0. return to main menu");
    }

    public void show(Scanner scanner) {
        int answer;

        do {
            printMenu();
            answer = scanner.nextInt();

            switch (answer) {
                case SNOW_ALL_COUNTRIES: {
                    showCountries();
                    break;
                }

                case ADD_COUNTRY: {
                    addCountry(scanner);
                    break;
                }

                case UPDATE_COUNTRY: {
                    updateCountry(scanner);
                    break;
                }

                case DELETE_COUNTRY: {
                    deleteCountry(scanner);
                    break;
                }
            }
        } while (answer != 0);
    }

    private void showCountries() {

        Function<Country, String> countryView =
                c -> String.format("%d - %s", c.getId(), c.getName());

        System.out.println("----------- Countries ------------");
        countriesManager.getAll().stream().map(countryView).forEach(System.out::println);
        System.out.println("----------------------------------");
    }

    private void addCountry(Scanner scanner) {
        System.out.print("write new country name: ");
        String countryName = scanner.next();
        Country country = countriesManager.addCountry(countryName);
        if (country != null) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void updateCountry(Scanner scanner) {
        System.out.println("which country do you want to update?");
        System.out.print("id: ");
        int countryId = scanner.nextInt();

        System.out.print("NEW name: ");
        String countryName = scanner.next();

        if (countriesManager.updateCountry(countryId, countryName)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void deleteCountry(Scanner scanner) {
        System.out.println("which country do you want to delete?");
        System.out.print("id: ");
        int countryId = scanner.nextInt();
        if (countriesManager.deleteCountry(countryId)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }
}
