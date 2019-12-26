package model;

import java.math.BigDecimal;
import java.util.Objects;

public class DiscountCard {
    private int id;
    private String number;
    private BigDecimal percent;

    public DiscountCard(int id, String number, BigDecimal percent) {
        this.id = id;
        this.number = number;
        this.percent = percent;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountCard that = (DiscountCard) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DiscountCard{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", percent=" + percent +
                '}';
    }
}
