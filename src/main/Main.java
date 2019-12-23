package main;

import manage.CountriesManager;
import manage.CustomerManager;
import manage.VendorManager;
import manage.WatchManager;
import ui.Menu;

import static ui.Menu.*;

public class Main {

    private Menu menu = new Menu();
    private CountriesManager cm = new CountriesManager();
    private CustomerManager customerManager = new CustomerManager();
    private VendorManager vendorManager = new VendorManager();
    private WatchManager watchManager = new WatchManager();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        int selection;
        while ((selection = menu.menu()) != 0) {
            switch (selection) {
                case COUNTRIES: {
                    cm.showCountries();
                    break;
                }
                case VENDORS: {
                    vendorManager.showVendors();
                    break;
                }
                case CUSTOMERS: {
                    customerManager.showCustomers();
                    break;
                }
                case WATCHES: {
                    watchManager.showWatches();
                    break;
                }
            }
        }
    }


}
