package model;

public abstract class DAOFactory {

    private static DAO<Vendor> vendorDAO;
    private static DAO<Country> countryDAO;
    private static DAO<DiscountCard> discountCardDAO;
    private static DAO<Watch> watchDAO;
    private static DAO<Customer> customerDAO;
    private static DAO<Order> orderDAO;
    private static DAO<Item> itemsDAO;

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
            vendorDAO = new VendorDAO();
        }
        return vendorDAO;
    }

    public static DAO<DiscountCard> getDiscountCardDAO() {
        if (discountCardDAO == null) {
            discountCardDAO = new DiscountCardDAO();
        }

        return discountCardDAO;
    }

    public static DAO<Watch> getWatchDAO() {
        if (watchDAO == null) {
            watchDAO = new WatchDAO();
        }
        return watchDAO;
    }

    public static DAO<Customer> getCustomerDAO() {
        if (customerDAO == null) {
            customerDAO = new CustomerDAO();
        }

        return customerDAO;
    }

    public static DAO<Order> getOrdersDAO() {
        if (orderDAO == null) {
            orderDAO = new OrderDAO();
        }

        return orderDAO;
    }

    public static DAO<Item> getItemsDAO() {
        if (itemsDAO == null) {
            itemsDAO = new ItemDAO<>();
        }

        return itemsDAO;
    }
}
