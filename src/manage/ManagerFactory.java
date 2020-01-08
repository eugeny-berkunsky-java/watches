package manage;

import model.DAOFactory;

public class ManagerFactory {

    private static ManagerFactory instance;
    private DAOFactory daoFactory;
    private CountriesManager countriesManager;
    private CustomerManager customerManager;
    private OrdersManager ordersManager;
    private VendorManager vendorManager;
    private WatchManager watchManager;

    private ManagerFactory() {
        daoFactory = DAOFactory.getInstance();
    }

    public static ManagerFactory getInstance() {
        if (instance == null) {
            instance = new ManagerFactory();
        }
        return instance;
    }

    public CountriesManager getCountriesManager() {
        if (countriesManager == null) {
            countriesManager = new CountriesManager(daoFactory);
        }
        return countriesManager;
    }

    public CustomerManager getCustomerManager() {
        if (customerManager == null) {
            customerManager = new CustomerManager(daoFactory);
        }
        return customerManager;
    }

    public OrdersManager getOrdersManager() {
        if (ordersManager == null) {
            ordersManager = new OrdersManager(daoFactory);
        }
        return ordersManager;
    }

    public VendorManager getVendorManager() {
        if (vendorManager == null) {
            vendorManager = new VendorManager(daoFactory);
        }
        return vendorManager;
    }

    public WatchManager getWatchManager() {
        if (watchManager == null) {
            watchManager = new WatchManager(daoFactory);
        }
        return watchManager;
    }
}
