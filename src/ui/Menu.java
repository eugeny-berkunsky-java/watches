package ui;

import java.util.Scanner;

public class Menu {
    private void printMenu() {
        System.out.println("1. Countries");
        System.out.println("2. Vendors");
        System.out.println("3. Customers");
        System.out.println("0. Exit");
    }

    public int menu() {
        printMenu();
        return new Scanner(System.in).nextInt();
    }
}
