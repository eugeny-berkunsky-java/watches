package ui;

import manage.OrdersManager;
import model.Item;
import model.Order;
import utils.UserInput;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

public class NewOrderMenu {

    private final static int VIEW_ITEMS = 1;
    private final static int ADD_ITEM_TO_ORDER = 2;
    private final static int UPDATE_ITEM = 3;
    private final static int REMOVE_ITEM = 4;
    private final static int COMMIT_ORDER = 5;
    private final static int PREVIOUS_MENU = 0;

    private final OrdersManager ordersManager;

    public NewOrderMenu() {
        ordersManager = new OrdersManager();
    }

    private void printMenu() {
        System.out.println("---------- Create new order ----------");
        System.out.format("%d. view items%n", VIEW_ITEMS);
        System.out.format("%d. add item to order%n", ADD_ITEM_TO_ORDER);
        System.out.format("%d. update item%n", UPDATE_ITEM);
        System.out.format("%d. remove item from order%n", REMOVE_ITEM);
        System.out.format("%d. complete order and return to previous menu%n", COMMIT_ORDER);
        System.out.format("%d. cancel order and return to previous menu%n", PREVIOUS_MENU);
    }

    public void show(UserInput userInput) {
        int answer;

        int userId = userInput.getNumber("set user ID", -1);
        Optional<Order> order = ordersManager.addOrder(LocalDateTime.now(), userId);

        if (!order.isPresent()) {
            System.err.println("cant't create new order");
            return;
        }

        do {
            printMenu();
            answer = userInput.getNumber("your choice", -1);

            switch (answer) {
                case VIEW_ITEMS: {
                    viewItems(order.get());
                    break;
                }
                case ADD_ITEM_TO_ORDER: {
                    addItemToOrder(order.get(), userInput);
                    break;
                }
                case UPDATE_ITEM: {
                    updateItem(order.get(), userInput);
                    break;
                }

                case REMOVE_ITEM: {
                    removeItem(order.get(), userInput);
                    break;
                }
            }

        } while (answer != PREVIOUS_MENU && answer != COMMIT_ORDER);

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

    private void addItemToOrder(Order order, UserInput userInput) {
        final int watchId = userInput.getNumber("watch ID", -1);

        final int qty = userInput.getNumber("watch qty", -1);

        final BigDecimal price = new BigDecimal(userInput.getString("watch price"));

        final Optional<Item> item = ordersManager.addItem(order, watchId, qty, price);

        if (item.isPresent()) {
            System.out.println("added successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void updateItem(Order order, UserInput userInput) {
        final int itemId = userInput.getNumber("item ID", -1);

        final int itemQty = userInput.getNumber("item qty", -1);

        final BigDecimal itemPrice = new BigDecimal(userInput.getString("item price"));

        if (ordersManager.updateItem(order, itemId, itemPrice, itemQty)) {
            System.out.println("updated successfully");
        } else {
            System.out.println("operation failed");
        }
    }

    private void removeItem(Order order, UserInput userInput) {
        final int itemId = userInput.getNumber("item ID", -1);

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
