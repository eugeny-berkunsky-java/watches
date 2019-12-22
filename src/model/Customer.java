package model;

public class Customer {
    int cardNumber;
    String name;
    Double sumOfOrders;

    public Customer(int cardNumber, String name) {
        this.cardNumber = cardNumber;
        this.name = name;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSumOfOrders() {
        return sumOfOrders;
    }

    public void setSumOfOrders(Double sumOfOrders) {
        this.sumOfOrders = sumOfOrders;
    }
}
