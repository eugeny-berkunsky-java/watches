package ui;

import manage.CountriesManager;

import java.util.Scanner;

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
                    countriesManager.showCountries();
                    break;
                }

                case ADD_COUNTRY: {
                    //todo: add country menu
                    break;
                }

                case UPDATE_COUNTRY: {
                    //todo: update country menu
                    break;
                }

                case DELETE_COUNTRY: {
                    //todo: delete country menu
                    break;
                }
            }
        } while (answer != 0);
    }
}
