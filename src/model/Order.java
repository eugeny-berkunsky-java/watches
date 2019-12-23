package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private int id;
    private LocalDateTime date;
    private Customer customer;
    private List<Item> items;
    private BigDecimal totalPrice;

    private Order() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Item> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", customer=" + customer +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public static class OrderBuilder {

        private Order order;

        public OrderBuilder newOrder(Customer customer) {
            order = new Order();
            order.id = -1;
            order.customer = customer;
            order.items = new ArrayList<>();
            order.totalPrice = BigDecimal.ZERO;

            return this;
        }

        OrderBuilder addItem(Watch watch) {
            return addItem(watch, watch.getPrice(), 1);
        }

        OrderBuilder addItem(Watch watch, BigDecimal price) {
            return addItem(watch, price, 1);
        }

        OrderBuilder addItem(Watch watch, BigDecimal price, int qty) {

            Item item = new Item(watch, price, qty);
            order.totalPrice = order.totalPrice.add(item.price.multiply(new BigDecimal(item.qty)));
            order.items.add(item);

            return this;
        }

        public Order build(DiscountCard card) {
            order.date = LocalDateTime.now();
            //todo: calculate discount

            Order result = this.order;
            this.order = null;

            return result;
        }
    }

    private static class Item {
        private Watch watch;
        private BigDecimal price;
        private int qty;

        public Item(Watch watch, BigDecimal price, int qty) {
            this.watch = watch;
            this.price = price;
            this.qty = qty;
        }
    }
}
