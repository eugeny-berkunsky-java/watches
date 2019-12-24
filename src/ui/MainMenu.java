package ui;

import manage.CustomerManager;
import manage.OrderManager;
import manage.VendorManager;
import manage.WatchManager;

import java.util.Scanner;

public class MainMenu {
    public static final int COUNTRIES = 1;
    public static final int VENDORS = 2;
    public static final int CUSTOMERS = 3;
    public static final int WATCHES = 4;
    public static final int ORDERS = 5;

    private CountriesMenu countriesMenu = new CountriesMenu();

    private CustomerManager customerManager = new CustomerManager();
    private VendorManager vendorManager = new VendorManager();
    private WatchManager watchManager = new WatchManager();
    private OrderManager orderManager = new OrderManager();

    private void printMenu() {
        System.out.println("------------ Main menu -----------");
        System.out.println("1. Countries");
        System.out.println("2. Vendors");
        System.out.println("3. Customers");
        System.out.println("4. Watches");
        System.out.println("5. Orders");
        System.out.println("0. Exit");
    }

    public void show(Scanner scanner) {

        int selection;
        do {
            printMenu();
            selection = scanner.nextInt();

            switch (selection) {
                case COUNTRIES: {
                    countriesMenu.show(scanner);
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

                case ORDERS: {
                    orderManager.showOrders();
                    break;
                }
            }
        } while (selection != 0);

    }
}
