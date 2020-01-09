package manage;

import model.*;
import utils.DBException;
import utils.Settings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdersManager {

    private static Logger logger = Logger.getLogger(OrdersManager.class.getName());

    private DAO<Order> ordersDAO;
    private DAO<Item> itemsDAO;
    private DAO<Customer> customerDAO;

    public OrdersManager(DAOContainer container) {
        ordersDAO = container.getOrdersDAO();
        itemsDAO = container.getItemsDAO();
        customerDAO = container.getCustomerDAO();
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

    public Optional<Order> addOrder(Order order) {
        if (order == null
                || order.getCustomer() == null || order.getCustomer().getId() == -1
                || order.getItems() == null || order.getItems().size() == 0) {
            return Optional.empty();
        }

        BigDecimal finalPrice = calcFinalPrice(order);

        try {
            Settings.getConnection().setAutoCommit(false);

            // add new Order to OrderModel
            order.setTotalPrice(finalPrice);
            final Order savedOrder = ordersDAO.create(order);

            // add all items to ItemModel
            for (Item item : order.getItems()) {
                if (item.getPrice() == null
                        || item.getPrice().compareTo(BigDecimal.ZERO) <= 0
                        || item.getQty() <= 0
                ) {
                    Settings.getConnection().rollback();
                    return Optional.empty();
                }

                item.setOrderId(savedOrder.getId());
                itemsDAO.create(item);
            }

            // update customer total sum of orders
            order.getCustomer().setSumOfOrders(order.getCustomer().getSumOfOrders().add(finalPrice));
            customerDAO.update(order.getCustomer());

            Settings.getConnection().commit();

            return Optional.of(order);

        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "create order error", e);
            try {
                Settings.getConnection().rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "rollback transaction error", e);
            }
        } finally {
            try {
                Settings.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "set autocommit mode on error", e);
            }
        }

        return Optional.empty();
    }

    public boolean deleteOrder(int orderId) {
        try {
            return orderId != -1 && ordersDAO.delete(orderId);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete order error", e);
        }

        return false;
    }

    private BigDecimal calcFinalPrice(Order order) {
        // calculate total order price include taxes and discount
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Item item : order.getItems()) {
            totalPrice =
                    totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQty())));
        }
        final BigDecimal taxes = BigDecimal.valueOf(16);

        final BigDecimal discount = order.getCustomer().getDiscountCard().getPercent();
        return totalPrice
                .add(calcPercent(totalPrice, taxes))
                .subtract(calcPercent(totalPrice, discount));
    }

    private BigDecimal calcPercent(BigDecimal value, BigDecimal percent) {
        if (value == null || percent == null) {
            return BigDecimal.ZERO;
        }
        return value.multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
    }
}
