package model;

import manage.DBException;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static main.Settings.getConnection;

public class ItemDAO implements DAO<Item> {

    static Item createFromResultSet(ResultSet rs) {
        try {
            int id = rs.getInt("item_id");
            BigDecimal price = rs.getBigDecimal("item_price");
            int qty = rs.getInt("item_qty");
            int orderId = rs.getInt("item_order_id");

            return new Item(id, price, qty, orderId, WatchDAO.createFromResultSet(rs));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Item create(Item model) throws SQLException {
        final String sql = "insert into public.\"ItemModel\" " +
                "(item_price, item_qty, item_order_id, watch_id) " +
                "VALUES (?, ?, ?, ?) returning *;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            st.setBigDecimal(1, model.getPrice());
            st.setInt(2, model.getQty());
            st.setInt(3, model.getOrderId());
            st.setInt(4, model.getWatch().getId());

            st.execute();
            final ResultSet rs = st.getGeneratedKeys();
            rs.next();

            return createFromResultSet(rs);
        }
    }

    @Override
    public List<Item> getAll() throws SQLException {
        final String sql = "select * from public.\"ItemModel\";";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            return executeAndReturnCollection(st, ItemDAO::createFromResultSet);
        }
    }

    @Override
    public Item getById(int id) throws SQLException {
        final String sql = "select * from public.\"ItemModel\" where item_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);

            return executeAndReturnObject(st, ItemDAO::createFromResultSet);
        }
    }

    @Override
    public boolean update(Item model) throws SQLException {
        final String sql = "update public.\"ItemModel\" " +
                "set item_price = ?, item_qty = ? " +
                "where item_id = ? and item_order_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setBigDecimal(1, model.getPrice());
            st.setInt(2, model.getQty());
            st.setInt(3, model.getId());
            st.setInt(4, model.getOrderId());

            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"ItemModel\" where item_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);

            return st.executeUpdate() > 0;
        }
    }
}
