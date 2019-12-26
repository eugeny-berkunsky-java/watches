package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static main.Settings.getConnection;

class VendorDAO implements DAO<Vendor> {

    @Override
    public Vendor create(Vendor model) throws SQLException {

        final String sql = "insert into public.\"Vendor\" (vendorname, countryid) " +
                "VALUES (?, ?) returning *";

        try (final PreparedStatement st
                     = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, model.getVendorName());
            st.setInt(2, model.getCountry().getId());
            st.execute();
            final ResultSet rs = st.getGeneratedKeys();

            return getById(rs.getInt("id"));
        }
    }

    @Override
    public List<Vendor> getAll() throws SQLException {
        final String sql =
                "" +
                        "select " +
                        "vendor.id         as vendor_id," +
                        "vendor.vendorname as vendor_vendorname," +
                        "country.id        as country_id," +
                        "country.name      as country_name " +
                        "from \"Vendor\"   as vendor " +
                        "    inner join \"Country\" as country on vendor.countryid = country.id";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            final ResultSet rs = st.executeQuery();

            List<Vendor> vendors = new ArrayList<>();
            while (rs.next()) {
                int vendorId = rs.getInt("vendor_id");
                String vendorName = rs.getString("vendor_vendorname");
                int countryId = rs.getInt("country_id");
                String countryName = rs.getString("country_name");
                vendors.add(new Vendor(vendorId, vendorName, new Country(countryId, countryName)));
            }
            return vendors;
        }
    }

    public Vendor getById(int id) throws SQLException {
        final String sql =
                "" +
                        "select " +
                        "vendor.id         as vendor_id," +
                        "vendor.vendorname as vendor_vendorname," +
                        "country.id        as country_id," +
                        "country.name      as country_name " +
                        "from \"Vendor\"   as vendor " +
                        "    inner join \"Country\" as country on vendor.countryid = country.id " +
                        "where vendor.id = ?";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);

            final ResultSet rs = st.executeQuery();
            final int vendorId = rs.getInt("vendor_id");
            final String vendorName = rs.getString("vendor_vendorname");
            final int countryId = rs.getInt("country_id");
            final String countryName = rs.getString("country_name");

            return new Vendor(vendorId, vendorName, new Country(countryId, countryName));
        }
    }

    @Override
    public boolean update(Vendor model) throws SQLException {
        final String sql = "" +
                "update public.\"Vendor\" " +
                "set vendorname = ?, countryid = ? " +
                "where id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, model.getVendorName());
            st.setInt(2, model.getCountry().getId());
            st.setInt(3, model.getId());

            return st.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        final String sql = "delete from public.\"Vendor\" where id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
