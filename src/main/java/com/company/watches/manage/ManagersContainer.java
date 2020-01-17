package com.company.watches.manage;

import com.company.watches.dao.DAOContainer;

public class ManagersContainer {

    private static ManagersContainer instance;
    private DAOContainer container;
    private CountriesManager countriesManager;
    private CustomerManager customerManager;
    private OrdersManager ordersManager;
    private VendorManager vendorManager;
    private WatchManager watchManager;

    private ManagersContainer(DAOContainer container) {
        this.container = container;
    }

    public static ManagersContainer getInstance(DAOContainer container) {
        if (instance == null) {
            instance = new ManagersContainer(container);
        }
        return instance;
    }

    public CountriesManager getCountriesManager() {
        if (countriesManager == null) {
            countriesManager = new CountriesManager(container);
        }
        return countriesManager;
    }

    public CustomerManager getCustomerManager() {
        if (customerManager == null) {
            customerManager = new CustomerManager(container);
        }
        return customerManager;
    }

    public OrdersManager getOrdersManager() {
        if (ordersManager == null) {
            ordersManager = new OrdersManager(container);
        }
        return ordersManager;
    }

    public VendorManager getVendorManager() {
        if (vendorManager == null) {
            vendorManager = new VendorManager(container);
        }
        return vendorManager;
    }

    public WatchManager getWatchManager() {
        if (watchManager == null) {
            watchManager = new WatchManager(container);
        }
        return watchManager;
    }
}
