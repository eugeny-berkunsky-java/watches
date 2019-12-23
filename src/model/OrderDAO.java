package model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.Watch.WatchType.ANALOGUE;
import static model.Watch.WatchType.DIGITAL;

public class OrderDAO {
    private Map<Integer, Order> orders;

    private int id;

    public OrderDAO() {
        orders = new HashMap<>();
        id = 0;
    }

    private int getNextId() {
        return ++id;
    }

    public void generateOrders() {
        Customer customer = new Customer(1235, "Tom");
        Vendor vendor = new Vendor("Cartier", new Country(1, "Italy"));
        Watch watch1 = new Watch("Model 1", ANALOGUE, new BigDecimal("10.00"), 10, vendor);
        Watch watch2 = new Watch("Model 2", DIGITAL, new BigDecimal("10.00"), 10, vendor);
        Watch watch3 = new Watch("Model 3", ANALOGUE, new BigDecimal("10.00"), 10, vendor);
        DiscountCard card = new DiscountCard();

        Order.OrderBuilder builder = new Order.OrderBuilder();

        Order order = builder.newOrder(customer).addItem(watch1).build(card);
        add(order);

        order = builder.newOrder(customer).addItem(watch1).addItem(watch2).build(card);
        add(order);

        order = builder.newOrder(customer).addItem(watch3, new BigDecimal("19.99")).build(card);
        add(order);

    }

    public List<Order> getAll() {
        return new ArrayList<>(orders.values());
    }

    void add(Order order) {
        if (order.getId() == -1) {
            order.setId(getNextId());
        }

        orders.putIfAbsent(order.getId(), order);
    }
}
