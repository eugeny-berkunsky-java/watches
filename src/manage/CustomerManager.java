package manage;

import model.Customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerManager {
    private List<Customer> customers = new ArrayList<>(
            Arrays.asList(
                    new Customer(1235, "Tom"),
                    new Customer (8624, "Jack"),
                    new Customer(8203, "Bill"),
                    new Customer(7403, "Mary")));
    public void showCustomers() {
        System.out.println("-----------------------Customers----------------------");
        for (Customer customer : customers) {
            System.out.println("card number: "+customer.getCardNumber() + " " + "Name: "+ customer.getName() + " "+ "Summarising order: "+customer.getSumOfOrders());
        }
        System.out.println("------------------------------------------------------");
    }
}
