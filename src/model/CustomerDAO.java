package model;

import manage.DBException;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static main.Settings.getConnection;

public class CustomerDAO implements DAO<Customer> {

    @Override
    public Customer create(Customer model) throws SQLException {
        final String sql = "insert into public.\"Customer\" (name, sumoforders, discountcard_id) " +
                " values (?, ?, ?) returning *;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, model.getName());
            st.setBigDecimal(2, model.getSumOfOrders());
            st.setInt(3, model.getDiscountCard().getId());

            st.execute();
            final ResultSet rs = st.getGeneratedKeys();
            rs.next();

            return getById(rs.getInt("id"));
        }
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        final String sql = "" +
                "select c.id          as customer_id, " +
                "       c.name        as customer_name, " +
                "       c.sumoforders as customer_sumoforders, " +
                "       dc.id         as customer_dc_id, " +
                "       dc.number     as customer_dc_number, " +
                "       dc.percent    as customer_dc_percent " +
                "from public.\"Customer\" as c " +
                "         inner join public.\"DiscountCard\" dc on c.discountcard_id = dc.id;";


        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            return executeAndReturnCollection(st, this::createCustomerFromResultSet);
        }
    }

    @Override
    public Customer getById(int id) throws SQLException {
        final String sql = "" +
                "select c.id          as customer_id, " +
                "       c.name        as customer_name, " +
                "       c.sumoforders as customer_sumoforders, " +
                "       dc.id         as customer_dc_id, " +
                "       dc.number     as customer_dc_number, " +
                "       dc.percent    as customer_dc_percent " +
                "from public.\"Customer\" as c " +
                "         inner join public.\"DiscountCard\" dc on c.discountcard_id = dc.id " +
                "where c.id = ?; ";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return executeAndReturnObject(st, this::createCustomerFromResultSet);
        }
    }

    @Override
    public boolean update(Customer model) throws SQLException {
        final String sql = "update public.\"Customer\" set name = ?, sumoforders=?, " +
                "discountcard_id = ? where id = ?;";

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
        final String sql = "delete from public.\"Customer\" where id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }

    private Customer createCustomerFromResultSet(ResultSet rs) {
        try {
            final int cId = rs.getInt("customer_id");
            final String cName = rs.getString("customer_name");
            final BigDecimal cSumOfOrders = rs.getBigDecimal("customer_sumoforders");
            final int dcId = rs.getInt("customer_dc_id");
            final String dcNumber = rs.getString("customer_dc_number");
            final BigDecimal dcPercent = rs.getBigDecimal("customer_dc_percent");

            return new Customer(cId, cName, cSumOfOrders,
                    new DiscountCard(dcId, dcNumber, dcPercent));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
