package model;

public abstract class DAOFactory {

    private static DAO<Vendor> vendorDAO;
    private static DAO<Country> countryDAO;

    private DAOFactory() {
    }

    public static DAO<Country> getCountriesDAO() {
        if (countryDAO == null) {
            countryDAO = new CountryDAO();
        }
        return countryDAO;
    }

    public static DAO<Vendor> getVendorsDAO() {
        if (vendorDAO == null) {
            vendorDAO = new VendorDAO(getCountriesDAO());
        }
        return vendorDAO;
    }
}
