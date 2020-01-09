package ui;

import manage.CustomerManager;
import manage.ManagersContainer;
import model.Customer;
import utils.UserInput;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

public class CustomersMenu {
    private final static int SHOW_ALL_CUSTOMERS = 1;
    private final static int ADD_CUSTOMER = 2;
    private final static int UPDATE_CUSTOMER = 3;
    private final static int DELETE_CUSTOMER = 4;
    private final static int PREVIOUS_MENU = 0;

    private CustomerManager customerManager;

    public CustomersMenu(ManagersContainer managersContainer) {
        this.customerManager = managersContainer.getCustomerManager();
    }

    private void printMenu() {
        System.out.println("---------- Customers menu ----------");
        System.out.format("%d. Show all customers%n", SHOW_ALL_CUSTOMERS);
        System.out.format("%d. Add customer%n", ADD_CUSTOMER);
        System.out.format("%d. Update customer%n", UPDATE_CUSTOMER);
        System.out.format("%d. Delete customer%n", DELETE_CUSTOMER);
        System.out.format("%d. return to main menu%n", PREVIOUS_MENU);
    }

    public void show(UserInput userInput) {
        int answer;

        do {
            printMenu();
            answer = userInput.getNumber("your choice", -1);

            switch (answer) {
                case SHOW_ALL_CUSTOMERS: {
                    showCustomers();
                    break;
                }
                case ADD_CUSTOMER: {
                    addCustomer(userInput);
                    break;
                }
                case UPDATE_CUSTOMER: {
                    updateCustomer(userInput);
                    break;
                }
                case DELETE_CUSTOMER: {
                    deleteCustomer(userInput);
                    break;
                }
            }
        } while (answer != PREVIOUS_MENU);
    }

    private void showCustomers() {

        Function<Customer, String> customerView = c -> String.format("%d - %s - %.02f",
                c.getId(), c.getName(), c.getSumOfOrders());

        System.out.println("---------------------- Customers ---------------------");
        customerManager.getAll().stream().map(customerView).forEach(System.out::println);
        System.out.println("------------------------------------------------------");

    }

    private void addCustomer(UserInput userInput) {
        String customerName = userInput.getString("customer name");

        final Optional<Customer> customer = customerManager.addCustomer(customerName);
        if (customer.isPresent()) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }

    }

    private void updateCustomer(UserInput userInput) {
        System.out.println("what customer do you want to update?");

        int id = userInput.getNumber("customer ID", -1);

        String name = userInput.getString("customer name");

        BigDecimal totalSum = new BigDecimal(userInput.getString("total sum"));

        int cardId = userInput.getNumber("card number ID", -1);

        if (customerManager.updateCustomer(id, name, totalSum, cardId)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void deleteCustomer(UserInput userInput) {
        System.out.println("what customer do you want to delete?");
        int id = userInput.getNumber("id", -1);

        if (customerManager.deleteCustomer(id)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }
}
