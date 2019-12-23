package ui;

import java.util.Scanner;

public class Menu {
    public static final int COUNTRIES = 1;
    public static final int VENDORS = 2;
    public static final int CUSTOMERS = 3;
    public static final int WATCHES = 4;

    private void printMenu() {
        System.out.println("1. Countries");
        System.out.println("2. Vendors");
        System.out.println("3. Customers");
        System.out.println("4. Watches");
        System.out.println("0. Exit");
    }

    public int menu() {
        printMenu();
        return new Scanner(System.in).nextInt();
    }
}
