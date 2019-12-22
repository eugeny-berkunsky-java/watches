package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Watch {
    private String brand;
    private WatchType type;
    private BigDecimal price;
    private int quantity;
    private Vendor vendor;

    public Watch(String brand, WatchType type, BigDecimal price, int quantity, Vendor vendor) {
        this.brand = brand;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.vendor = vendor;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
        return brand.equals(watch.brand) &&
                type == watch.type &&
                vendor.equals(watch.vendor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, type, vendor);
    }

    @Override
    public String toString() {
        return "Watch{" +
                "brand='" + brand + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", quantity=" + quantity +
                ", vendor=" + vendor +
                '}';
    }

    public enum WatchType {
        ANALOGUE, DIGITAL
    }
}
