package com.company.watches.dao;

import com.company.watches.model.Customer;
import com.company.watches.model.Item;
import com.company.watches.model.Order;
import com.company.watches.utils.DBException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.company.watches.utils.ConnectionManager.getConnection;

public class OrderDAO implements DAO<Order> {

    static Order createOrderFromResultSet(ResultSet rs) {
        try {
            int orderId = rs.getInt("order_id");
            LocalDateTime orderDate = rs.getObject("order_date", LocalDateTime.class);
            BigDecimal orderTotalPrice = rs.getBigDecimal("order_totalprice");
            Customer customer = CustomerDAO.createFromResultSet(rs);

            return new Order(orderId, orderDate, customer, Collections.emptyList(), orderTotalPrice);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Order> create(Order model) throws SQLException {
        final String sql = "insert into public.\"OrderModel\" (order_date, order_totalprice, customer_id) " +
                "values (?, ?, ?) returning *;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

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
        final String sql = "select * from public.\"ShortOrderModel\";";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);

            return executeAndReturnCollection(st, OrderDAO::createOrderFromResultSet);
        }
    }

    @Override
    public Optional<Order> getById(int id) throws SQLException {
        final String sql = "select * from public.\"OrderModel\" where order_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, id);
            final ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            final Order order = createOrderFromResultSet(rs);

            List<Item> items = new ArrayList<>();

            do {
                final Item item = ItemDAO.createFromResultSet(rs);
                if (item.getId() != -1) {
                    items.add(item);
                }

            } while (rs.next());

            order.setItems(items);

            return Optional.of(order);
        }
    }

    @Override
    public boolean update(Order model) throws SQLException {
        final String sql = "update public.\"OrderModel\" " +
                "set order_date = ?, order_totalprice = ?, customer_id = ? " +
                "where order_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);

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

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, id);

            return st.executeUpdate() > 0;
        }
    }
}
