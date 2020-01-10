package manage;

import model.Customer;
import model.DAO;
import model.DAOContainer;
import model.DiscountCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import utils.DBException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerManagerTest {

    private CustomerManager manager;
    private DAO<Customer> dao;

    private static Stream<Arguments> argsProvider() {
        return Stream.of(
                Arguments.of(1, "", BigDecimal.ZERO, 1),
                Arguments.of(1, "  ", BigDecimal.ZERO, 1),
                ///
                Arguments.of(-1, null, null, -1),
                Arguments.of(-1, null, null, 1),
                Arguments.of(-1, null, BigDecimal.ZERO, -1),
                Arguments.of(-1, null, BigDecimal.ZERO, 1),
                //
                Arguments.of(-1, "abc", null, -1),
                Arguments.of(-1, "abc", null, 1),
                Arguments.of(-1, "abc", BigDecimal.ZERO, -1),
                Arguments.of(-1, "abc", BigDecimal.ZERO, 1),
                //
                Arguments.of(1, null, null, -1),
                Arguments.of(1, null, null, 1),
                Arguments.of(1, null, BigDecimal.ZERO, -1),
                Arguments.of(1, null, BigDecimal.ZERO, 1),
                //
                Arguments.of(1, "abc", null, -1),
                Arguments.of(1, "abc", null, 1),
                Arguments.of(1, "abc", BigDecimal.ZERO, -1)
        );
    }

    @BeforeEach
    void setUp() {
        dao = mock(DAO.class);
        final DAOContainer container = mock(DAOContainer.class);
        when(container.getCustomerDAO()).thenReturn(dao);

        manager = new CustomerManager(container);
    }

    @Test
    void getAllBelowTrashArgs() throws SQLException {
        when(dao.getAll()).thenReturn(null);

        assertEquals(0, manager.getAll().size());
        verify(dao, times(1)).getAll();
    }

    @Test
    void getAllBelowCorrectArgs() throws SQLException {
        final Customer customer = new Customer(1, null, BigDecimal.ZERO,
                new DiscountCard(1, null, BigDecimal.ZERO));

        when(dao.getAll()).thenReturn(Arrays.asList(customer, customer));
        assertEquals(2, manager.getAll().size());
        verify(dao, times(1)).getAll();
    }

    @Test
    void getAllCatchSQLException() throws SQLException {
        when(dao.getAll()).thenThrow(new SQLException());

        assertEquals(0, manager.getAll().size());
        verify(dao, times(1)).getAll();

    }

    @Test
    void getAllCatchDBException() throws SQLException {
        when(dao.getAll()).thenThrow(new DBException(new SQLException()));

        assertEquals(0, manager.getAll().size());
        verify(dao, times(1)).getAll();
    }

    @Test
    void addCustomerAboveTrashArgs() throws SQLException {
        assertFalse(manager.addCustomer(null).isPresent());
        verify(dao, times(0)).create(any());
    }

    @Test
    void addCustomerAboveCorrectArgs() throws SQLException {
        manager.addCustomer(" abc ");

        final ArgumentCaptor<Customer> args = ArgumentCaptor.forClass(Customer.class);
        verify(dao, times(1)).create(args.capture());
        final Customer value = args.getValue();
        assertEquals(-1, value.getId());
        assertEquals("abc", value.getName());
        assertEquals(0, value.getDiscountCard().getId());
    }

    /*
    @Test
    void addCustomerBelowTrashArgs() throws SQLException {
        when(dao.create(any(Customer.class))).thenReturn(null);
        assertFalse(manager.addCustomer("abc").isPresent());

        Customer customer = new Customer(-1, "", BigDecimal.ZERO, new DiscountCard(-1, "", BigDecimal.ZERO));
        when(dao.create(any(Customer.class))).thenReturn(customer);
        assertFalse(manager.addCustomer("abc").isPresent());

        customer = new Customer(1, "", BigDecimal.ZERO, new DiscountCard(-1, "", BigDecimal.ZERO));
        when(dao.create(any(Customer.class))).thenReturn(customer);
        assertFalse(manager.addCustomer("abc").isPresent());

        customer = new Customer(-1, "", BigDecimal.ZERO, new DiscountCard(1, "", BigDecimal.ZERO));
        when(dao.create(any(Customer.class))).thenReturn(customer);
        assertFalse(manager.addCustomer("abc").isPresent());

        customer = new Customer(1, "", BigDecimal.ZERO, new DiscountCard(1, "", BigDecimal.ZERO));
        when(dao.create(any(Customer.class))).thenReturn(customer);
        assertTrue(manager.addCustomer("abc").isPresent());
    }

    @Test
    void addCustomerBelowCorrectArgs() throws SQLException {
        Customer customer = new Customer(1, "", BigDecimal.ZERO,
                new DiscountCard(1, "", BigDecimal.ZERO));

        when(dao.create(any(Customer.class))).thenReturn(customer);
        assertTrue(manager.addCustomer("abc").isPresent());
    }
*/
    @Test
    void addCustomerCatchException() throws SQLException {
        when(dao.create(any(Customer.class))).thenThrow(new SQLException(),
                new DBException(new Exception()));

        assertFalse(manager.addCustomer(" abc ").isPresent());
        assertFalse(manager.addCustomer(" abc ").isPresent());
    }

    @ParameterizedTest
    @MethodSource("argsProvider")
    void updateCustomerAboveTrashArgs(int id, String name, BigDecimal sum, int cardId) throws SQLException {
        assertFalse(manager.updateCustomer(id, name, sum, cardId));
        verify(dao, never()).update(any(Customer.class));
    }

    @Test
    void updateCustomerAboveCorrectArgs() throws SQLException {
        when(dao.update(any(Customer.class))).thenReturn(true);

        assertTrue(manager.updateCustomer(1, " abc ", BigDecimal.ZERO, 2));
        verify(dao, times(1)).update(any(Customer.class));

        final ArgumentCaptor<Customer> args = ArgumentCaptor.forClass(Customer.class);
        verify(dao).update(args.capture());
        final Customer value = args.getValue();
        assertEquals(1, value.getId());
        assertEquals("abc", value.getName());
        assertEquals(2, value.getDiscountCard().getId());
    }

    @Test
    void updateCustomerBelowArgs() throws SQLException {
        when(dao.update(any(Customer.class))).thenReturn(true, false);

        assertTrue(manager.updateCustomer(1, "abc", BigDecimal.ZERO, 1));
        verify(dao, times(1)).update(any(Customer.class));

        assertFalse(manager.updateCustomer(1, "abc", BigDecimal.ZERO, 1));
        verify(dao, times(2)).update(any(Customer.class));
    }

    @Test
    void updateCustomerCatchException() throws SQLException {
        when(dao.update(any(Customer.class))).thenThrow(new SQLException(),
                new DBException(new Exception()));

        assertFalse(manager.updateCustomer(1, "abc", BigDecimal.ZERO, 1));
        assertFalse(manager.updateCustomer(1, "abc", BigDecimal.ZERO, 1));
    }

    @Test
    void deleteCustomerAboveTrashArgs() throws SQLException {
        assertFalse(manager.deleteCustomer(-1));
        verify(dao, never()).delete(eq(-1));
    }

    @Test
    void deleteCustomerAboveCorrectArgs() throws SQLException {
        when(dao.delete(eq(10))).thenReturn(true);

        assertTrue(manager.deleteCustomer(10));
        verify(dao, times(1)).delete(eq(10));
    }

    @Test
    void deleteCustomerBelowArgs() throws SQLException {
        when(dao.delete(anyInt())).thenReturn(true, false);

        assertTrue(manager.deleteCustomer(10));
        verify(dao, times(1)).delete(anyInt());

        assertFalse(manager.deleteCustomer(10));
        verify(dao, times(2)).delete(anyInt());
    }

    @Test
    void deleteCustomerCatchExceptions() throws SQLException {
        when(dao.delete(anyInt())).thenThrow(new SQLException(), new DBException(new Exception()));

        assertFalse(manager.deleteCustomer(10));
        verify(dao, times(1)).delete(anyInt());

        assertFalse(manager.deleteCustomer(20));
        verify(dao, times(2)).delete(anyInt());

    }
}