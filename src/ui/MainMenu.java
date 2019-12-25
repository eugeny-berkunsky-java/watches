package ui;

import java.util.Scanner;

public class MainMenu {
    public static final int COUNTRIES = 1;
    public static final int VENDORS = 2;
    public static final int CUSTOMERS = 3;
    public static final int WATCHES = 4;
    public static final int ORDERS = 5;

    private CountriesMenu countriesMenu = new CountriesMenu();
    private VendorsMenu vendorsMenu = new VendorsMenu();

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
                    vendorsMenu.show(scanner);
                    break;
                }

                case CUSTOMERS: {
                    // todo: add customers menu
                    break;
                }

                case WATCHES: {
                    // todo: add watches menu
                    break;
                }

                case ORDERS: {
                    //todo: add orders menu
                    break;
                }
            }
        } while (selection != 0);

    }
}
