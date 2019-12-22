package model;

public class Vendor {
    String vendorName;
    Country country;

    public Vendor(String vendorName, Country country) {
        this.vendorName = vendorName;
        this.country = country;
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
    public String toString() {
        return "Vendor{" +
                "vendorName='" + vendorName + '\'' +
                ", country=" + country +
                '}';
    }
}
