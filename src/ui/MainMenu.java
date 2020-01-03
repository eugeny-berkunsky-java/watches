package ui;

import utils.UserInput;

public class MainMenu {
    public static final int COUNTRIES = 1;
    public static final int VENDORS = 2;
    public static final int CUSTOMERS = 3;
    public static final int WATCHES = 4;
    public static final int ORDERS = 5;
    public static final int EXIT = 0;

    private CountriesMenu countriesMenu = new CountriesMenu();
    private VendorsMenu vendorsMenu = new VendorsMenu();
    private CustomersMenu customersMenu = new CustomersMenu();
    private WatchesMenu watchesMenu = new WatchesMenu();
    private OrdersMenu ordersMenu = new OrdersMenu();

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
