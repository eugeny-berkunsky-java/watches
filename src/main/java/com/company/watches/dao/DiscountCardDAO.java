package com.company.watches.dao;

import com.company.watches.model.DiscountCard;
import com.company.watches.utils.DBException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static com.company.watches.utils.Settings.getConnection;

class DiscountCardDAO implements DAO<DiscountCard> {

    static DiscountCard createFromResultSet(ResultSet rs) {
        try {
            int id = rs.getInt("dcard_id");
            String number = rs.getString("dcard_number");
            final BigDecimal percent = rs.getBigDecimal("dcard_percent");
            return new DiscountCard(id, number, percent);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<DiscountCard> create(DiscountCard model) throws SQLException {
        final String sql = "insert into public.\"DiscountCardModel\" (dcard_number, dcard_percent) " +
                "values (? ,  ?) returning *;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st
                    = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, model.getNumber());
            st.setBigDecimal(2, model.getPercent());
            st.execute();
            final ResultSet rs = st.getGeneratedKeys();

            return rs.next() ? Optional.of(createFromResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<DiscountCard> getAll() throws SQLException {
        final String sql = "select * from public.\"DiscountCardModel\";";

        try (final Connection conn = getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            return executeAndReturnCollection(st, DiscountCardDAO::createFromResultSet);
        }
    }

    @Override
    public Optional<DiscountCard> getById(int id) throws SQLException {
        final String sql = "select * from public.\"DiscountCardModel\" where dcard_id = ?;";

        try (final Connection conn = getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            return executeAndReturnObject(st, DiscountCardDAO::createFromResultSet);
        }
    }

    @Override
    public boolean update(DiscountCard model) throws SQLException {
        final String sql = "update public.\"DiscountCardModel\"" +
                " set dcard_number = ?, dcard_percent = ? " +
                "where dcard_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);

            st.setString(1, model.getNumber());
            st.setBigDecimal(2, model.getPercent());
            st.setInt(3, model.getId());

            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"DiscountCardModel\" where dcard_id = ?;";

        try (final Connection conn = getConnection()) {
            final PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
