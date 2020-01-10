package model;

import utils.DBException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import static utils.Settings.getConnection;

public class OrderDAO implements DAO<Order> {

    static Order createOrderFromResultSet(ResultSet rs) {
        try {
            int orderId = rs.getInt("order_id");
            LocalDateTime orderDate = rs.getObject("order_date", LocalDateTime.class);
            BigDecimal orderTotalPrice = rs.getBigDecimal("order_totalprice");
            Customer customer = CustomerDAO.createFromResultSet(rs);

            return new Order(orderId, orderDate, customer, null, orderTotalPrice);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Order> create(Order model) throws SQLException {
        final String sql = "insert into public.\"OrderModel\" (order_date, order_totalprice, customer_id) " +
                "values (?, ?, ?) returning *;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            st.setObject(1, model.getDate(), Types.TIMESTAMP);
            st.setBigDecimal(2, model.getTotalPrice());
            st.setInt(3, model.getCustomer().getId());

            st.execute();
            final ResultSet rs = st.getGeneratedKeys();

            return rs.next() ? Optional.of(createOrderFromResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Order> getAll() throws SQLException {
        final String sql = "select * from public.\"OrderModel\";";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            final ResultSet rs = st.executeQuery();

            Map<Integer, Order> orders = new HashMap<>();

            while (rs.next()) {
                final int orderId = rs.getInt("order_id");
                orders.putIfAbsent(orderId, createOrderFromResultSet(rs));

                rs.getInt("item_id");
                final Order order = orders.get(orderId);
                if (!rs.wasNull()) {
                    if (order.getItems() == null) {
                        order.setItems(new ArrayList<>());
                    }

                    order.getItems().add(ItemDAO.createFromResultSet(rs));
                } else {
                    order.setItems(Collections.emptyList());
                }
            }

            return new ArrayList<>(orders.values());
        }

    }

    @Override
    public Optional<Order> getById(int id) throws SQLException {
        final String sql = "select * from public.\"OrderModel\" where order_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            final ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            final Order order = createOrderFromResultSet(rs);

            List<Item> items = new ArrayList<>();
            items.add(ItemDAO.createFromResultSet(rs));

            while (rs.next()) {
                items.add(ItemDAO.createFromResultSet(rs));
            }
            order.setItems(items);

            return Optional.of(order);
        }
    }

    @Override
    public boolean update(Order model) throws SQLException {
        final String sql = "update public.\"OrderModel\" " +
                "set order_date = ?, order_totalprice = ?, customer_id = ? " +
                "where order_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setObject(1, model.getDate(), Types.TIMESTAMP);
            st.setBigDecimal(2, model.getTotalPrice());
            st.setInt(3, model.getCustomer().getId());
            st.setInt(4, model.getId());

            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"OrderModel\" where order_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);

            return st.executeUpdate() > 0;
        }
    }
}
