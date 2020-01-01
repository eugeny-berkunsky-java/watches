package ui;

import manage.OrdersManager;
import model.Item;
import model.Order;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class NewOrderMenu {

    private final static int VIEW_ITEMS = 1;
    private final static int ADD_ITEM_TO_ORDER = 2;
    private final static int UPDATE_ITEM = 3;
    private final static int REMOVE_ITEM = 4;
    private final static int COMMIT_ORDER = 5;
    private final static int RETURN_BACK = 0;

    private final OrdersManager ordersManager;

    public NewOrderMenu() {
        ordersManager = new OrdersManager();
    }

    private void printMenu() {
        System.out.println("---------- Create new order ----------");
        System.out.println("1. view items");
        System.out.println("2. add item to order");
        System.out.println("3. update item");
        System.out.println("4. remove item from order");
        System.out.println("5. complete order and return to previous menu");
        System.out.println("0. cancel order and return to previous menu");
    }

    public void show(Scanner scanner) {
        int answer;


        System.out.print("set user ID: ");
        int userId = scanner.nextInt();
        Optional<Order> order = ordersManager.addOrder(LocalDateTime.now(), userId);

        if (!order.isPresent()) {
            System.err.println("cant't create new order");
            return;
        }

        do {
            printMenu();
            answer = scanner.nextInt();

            switch (answer) {
                case VIEW_ITEMS: {
                    viewItems(order.get());
                    break;
                }
                case ADD_ITEM_TO_ORDER: {
                    addItemToOrder(order.get(), scanner);
                    break;
                }
                case UPDATE_ITEM: {
                    updateItem(order.get(), scanner);
                    break;
                }

                case REMOVE_ITEM: {
                    removeItem(order.get(), scanner);
                    break;
                }
            }

        } while (answer != RETURN_BACK && answer != COMMIT_ORDER);

        if (answer == COMMIT_ORDER) {
            commitOrder(order.get());
        } else {
            removeOrder(order.get());
        }
    }

    private void viewItems(Order order) {

        final Optional<Order> updatedOrder = ordersManager.getById(order.getId());

        Function<Item, String> itemsView = i -> String.format("%3d - [%s] - %d - %.2f",
                i.getId(), i.getWatch().getBrand(), i.getQty(), i.getPrice());

        updatedOrder.ifPresent(o -> {
            System.out.println("------------------ Items ------------------");
            o.getItems().stream().map(itemsView).forEach(System.out::println);
            System.out.println("-------------------------------------------");
        });
    }

    private void addItemToOrder(Order order, Scanner scanner) {
        System.out.print("watch ID: ");
        final int watchId = scanner.nextInt();

        System.out.print("watch qty: ");
        final int qty = scanner.nextInt();

        System.out.print("watch price: ");
        final BigDecimal price = new BigDecimal(scanner.next().trim());

        final Optional<Item> item = ordersManager.addItem(order, watchId, qty, price);

        if (item.isPresent()) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void updateItem(Order order, Scanner scanner) {
        System.out.print("item ID: ");
        final int itemId = scanner.nextInt();

        System.out.print("item qty: ");
        final int itemQty = scanner.nextInt();

        System.out.print("item price: ");
        final BigDecimal itemPrice = new BigDecimal(scanner.next().trim());

        if (ordersManager.updateItem(order, itemId, itemPrice, itemQty)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void removeItem(Order order, Scanner scanner) {
        System.out.print("item ID: ");
        final int itemId = scanner.nextInt();

        final Item item = new Item(itemId, BigDecimal.ZERO, 1, order.getId(), null);
        if (ordersManager.deleteItem(item)) {
            System.out.println("deleted successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void commitOrder(Order order) {
        final Optional<Order> updatedOrder = ordersManager.getById(order.getId());
        if (updatedOrder.isPresent()) {
            final BigDecimal tax = BigDecimal.valueOf(16);
            final BigDecimal discount =
                    updatedOrder.get().getCustomer().getDiscountCard().getPercent();
            final BigDecimal totalPrice = updatedOrder.get().getTotalPrice();
            final BigDecimal finalPrice = totalPrice
                    .add(calcPercent(totalPrice, tax))
                    .subtract(calcPercent(totalPrice, discount));

            if (ordersManager.updateOrder(updatedOrder.get().getId(),
                    updatedOrder.get().getCustomer().getId(),
                    LocalDateTime.now(),
                    finalPrice)) {
                System.out.println("order completed");
            } else {
                System.out.println("operation failed");
            }
        } else {
            System.out.println("operation failed");
        }
    }

    private void removeOrder(Order order) {
        final Optional<Order> updatedOrder = ordersManager.getById(order.getId());

        updatedOrder.ifPresent(o -> {
            o.getItems().forEach(ordersManager::deleteItem);
            ordersManager.deleteOrder(o.getId());
        });
    }

    private BigDecimal calcPercent(BigDecimal value, BigDecimal percent) {
        if (value == null || percent == null) {
            return BigDecimal.ZERO;
        }
        return value.multiply(percent).divide(BigDecimal.valueOf(100), new MathContext(2));
    }
}
