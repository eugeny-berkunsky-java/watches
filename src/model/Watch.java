package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Watch {
    private int id;
    private String brand;
    private WatchType type;
    private BigDecimal price;
    private int qty;
    private Vendor vendor;

    public Watch(int id, String brand, WatchType type, BigDecimal price, int qty, Vendor vendor) {
        this.id = id;
        this.brand = brand;
        this.type = type;
        this.price = price;
        this.qty = qty;
        this.vendor = vendor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public WatchType getType() {
        return type;
    }

    public void setType(WatchType type) {
        this.type = type;
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

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Watch watch = (Watch) o;
        return id == watch.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Watch{" +
                "brand='" + brand + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", quantity=" + qty +
                ", vendor=" + vendor +
                '}';
    }

    public enum WatchType {
        ANALOGUE, DIGITAL
    }
}
