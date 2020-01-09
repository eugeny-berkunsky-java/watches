package ui;

import manage.CustomerManager;
import manage.ManagersContainer;
import manage.OrdersManager;
import manage.WatchManager;
import model.Customer;
import model.Item;
import model.Order;
import model.Watch;
import utils.UserInput;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class NewOrderMenu {

    private final static int VIEW_ITEMS = 1;
    private final static int ADD_ITEM_TO_ORDER = 2;
    private final static int UPDATE_ITEM = 3;
    private final static int REMOVE_ITEM = 4;
    private final static int COMPLETE_ORDER = 5;
    private final static int PREVIOUS_MENU = 0;

    private final OrdersManager ordersManager;
    private final WatchManager watchesManager;
    private final CustomerManager customersManager;

    public NewOrderMenu(ManagersContainer container) {
        this.ordersManager = container.getOrdersManager();
        this.watchesManager = container.getWatchManager();
        this.customersManager = container.getCustomerManager();
    }

    private void printMenu() {
        System.out.println("---------- Create new order ----------");
        System.out.format("%d. view items%n", VIEW_ITEMS);
        System.out.format("%d. add item to order%n", ADD_ITEM_TO_ORDER);
        System.out.format("%d. update item%n", UPDATE_ITEM);
        System.out.format("%d. remove item from order%n", REMOVE_ITEM);
        System.out.format("%d. complete order and return to previous menu%n", COMPLETE_ORDER);
        System.out.format("%d. cancel order and return to previous menu%n", PREVIOUS_MENU);
    }

    public void show(UserInput userInput) {
        int answer;

        int customerId = userInput.getNumber("set customer ID", -1);
        Optional<Customer> customer = customersManager.getById(customerId);
        if (!customer.isPresent()) {
            System.out.println("operation failed");
            return;
        }

        Order order = new Order(-1, LocalDateTime.now(), customer.get(), new ArrayList<>(),
                BigDecimal.ZERO);

        do {
            printMenu();
            answer = userInput.getNumber("your choice", -1);

            switch (answer) {
                case VIEW_ITEMS: {
                    viewItems(order);
                    break;
                }
                case ADD_ITEM_TO_ORDER: {
                    addItemToOrder(order, userInput);
                    break;
                }
                case UPDATE_ITEM: {
                    updateItem(order, userInput);
                    break;
                }

                case REMOVE_ITEM: {
                    deleteItem(order, userInput);
                    break;
                }
            }

        } while (answer != PREVIOUS_MENU && answer != COMPLETE_ORDER);

        if (answer == COMPLETE_ORDER) {
            completeOrder(order);
        }
    }

    private void viewItems(Order order) {

        Function<Item, String> itemsView = i -> String.format("[%s] - %d - %.2f",
                i.getWatch().getBrand(), i.getQty(), i.getPrice());


        final List<Item> items = order.getItems();
        System.out.println("------------------ Items ------------------");
        for (int i = 0; i < items.size(); i++) {
            System.out.format("%d. %s%n", i + 1, itemsView.apply(items.get(i)));
        }
        System.out.println("-------------------------------------------");
    }

    private void addItemToOrder(Order order, UserInput userInput) {
        final int watchId = userInput.getNumber("watch ID", -1);
        final Optional<Watch> watch = watchesManager.getById(watchId);

        if (!watch.isPresent()) {
            System.out.println("operation failed");
            return;
        }

        final int qty = userInput.getNumber("watch qty", -1);

        final BigDecimal price = new BigDecimal(userInput.getString("watch price"));

        Item item = new Item();
        item.setQty(qty);
        item.setPrice(price);
        item.setWatch(watch.get());
        order.getItems().add(item);

        System.out.println("added successfully");
    }

    private void updateItem(Order order, UserInput userInput) {
        final int itemNumber = userInput.getNumber("item number", -1);

        final int itemQty = userInput.getNumber("item qty", -1);

        final BigDecimal itemPrice = new BigDecimal(userInput.getString("item price"));

        final Item item = order.getItems().get(itemNumber);
        item.setQty(itemQty);
        item.setPrice(itemPrice);

        order.getItems().set(itemNumber, item);

        System.out.println("updated successfully");
    }

    private void deleteItem(Order order, UserInput userInput) {
        final int itemNumber = userInput.getNumber("item number", -1);

        order.getItems().remove(itemNumber);

        System.out.println("deleted successfully");

    }

    private void completeOrder(Order order) {
        if (ordersManager.addOrder(order).isPresent()) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }
    }
}
