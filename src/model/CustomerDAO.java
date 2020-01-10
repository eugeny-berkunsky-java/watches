package model;

import utils.DBException;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static utils.Settings.getConnection;

public class CustomerDAO implements DAO<Customer> {

    static Customer createFromResultSet(ResultSet rs) {
        try {
            final int cId = rs.getInt("customer_id");
            final String cName = rs.getString("customer_name");
            final BigDecimal cSumOfOrders = rs.getBigDecimal("customer_sumoforders");

            return new Customer(cId, cName, cSumOfOrders,
                    DiscountCardDAO.createFromResultSet(rs));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Customer> create(Customer model) throws SQLException {
        final String sql = "insert into public.\"CustomerModel\" " +
                "(customer_name, customer_sumoforders, dcard_id) " +
                " values (?, ?, ?) returning *;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, model.getName());
            st.setBigDecimal(2, model.getSumOfOrders());
            st.setInt(3, model.getDiscountCard().getId());

            st.execute();
            final ResultSet rs = st.getGeneratedKeys();

            return rs.next() ? Optional.of(createFromResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        final String sql = "select * from public.\"CustomerModel\";";


        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            return executeAndReturnCollection(st, CustomerDAO::createFromResultSet);
        }
    }

    @Override
    public Optional<Customer> getById(int id) throws SQLException {
        final String sql = "select * from public.\"CustomerModel\" where customer_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return executeAndReturnObject(st, CustomerDAO::createFromResultSet);
        }
    }

    @Override
    public boolean update(Customer model) throws SQLException {
        final String sql = "update public.\"CustomerModel\" " +
                "set customer_name = ?, customer_sumoforders=?, dcard_id = ? " +
                "where customer_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, model.getName());
            st.setBigDecimal(2, model.getSumOfOrders());
            st.setInt(3, model.getDiscountCard().getId());
            st.setInt(4, model.getId());

            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"CustomerModel\" where customer_id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
