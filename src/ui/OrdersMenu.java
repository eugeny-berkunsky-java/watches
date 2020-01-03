package ui;

import manage.OrdersManager;
import model.Item;
import model.Order;
import utils.UserInput;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

public class OrdersMenu {

    private final static int SHOW_ALL_ORDERS = 1;
    private final static int SHOW_ORDER_DETAILS = 2;
    private final static int ADD_NEW_ORDER = 3;
    private final static int DELETE_ORDER = 4;
    private final static int PREVIOUS_MENU = 0;

    private final OrdersManager ordersManager;
    private final NewOrderMenu addNewOrderMenu;
    private final DateTimeFormatter dateFormatter;

    public OrdersMenu() {
        ordersManager = new OrdersManager();
        addNewOrderMenu = new NewOrderMenu();
        dateFormatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss");
    }

    private void printMenu() {
        System.out.println("---------- Orders menu ----------");
        System.out.format("%d. Show all orders%n", SHOW_ALL_ORDERS);
        System.out.format("%d. Show order details%n", SHOW_ORDER_DETAILS);
        System.out.format("%d. Add new order%n", ADD_NEW_ORDER);
        System.out.format("%d. Delete order%n", DELETE_ORDER);
        System.out.format("%d. return to main menu%n", PREVIOUS_MENU);
    }

    public void show(UserInput userInput) {
        int answer;

        do {
            printMenu();
            answer = userInput.getNumber("your choice", -1);

            switch (answer) {
                case SHOW_ALL_ORDERS: {
                    showAllOrders();
                    break;
                }

                case SHOW_ORDER_DETAILS: {
                    showOrderDetails(userInput);
                    break;
                }

                case ADD_NEW_ORDER: {
                    addNewOrderMenu.show(userInput);
                    break;
                }

                case DELETE_ORDER: {
                    deleteOrder(userInput);
                    break;
                }
            }

        } while (answer != PREVIOUS_MENU);
    }

    private void showAllOrders() {
        Function<Order, String> orderView = o -> String.format("%3d - %s - %s [%s]",
                o.getId(), o.getDate().format(dateFormatter), o.getCustomer().getName(),
                o.getTotalPrice());

        System.out.println("----------------------- Orders ----------------------");
        ordersManager.getAll().stream().map(orderView).forEach(System.out::println);
        System.out.println("-----------------------------------------------------");
    }

    private void showOrderDetails(UserInput userInput) {
        int orderId = userInput.getNumber("order ID", -1);

        Optional<Order> order = ordersManager.getById(orderId);

        Function<Item, String> itemsView = i -> String.format("%3d - [%s] - %d - %.2f",
                i.getId(), i.getWatch().getBrand(), i.getQty(), i.getPrice());

        order.ifPresent(o -> {
            System.out.format("------------------ Order %d details ------------------%n",
                    o.getId());
            o.getItems().stream().map(itemsView).forEach(System.out::println);
            System.out.println("-----------------------------------------------------");
        });
    }

    private void deleteOrder(UserInput userInput) {
        int orderId = userInput.getNumber("order ID", -1);

        if (ordersManager.deleteOrder(orderId)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }
}
