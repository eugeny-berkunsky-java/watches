package manage;

import model.Customer;
import model.DAO;
import model.DAOFactory;
import model.DiscountCard;
import utils.DBException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerManager {
    private DAO<Customer> dao;
    private Logger logger;

    public CustomerManager() {
        this.dao = DAOFactory.getCustomerDAO();
        logger = Logger.getLogger(CustomerManager.class.getName());
    }

    public List<Customer> getAll() {
        try {
            return dao.getAll();
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "getAll error", e);
        }

        return Collections.emptyList();
    }

    public Optional<Customer> addCustomer(String name) {
        if (name == null) {
            return Optional.empty();
        }

        try {
            final Customer customer = new Customer(-1, name, BigDecimal.ZERO,
                    new DiscountCard(0, null, BigDecimal.ZERO));
            return Optional.of(dao.create(customer));
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "add customer error", e);
        }

        return Optional.empty();
    }

    public boolean updateCustomer(int id, String name, BigDecimal sum, int cardId) {
        if (name == null || sum == null) {
            return false;
        }

        final Customer customer = new Customer(id, name, sum,
                new DiscountCard(cardId, null, BigDecimal.ZERO));

        try {
            return dao.update(customer);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "update customer error", e);
        }

        return false;
    }

    public boolean deleteCustomer(int id) {
        try {
            return dao.delete(id);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete customer error", e);
        }

        return false;
    }
}
