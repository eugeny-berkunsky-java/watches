package com.company.watches.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Item {
    private int id;
    private Watch watch;
    private BigDecimal price;
    private int qty;
    private int orderId;


    public Item() {
        id = -1;
        price = BigDecimal.ZERO;
        qty = 1;
        orderId = -1;
    }

    public Item(int id, BigDecimal price, int qty, int orderId, Watch watch) {
        this.id = id;
        this.watch = watch;
        this.price = price;
        this.qty = qty;
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public Watch getWatch() {
        return watch;
    }

    public void setWatch(Watch watch) {
        this.watch = watch;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", watch=" + watch +
                ", price=" + price +
                ", qty=" + qty +
                ", orderId=" + orderId +
                '}';
    }
}
