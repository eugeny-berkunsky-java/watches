package ui;

import manage.CustomerManager;
import model.Customer;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class CustomersMenu {
    private final static int SHOW_ALL_CUSTOMERS = 1;
    private final static int ADD_CUSTOMER = 2;
    private final static int UPDATE_CUSTOMER = 3;
    private final static int DELETE_CUSTOMER = 4;

    private CustomerManager customerManager = new CustomerManager();

    private void printMenu() {
        System.out.println("---------- Customers menu ----------");
        System.out.println("1. Show all customers");
        System.out.println("2. Add customer");
        System.out.println("3. Update customer");
        System.out.println("4. Delete customer");
        System.out.println("0. return to main menu");
    }

    public void show(Scanner scanner) {
        int answer;

        do {
            printMenu();
            answer = scanner.nextInt();

            switch (answer) {
                case SHOW_ALL_CUSTOMERS: {
                    showCustomers();
                    break;
                }
                case ADD_CUSTOMER: {
                    addCustomer(scanner);
                    break;
                }
                case UPDATE_CUSTOMER: {
                    updateCustomer(scanner);
                    break;
                }
                case DELETE_CUSTOMER: {
                    deleteCustomer(scanner);
                    break;
                }
            }
        } while (answer != 0);
    }

    private void showCustomers() {

        Function<Customer, String> customerView = c -> String.format("%d - %s - %.02f",
                c.getId(), c.getName(), c.getSumOfOrders());

        System.out.println("---------------------- Customers ---------------------");
        customerManager.getAll().stream().map(customerView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");

    }

    private void addCustomer(Scanner scanner) {
        System.out.print("customer name: ");
        String customerName = scanner.next();

        final Optional<Customer> customer = customerManager.addCustomer(customerName);
        if (customer.isPresent()) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }

    }

    private void updateCustomer(Scanner scanner) {
        System.out.println("what customer do you want to update?");

        System.out.print("customer ID: ");
        int id = scanner.nextInt();

        System.out.print("customer name: ");
        String name = scanner.next();

        System.out.print("total sum: ");
        BigDecimal totalSum = new BigDecimal(scanner.next().trim());

        System.out.print("card number ID: ");
        int cardId = scanner.nextInt();

        if (customerManager.updateCustomer(id, name, totalSum, cardId)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void deleteCustomer(Scanner scanner) {
        System.out.println("what customer do you want to delete?");
        System.out.print("id: ");
        int id = scanner.nextInt();

        if (customerManager.deleteCustomer(id)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }
}
