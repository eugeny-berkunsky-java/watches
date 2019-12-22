package main;

import manage.CountriesManager;
import manage.CustomerManager;
import ui.Menu;

import static ui.Menu.COUNTRIES;
import static ui.Menu.CUSTOMERS;
import static ui.Menu.VENDORS;

public class Main {

    private Menu menu = new Menu();
    private CountriesManager cm = new CountriesManager();
    private CustomerManager customerManager = new CustomerManager();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        int selection;
        while ((selection = menu.menu()) != 0) {
            switch (selection) {
                case COUNTRIES:
                    cm.showCountries();
                    break;
                case VENDORS:
                    showVendors();
                    break;
                case CUSTOMERS:
               customerManager.showCustomers();
                    break;
            }
        }
    }

    private void showVendors() {

    }


}
