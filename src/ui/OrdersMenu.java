package ui;

import manage.OrdersManager;
import model.Item;
import model.Order;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class OrdersMenu {

    private final static int SHOW_ALL_ORDERS = 1;
    private final static int SHOW_ORDER_DETAILS = 2;
    private final static int ADD_NEW_ORDER = 3;
    private final static int DELETE_ORDER = 4;

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
        System.out.println("1. Show all orders");
        System.out.println("2. Show order details");
        System.out.println("3. Add new order");
        System.out.println("4. Delete order");
        System.out.println("0. return to main menu");
    }

    public void show(Scanner scanner) {
        int answer;

        do {
            printMenu();
            answer = scanner.nextInt();

            switch (answer) {
                case SHOW_ALL_ORDERS: {
                    showAllOrders();
                    break;
                }

                case SHOW_ORDER_DETAILS: {
                    showOrderDetails(scanner);
                    break;
                }

                case ADD_NEW_ORDER: {
                    addNewOrderMenu.show(scanner);
                    break;
                }

                case DELETE_ORDER: {
                    deleteOrder(scanner);
                    break;
                }
            }

        } while (answer != 0);
    }

    private void showAllOrders() {
        Function<Order, String> orderView = o -> String.format("%3d - %s - %s [%s]",
                o.getId(), o.getDate().format(dateFormatter), o.getCustomer().getName(),
                o.getTotalPrice());

        System.out.println("----------------------- Orders ----------------------");
        ordersManager.getAll().stream().map(orderView).forEach(System.out::println);
        System.out.println("-----------------------------------------------------");
    }

    private void showOrderDetails(Scanner scanner) {
        System.out.print("order ID: ");
        int orderId = scanner.nextInt();

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

    private void deleteOrder(Scanner scanner) {
        System.out.print("order ID: ");
        int orderId = scanner.nextInt();

        if (ordersManager.deleteOrder(orderId)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }
}
