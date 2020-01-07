package model;

public class DAOFactory {

    private static DAO<DiscountCard> discountCardDAO;
    private static DAOFactory factory;
    private DAO<Order> orderDAO;
    private DAO<Item> itemsDAO;
    private DAO<Watch> watchDAO;
    private DAO<Customer> customerDAO;
    private DAO<Vendor> vendorDAO;
    private DAO<Country> countryDAO;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        if (factory == null) {
            factory = new DAOFactory();
        }
        return factory;
    }

    public static DAO<DiscountCard> getDiscountCardDAO() {
        if (discountCardDAO == null) {
            discountCardDAO = new DiscountCardDAO();
        }

        return discountCardDAO;
    }

    public DAO<Order> getOrdersDAO() {
        if (orderDAO == null) {
            orderDAO = new OrderDAO();
        }

        return orderDAO;
    }

    public DAO<Item> getItemsDAO() {
        if (itemsDAO == null) {
            itemsDAO = new ItemDAO();
        }

        return itemsDAO;
    }

    public DAO<Watch> getWatchDAO() {
        if (watchDAO == null) {
            watchDAO = new WatchDAO();
        }
        return watchDAO;
    }

    public DAO<Customer> getCustomerDAO() {
        if (customerDAO == null) {
            customerDAO = new CustomerDAO();
        }

        return customerDAO;
    }

    public DAO<Vendor> getVendorsDAO() {
        if (vendorDAO == null) {
            vendorDAO = new VendorDAO();
        }
        return vendorDAO;
    }

    public DAO<Country> getCountriesDAO() {
        if (countryDAO == null) {
            countryDAO = new CountryDAO();
        }
        return countryDAO;
    }
}
