package com.company.watches.ui;

import com.company.watches.manage.ManagersContainer;
import com.company.watches.utils.UserInput;

public class MainMenu {
    private static final int COUNTRIES = 1;
    private static final int VENDORS = 2;
    private static final int CUSTOMERS = 3;
    private static final int WATCHES = 4;
    private static final int ORDERS = 5;
    private static final int EXIT = 0;

    private CountriesMenu countriesMenu;
    private VendorsMenu vendorsMenu;
    private CustomersMenu customersMenu;
    private WatchesMenu watchesMenu;
    private OrdersMenu ordersMenu;

    public MainMenu(ManagersContainer managersContainer) {
        countriesMenu = new CountriesMenu(managersContainer);
        vendorsMenu = new VendorsMenu(managersContainer);
        customersMenu = new CustomersMenu(managersContainer);
        watchesMenu = new WatchesMenu(managersContainer);
        ordersMenu = new OrdersMenu(managersContainer);
    }

    private void printMenu() {
        System.out.println("------------ Main menu -----------");
        System.out.format("%d. Countries%n", COUNTRIES);
        System.out.format("%d. Vendors%n", VENDORS);
        System.out.format("%d. Customers%n", CUSTOMERS);
        System.out.format("%d. Watches%n", WATCHES);
        System.out.format("%d. Orders%n", ORDERS);
        System.out.format("%d. Exit%n", EXIT);
    }

    public void show(UserInput userInput) {

        int selection;
        do {
            printMenu();
            selection = userInput.getNumber("your choice", -1);

            switch (selection) {
                case COUNTRIES: {
                    countriesMenu.show(userInput);
                    break;
                }

                case VENDORS: {
                    vendorsMenu.show(userInput);
                    break;
                }

                case CUSTOMERS: {
                    customersMenu.show(userInput);
                    break;
                }

                case WATCHES: {
                    watchesMenu.show(userInput);
                    break;
                }

                case ORDERS: {
                    ordersMenu.show(userInput);
                    break;
                }
            }
        } while (selection != EXIT);

    }
}
