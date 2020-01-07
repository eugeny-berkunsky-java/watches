package manage;

import model.*;
import utils.DBException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdersManager {

    private static Logger logger = Logger.getLogger(OrdersManager.class.getName());

    private DAO<Order> ordersDAO;
    private DAO<Item> itemsDAO;

    public OrdersManager(DAOFactory factory) {
        ordersDAO = factory.getOrdersDAO();
        itemsDAO = factory.getItemsDAO();
    }

    public List<Order> getAll() {
        try {
            final List<Order> result = ordersDAO.getAll();
            return result == null ? Collections.emptyList() : result;
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "get all orders error", e);
        }

        return Collections.emptyList();
    }

    public Optional<Order> getById(int orderId) {
        if (orderId == -1) {
            return Optional.empty();
        }

        try {
            final Order result = ordersDAO.getById(orderId);
            return result == null ? Optional.empty() : Optional.of(result);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "get order by id error", e);
        }

        return Optional.empty();
    }

    public Optional<Order> addOrder(LocalDateTime date, int customerId) {
        if (date == null || customerId == -1) {
            return Optional.empty();
        }

        final Order order = new Order(-1, date,
                new Customer(customerId, null, BigDecimal.ZERO,
                        new DiscountCard(0, null, BigDecimal.ZERO)), null, BigDecimal.ZERO);

        try {
            final Order result = ordersDAO.create(order);
            return result == null ? Optional.empty() : Optional.of(result);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "add new order error", e);
        }

        return Optional.empty();
    }

    public boolean updateOrder(int orderId, int customerId, LocalDateTime date,
                               BigDecimal totalPrice) {
        if (date == null || totalPrice == null || orderId == -1 || customerId == -1) {
            return false;
        }

        final Customer customer = new Customer(customerId, null, BigDecimal.ZERO, null);

        final Order order = new Order(orderId, date, customer, Collections.emptyList(), totalPrice);

        try {
            return ordersDAO.update(order);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "update order error", e);
        }

        return false;
    }

    public boolean deleteOrder(int orderId) {
        try {
            return orderId != -1 && ordersDAO.delete(orderId);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete order error", e);
        }

        return false;
    }

    public Optional<Item> addItem(Order order, int watchId, int qty, BigDecimal price) {
        if (order == null || watchId == -1 || qty == 0 || price == null) {
            return Optional.empty();
        }

        final Watch watch = new Watch(watchId, null, null, BigDecimal.ZERO, 0, null);
        final Item item = new Item(-1, price, qty, order.getId(), watch);
        try {
            final Item result = itemsDAO.create(item);
            return result == null ? Optional.empty() : Optional.of(result);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "add item to order error", e);
        }

        return Optional.empty();
    }

    public boolean updateItem(Order order, int itemId, BigDecimal price, int qty) {
        if (order == null || itemId == -1 || price == null || qty < 0) {
            return false;
        }

        final Item item = new Item(itemId, price, qty, order.getId(), null);
        try {
            return itemsDAO.update(item);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "update order's item error", e);
        }

        return false;
    }

    public boolean deleteItem(Item item) {
        if (item == null || item.getId() == -1 || item.getOrderId() == -1) {
            return false;
        }

        try {
            final Item storedItem = itemsDAO.getById(item.getId());

            if (storedItem == null || storedItem.getOrderId() != item.getOrderId()) {
                return false;
            }

            return itemsDAO.delete(item.getId());
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete item error", e);
        }

        return false;
    }
}
