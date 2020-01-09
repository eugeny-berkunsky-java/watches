package manage;

import model.Customer;
import model.DAO;
import model.DAOContainer;
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
    private static Logger logger = Logger.getLogger(CustomerManager.class.getName());

    private DAO<Customer> dao;

    public CustomerManager(DAOContainer container) {
        this.dao = container.getCustomerDAO();
    }

    public List<Customer> getAll() {
        try {
            final List<Customer> result = dao.getAll();
            return result == null ? Collections.emptyList() : result;
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "getAll error", e);
        }

        return Collections.emptyList();
    }

    public Optional<Customer> addCustomer(String name) {
        if (name == null || name.trim().length() == 0) {
            return Optional.empty();
        }

        try {
            final Customer customer = new Customer(-1, name.trim(), BigDecimal.ZERO,
                    new DiscountCard(0, null, BigDecimal.ZERO));
            final Customer result = dao.create(customer);
            return result == null
                    || result.getId() == -1
                    || result.getDiscountCard() == null
                    || result.getDiscountCard().getId() == -1 ?
                    Optional.empty() :
                    Optional.of(result);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "add customer error", e);
        }

        return Optional.empty();
    }

    public boolean updateCustomer(int id, String name, BigDecimal sum, int cardId) {
        if (id == -1
                || name == null
                || name.trim().length() == 0
                || sum == null
                || cardId == -1
        ) {
            return false;
        }

        final Customer customer = new Customer(id, name, sum,
                new DiscountCard(cardId, null, BigDecimal.ZERO));

        try {
            customer.setName(customer.getName().trim());
            return dao.update(customer);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "update customer error", e);
        }

        return false;
    }

    public boolean deleteCustomer(int id) {
        try {
            return id != -1 && dao.delete(id);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "delete customer error", e);
        }

        return false;
    }

    public Optional<Customer> getById(int customerId) {
        if (customerId == -1) {
            return Optional.empty();
        }

        try {
            final Customer customer = dao.getById(customerId);
            return customer == null ? Optional.empty() : Optional.of(customer);
        } catch (SQLException | DBException e) {
            logger.log(Level.SEVERE, "getById error", e);
        }

        return Optional.empty();
    }
}
