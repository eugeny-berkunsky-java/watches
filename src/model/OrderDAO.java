package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
