package manage;

import model.*;
import utils.DBException;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        // calculate total order price include taxes and discount
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Item item : order.getItems()) {
            totalPrice =
                    totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQty())));
        }
        final BigDecimal taxes = BigDecimal.valueOf(16);

        final BigDecimal discount = order.getCustomer().getDiscountCard().getPercent();
        final BigDecimal finalPrice = totalPrice
                .add(calcPercent(totalPrice, taxes))
                .subtract(calcPercent(totalPrice, discount));

        try {
            // add new Order to OrderModel
            final Order savedOrder = ordersDAO.create(order);

            // add all items to ItemModel
            for (Item item : order.getItems()) {
                item.setOrderId(savedOrder.getId());
                itemsDAO.create(item);
            }

            //update order total price
            order.setTotalPrice(finalPrice);
            ordersDAO.update(order);

            // update customer total sum of orders
            customerDAO.update(order.getCustomer());

            return Optional.of(order);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "create order error", e);
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


    private BigDecimal calcPercent(BigDecimal value, BigDecimal percent) {
        if (value == null || percent == null) {
            return BigDecimal.ZERO;
        }
        return value.multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
    }
}
