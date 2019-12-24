package model;

import java.util.Objects;

public class Vendor {
    private int id;
    private String vendorName;
    private Country country;

    public Vendor(int id, String vendorName, Country country) {
        this.id = id;
        this.vendorName = vendorName;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vendor vendor = (Vendor) o;
        return id == vendor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "vendorName='" + vendorName + '\'' +
                ", country=" + country +
                '}';
    }
}
