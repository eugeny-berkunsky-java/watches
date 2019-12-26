package model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static main.Settings.getConnection;

class DiscountCardDAO implements DAO<DiscountCard> {

    @Override
    public DiscountCard create(DiscountCard model) throws SQLException {
        final String sql = "insert into public.\"DiscountCard\" (number, percent) " +
                "values (? ,  ?) returning *;";

        try (final PreparedStatement st
                     = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            st.setString(1, model.getNumber());
            st.setBigDecimal(2, model.getPercent());
            st.execute();
            final ResultSet rs = st.getGeneratedKeys();

            return getById(rs.getInt("id"));
        }
    }

    @Override
    public List<DiscountCard> getAll() throws SQLException {
        final String sql = "select id, number, percent from public.\"DiscountCard\";";

        try (Statement st = getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            List<DiscountCard> result = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("number");
                final BigDecimal percent = rs.getBigDecimal("percent");
                result.add(new DiscountCard(id, number, percent));
            }
            return result;
        }
    }

    @Override
    public DiscountCard getById(int id) throws SQLException {
        final String sql = "select number, percent from public.\"DiscountCard\" where id = ?;";

        try (PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery(sql);
            final String number = rs.getString("number");
            final BigDecimal percent = rs.getBigDecimal("percent");

            return new DiscountCard(id, number, percent);
        }
    }

    @Override
    public boolean update(DiscountCard model) throws SQLException {
        final String sql = "update public.\"DiscountCard\" set number = ?, percent = ? " +
                "where id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, model.getNumber());
            st.setBigDecimal(2, model.getPercent());
            st.setInt(3, model.getId());

            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"DiscountCard\" where id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
