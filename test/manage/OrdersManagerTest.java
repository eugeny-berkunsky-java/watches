package manage;

import model.DAO;
import model.DAOContainer;
import model.Item;
import model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import utils.DBException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdersManagerTest {

    private OrdersManager manager;
    private DAO<Order> orderDAO;
    private DAO<Item> itemDAO;

    public static Stream<Arguments> updateOrderAboveTrashArgsProvider() {
        return Stream.of(
                Arguments.of(-1, -1, null, null),
                Arguments.of(-1, -1, null, BigDecimal.ONE),
                Arguments.of(-1, -1, LocalDateTime.now(), null),
                Arguments.of(-1, -1, LocalDateTime.now(), BigDecimal.ONE),

                Arguments.of(-1, 1, null, null),
                Arguments.of(-1, 1, null, BigDecimal.ONE),
                Arguments.of(-1, 1, LocalDateTime.now(), null),
                Arguments.of(-1, 1, LocalDateTime.now(), BigDecimal.ONE),

                //
                Arguments.of(1, -1, null, null),
                Arguments.of(1, -1, null, BigDecimal.ONE),
                Arguments.of(1, -1, LocalDateTime.now(), null),
                Arguments.of(1, -1, LocalDateTime.now(), BigDecimal.ONE),

                Arguments.of(1, 1, null, null),
                Arguments.of(1, 1, null, BigDecimal.ONE),
                Arguments.of(1, 1, LocalDateTime.now(), null)

        );
    }

    public static Stream<Arguments> addItemAboveTrashArgsProvider() {
        final Order order = mock(Order.class);

        return Stream.of(
                Arguments.of(null, -1, 0, null),
                Arguments.of(null, -1, 0, BigDecimal.ONE),
                Arguments.of(null, -1, 1, null),
                Arguments.of(null, -1, 1, BigDecimal.ONE),

                Arguments.of(null, 1, 0, null),
                Arguments.of(null, 1, 0, BigDecimal.ONE),
                Arguments.of(null, 1, 1, null),
                Arguments.of(null, 1, 1, BigDecimal.ONE),

                Arguments.of(order, -1, 0, null),
                Arguments.of(order, -1, 0, BigDecimal.ONE),
                Arguments.of(order, -1, 1, null),
                Arguments.of(order, -1, 1, BigDecimal.ONE),

                Arguments.of(order, 1, 0, null),
                Arguments.of(order, 1, 0, BigDecimal.ONE),
                Arguments.of(order, 1, 1, null)
        );
    }

    public static Stream<Arguments> updateItemAboveTrashArgsProvider() {
        final Order order = mock(Order.class);

        return Stream.of(
                Arguments.of(null, -1, null, -1),
                Arguments.of(null, -1, null, 1),
                Arguments.of(null, -1, BigDecimal.ONE, -1),
                Arguments.of(null, -1, BigDecimal.ONE, 1),

                Arguments.of(null, 1, null, -1),
                Arguments.of(null, 1, null, 1),
                Arguments.of(null, 1, BigDecimal.ONE, -1),
                Arguments.of(null, 1, BigDecimal.ONE, 1),

                Arguments.of(order, -1, null, -1),
                Arguments.of(order, -1, null, 1),
                Arguments.of(order, -1, BigDecimal.ONE, -1),
                Arguments.of(order, -1, BigDecimal.ONE, 1),

                Arguments.of(order, 1, null, -1),
                Arguments.of(order, 1, null, 1),
                Arguments.of(order, 1, BigDecimal.ONE, -1)
        );
    }

    @BeforeEach
    void init() {
        DAOContainer container = mock(DAOContainer.class);
        orderDAO = mock(DAO.class);
        itemDAO = mock(DAO.class);
        when(container.getOrdersDAO()).thenReturn(orderDAO);
        when(container.getItemsDAO()).thenReturn(itemDAO);

        manager = new OrdersManager(container);
    }

    @Test
    void getAllBelowTrashArgs() throws SQLException {
        when(orderDAO.getAll()).thenReturn(null);
        assertEquals(0, manager.getAll().size());
        verify(orderDAO, times(1)).getAll();
    }

    @Test
    void getAllBelowCorrectArgs() throws SQLException {
        final Order order = mock(Order.class);
        when(orderDAO.getAll()).thenReturn(Arrays.asList(order, order, order));

        assertEquals(3, manager.getAll().size());
        verify(orderDAO, times(1)).getAll();
    }

    @Test
    void getAllCatchExceptions() throws SQLException {
        when(orderDAO.getAll()).thenThrow(new SQLException(), new DBException(new Exception()));

        assertEquals(0, manager.getAll().size());
        verify(orderDAO, times(1)).getAll();

        assertEquals(0, manager.getAll().size());
        verify(orderDAO, times(2)).getAll();
    }

    ///
    @Test
    void getByIdAboveTrashArgs() throws SQLException {
        assertFalse(manager.getById(-1).isPresent());
        verify(orderDAO, never()).getById(eq(-1));
    }

    @Test
    void getByIdAboveCorrectArgs() throws SQLException {
        manager.getById(2);
        verify(orderDAO, times(1)).getById(eq(2));
    }

    @Test
    void getByIdBelowTrashArgs() throws SQLException {
        when(orderDAO.getById(anyInt())).thenReturn(null);
        assertFalse(manager.getById(10).isPresent());
    }

    @Test
    void getByIdBelowCorrectArgs() throws SQLException {
        when(orderDAO.getById(anyInt())).thenReturn(mock(Order.class));
        assertTrue(manager.getById(10).isPresent());
    }

    @Test
    void getByIdCatchExceptions() throws SQLException {
        when(orderDAO.getById(anyInt())).thenThrow(new SQLException(),
                new DBException(new Exception()));

        assertFalse(manager.getById(10).isPresent());
        verify(orderDAO, times(1)).getById(eq(10));

        assertFalse(manager.getById(10).isPresent());
        verify(orderDAO, times(2)).getById(eq(10));
    }

    ///
    /*
    @Test
    void addOrderAboveTrashArgs() throws SQLException {
        assertFalse(manager.addOrder(null, -1).isPresent());
        verify(orderDAO, never()).create(any(Order.class));

        assertFalse(manager.addOrder(null, 1).isPresent());
        verify(orderDAO, never()).create(any(Order.class));

        assertFalse(manager.addOrder(LocalDateTime.now(), -1).isPresent());
        verify(orderDAO, never()).create(any(Order.class));
    }
*/
    /*
    @Test
    void addOrderAboveCorrectArgs() throws SQLException {
        final LocalDateTime localDateTime = LocalDateTime.now();
        manager.addOrder(localDateTime, 10);

        final ArgumentCaptor<Order> arg = ArgumentCaptor.forClass(Order.class);
        verify(orderDAO, times(1)).create(arg.capture());
        final Order value = arg.getValue();
        assertEquals(-1, value.getId());
        assertEquals(10, value.getCustomer().getId());
        assertEquals(localDateTime, value.getDate());
    }
*/
    /*@Test
    void addOrderBelowTrashArgs() throws SQLException {
        when(orderDAO.create(any(Order.class))).thenReturn(null);
        assertFalse(manager.addOrder(LocalDateTime.now(), 10).isPresent());
        verify(orderDAO, times(1)).create(any(Order.class));
    }

    @Test
    void addOrderBelowCorrectArgs() throws SQLException {
        when(orderDAO.create(any(Order.class))).thenReturn(mock(Order.class));

        assertTrue(manager.addOrder(LocalDateTime.now(), 10).isPresent());
        verify(orderDAO, times(1)).create(any(Order.class));
    }

    @Test
    void addOrderCatchExceptions() throws SQLException {
        when(orderDAO.create(any(Order.class))).thenThrow(new SQLException(),
                new DBException(new Exception()));

        assertFalse(manager.addOrder(LocalDateTime.now(), 10).isPresent());
        verify(orderDAO, times(1)).create(any(Order.class));

        assertFalse(manager.addOrder(LocalDateTime.now(), 10).isPresent());
        verify(orderDAO, times(2)).create(any(Order.class));
    }
*/
    ///
   /* @ParameterizedTest
    @MethodSource("updateOrderAboveTrashArgsProvider")
    void updateOrderAboveTrashArgs(int orderId, int customerId, LocalDateTime date,
                                   BigDecimal totalPrice) throws SQLException {
        manager.updateOrder(orderId, customerId, date, totalPrice);
        verify(orderDAO, never()).update(any(Order.class));
    }
*/
   /* @Test
    void updateOrderAboveCorrectArgs() throws SQLException {
        LocalDateTime dateTime = LocalDateTime.now();
        manager.updateOrder(1, 2, dateTime, BigDecimal.ONE);
        final ArgumentCaptor<Order> arg = ArgumentCaptor.forClass(Order.class);

        verify(orderDAO, times(1)).update(arg.capture());
        final Order order = arg.getValue();
        assertEquals(1, order.getId());
        assertEquals(2, order.getCustomer().getId());
        assertEquals(dateTime, order.getDate());
        assertEquals(BigDecimal.ONE, order.getTotalPrice());
    }

    @Test
    void updateOrderBelowArgs() throws SQLException {
        when(orderDAO.update(any(Order.class))).thenReturn(true, false);
        assertTrue(manager.updateOrder(1, 1, LocalDateTime.now(), BigDecimal.ONE));
        verify(orderDAO, times(1)).update(any(Order.class));

        assertFalse(manager.updateOrder(1, 1, LocalDateTime.now(), BigDecimal.ONE));
        verify(orderDAO, times(2)).update(any(Order.class));
    }

    @Test
    void updateOrderCatchExceptions() throws SQLException {
        when(orderDAO.update(any(Order.class))).thenThrow(new SQLException(),
                new DBException(new Exception()));

        assertFalse(manager.updateOrder(1, 1, LocalDateTime.now(), BigDecimal.ONE));
        verify(orderDAO, times(1)).update(any(Order.class));

        assertFalse(manager.updateOrder(1, 1, LocalDateTime.now(), BigDecimal.ONE));
        verify(orderDAO, times(2)).update(any(Order.class));
    }*/

    ///
    @Test
    void deleteOrderAboveTrashArgs() throws SQLException {
        assertFalse(manager.deleteOrder(-1));
        verify(orderDAO, never()).delete(eq(-1));
    }

    @Test
    void deleteOrderAboveCorrectArgs() throws SQLException {
        assertFalse(manager.deleteOrder(10));
        verify(orderDAO, times(1)).delete(eq(10));
    }

    @Test
    void deleteOrderBelowArgs() throws SQLException {
        when(orderDAO.delete(anyInt())).thenReturn(true, false);

        assertTrue(manager.deleteOrder(10));
        verify(orderDAO, times(1)).delete(eq(10));

        assertFalse(manager.deleteOrder(10));
        verify(orderDAO, times(2)).delete(eq(10));
    }

    @Test
    void deleteOrderCatchExceptions() throws SQLException {
        when(orderDAO.delete(anyInt())).thenThrow(new SQLException(),
                new DBException(new Exception()));

        assertFalse(manager.deleteOrder(10));
        verify(orderDAO, times(1)).delete(eq(10));

        assertFalse(manager.deleteOrder(10));
        verify(orderDAO, times(2)).delete(eq(10));
    }
/*
    @ParameterizedTest
    @MethodSource("addItemAboveTrashArgsProvider")
    void addItemAboveTrashArgs(Order order, int watchId, int qty, BigDecimal price) throws SQLException {
        assertFalse(manager.addItem(order, watchId, qty, price).isPresent());
        verify(itemDAO, never()).create(any(Item.class));
    }

    @Test
    void addItemAboveCorrectArgs() throws SQLException {
        final Order order = mock(Order.class);
        when(order.getId()).thenReturn(3);

        manager.addItem(order, 4, 10, BigDecimal.ONE);

        final ArgumentCaptor<Item> arg = ArgumentCaptor.forClass(Item.class);
        verify(itemDAO, times(1)).create(arg.capture());
        final Item value = arg.getValue();

        assertEquals(3, value.getOrderId());
        assertEquals(4, value.getWatch().getId());
        assertEquals(10, value.getQty());
        assertEquals(BigDecimal.ONE, value.getPrice());
    }

    @Test
    void addItemBelowTrashArgs() throws SQLException {
        when(itemDAO.create(any(Item.class))).thenReturn(null);
        final Order order = mock(Order.class);

        assertFalse(manager.addItem(order, 1, 2, BigDecimal.ONE).isPresent());
        verify(itemDAO, times(1)).create(any(Item.class));
    }

    @Test
    void addItemBelowCorrectArgs() throws SQLException {
        final Order order = mock(Order.class);
        final Item item = mock(Item.class);

        when(itemDAO.create(any(Item.class))).thenReturn(item);

        assertTrue(manager.addItem(order, 4, 1, BigDecimal.ONE).isPresent());
        verify(itemDAO, times(1)).create(any(Item.class));
    }

    @Test
    void addItemBelowCatchExceptions() throws SQLException {
        when(itemDAO.create(any(Item.class))).thenThrow(new SQLException(),
                new DBException(new Exception()));

        final Order order = mock(Order.class);
        assertFalse(manager.addItem(order, 2, 3, BigDecimal.ONE).isPresent());
        verify(itemDAO, times(1)).create(any(Item.class));

        assertFalse(manager.addItem(order, 2, 3, BigDecimal.ONE).isPresent());
        verify(itemDAO, times(2)).create(any(Item.class));
    }

    @ParameterizedTest
    @MethodSource("updateItemAboveTrashArgsProvider")
    void updateItemAboveTrashArgs(Order order, int itemId, BigDecimal price, int qty) throws SQLException {
        assertFalse(manager.updateItem(order, itemId, price, qty));
        verify(itemDAO, never()).update(any(Item.class));
    }

    @Test
    void updateItemAboveCorrectArgs() throws SQLException {
        final Order order = mock(Order.class);
        when(order.getId()).thenReturn(10);

        manager.updateItem(order, 2, BigDecimal.ONE, 10);
        final ArgumentCaptor<Item> arg = ArgumentCaptor.forClass(Item.class);
        verify(itemDAO, times(1)).update(arg.capture());
        final Item value = arg.getValue();
        assertEquals(10, value.getOrderId());
        assertEquals(2, value.getId());
        assertEquals(BigDecimal.ONE, value.getPrice());
        assertEquals(10, value.getQty());
    }

    @Test
    void updateItemBelowArgs() throws SQLException {
        when(itemDAO.update(any(Item.class))).thenReturn(true, false);

        final Order order = mock(Order.class);
        assertTrue(manager.updateItem(order, 1, BigDecimal.ONE, 10));
        assertFalse(manager.updateItem(order, 1, BigDecimal.ONE, 10));
    }

    @Test
    void updateItemCatchExceptions() throws SQLException {
        when(itemDAO.update(any(Item.class))).thenThrow(new SQLException(),
                new DBException(new Exception()));

        final Order order = mock(Order.class);
        assertFalse(manager.updateItem(order, 1, BigDecimal.ONE, 10));
        verify(itemDAO, times(1)).update(any(Item.class));

        assertFalse(manager.updateItem(order, 1, BigDecimal.ONE, 10));
        verify(itemDAO, times(2)).update(any(Item.class));
    }

    @Test
    void deleteItemAboveTrashArgs() throws SQLException {
        assertFalse(manager.deleteItem(null));
        verify(itemDAO, never()).delete(anyInt());

        final Item item = mock(Item.class);
        when(item.getId()).thenReturn(-1);
        assertFalse(manager.deleteItem(item));
        verify(itemDAO, never()).delete(anyInt());
    }

    @Test
    void deleteItemAboveCorrectArgs() throws SQLException {
        final Item item = mock(Item.class);
        when(item.getId()).thenReturn(10);
        when(item.getOrderId()).thenReturn(10);
        when(itemDAO.getById(10)).thenReturn(item);

        manager.deleteItem(item);
        verify(itemDAO, times(1)).delete(eq(10));
    }

    @Test
    void deleteItemBelow() throws SQLException {
        when(itemDAO.delete(anyInt())).thenReturn(true, false);

        final Item item = mock(Item.class);
        when(item.getId()).thenReturn(10);
        when(item.getOrderId()).thenReturn(10);
        when(itemDAO.getById(10)).thenReturn(item);

        assertTrue(manager.deleteItem(item));
        verify(itemDAO, times(1)).delete(eq(10));

        assertFalse(manager.deleteItem(item));
        verify(itemDAO, times(2)).delete(eq(10));
    }

    @Test
    void deleteItemCatchExceptions() throws SQLException {
        when(itemDAO.delete(anyInt())).thenThrow(new SQLException(),
                new DBException(new Exception()));

        final Item item = mock(Item.class);
        when(item.getId()).thenReturn(10);
        when(item.getOrderId()).thenReturn(10);
        when(itemDAO.getById(10)).thenReturn(item);

        assertFalse(manager.deleteItem(item));
        verify(itemDAO, times(1)).delete(eq(10));

        assertFalse(manager.deleteItem(item));
        verify(itemDAO, times(2)).delete(eq(10));
    }*/
}