package com.company.watches.model;

import com.company.watches.utils.DBException;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static com.company.watches.utils.Settings.getConnection;

public class WatchDAO implements DAO<Watch> {

    static Watch createFromResultSet(ResultSet rs) {
        try {
            int watchId = rs.getInt("watch_id");
            String watchBrand = rs.getString("watch_brand");
            Watch.WatchType watchType = Watch.WatchType.valueOf(rs.getString("watch_type"));
            BigDecimal watchPrice = rs.getBigDecimal("watch_price");
            int watchQty = rs.getInt("watch_qty");

            return new Watch(watchId, watchBrand, watchType, watchPrice, watchQty,
                    VendorDAO.createFromResultSet(rs));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Watch> create(Watch model) throws SQLException {
        final String sql = "insert into public.\"WatchModel\" " +
                "(watch_brand, watch_type, watch_price, watch_qty, vendor_id) " +
                "values (?, ?::watch_type, ?, ?, ?) returning *;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, model.getBrand());
            st.setString(2, model.getType().toString());
            st.setBigDecimal(3, model.getPrice());
            st.setInt(4, model.getQty());
            st.setInt(5, model.getVendor().getId());

            st.execute();
            final ResultSet rs = st.getGeneratedKeys();

            return rs.next() ? Optional.of(WatchDAO.createFromResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Watch> getAll() throws SQLException {
        final String sql = "select * from public.\"WatchModel\";";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            return executeAndReturnCollection(st, WatchDAO::createFromResultSet);
        }
    }

    @Override
    public Optional<Watch> getById(int id) throws SQLException {
        final String sql = "select * from public.\"WatchModel\" where watch_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return executeAndReturnObject(st, WatchDAO::createFromResultSet);
        }
    }

    @Override
    public boolean update(Watch model) throws SQLException {
        final String sql = "update public.\"WatchModel\" " +
                "set watch_brand = ?, watch_type=?::watch_type, watch_price=?, watch_qty=?," +
                " vendor_id = ? " +
                "where watch_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, model.getBrand());
            st.setString(2, model.getType().toString());
            st.setBigDecimal(3, model.getPrice());
            st.setInt(4, model.getQty());
            st.setInt(5, model.getVendor().getId());
            st.setInt(6, model.getId());

            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"WatchModel\" where watch_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
