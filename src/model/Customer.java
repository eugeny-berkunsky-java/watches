package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Customer {
    private int id;
    private String name;
    private BigDecimal sumOfOrders;
    private DiscountCard discountCard;

    public Customer(int id, String name, BigDecimal sumOfOrders, DiscountCard discountCard) {
        this.id = id;
        this.name = name;
        this.sumOfOrders = sumOfOrders;
        this.discountCard = discountCard;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSumOfOrders() {
        return sumOfOrders;
    }

    public void setSumOfOrders(BigDecimal sumOfOrders) {
        this.sumOfOrders = sumOfOrders;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sumOfOrders=" + sumOfOrders +
                ", discountCard=" + discountCard +
                '}';
    }
}
