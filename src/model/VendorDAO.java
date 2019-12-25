package model;

import main.Settings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class VendorDAO implements DAO<Vendor> {

    private final String url;
    private final Properties properties;

    public VendorDAO() {
        url = Settings.getConnectionURL();
        properties = Settings.getConnectionProperties();
    }

    @Override
    public Vendor create(Vendor model) {

        final String sql = "insert into public.\"Vendor\" (vendorname, countryid) " +
                "VALUES (?, ?) returning *";

        int savedId = -1;

        try (final Connection conn = DriverManager.getConnection(url, properties)) {
            final PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, model.getVendorName());
            st.setInt(2, model.getCountry().getId());
            st.execute();
            final ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                savedId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return getById(savedId);
    }

    @Override
    public List<Vendor> getAll() {
        final String sql =
                "" +
                        "select " +
                        "vendor.id         as vendor_id," +
                        "vendor.vendorname as vendor_vendorname," +
                        "country.id        as country_id," +
                        "country.name      as country_name " +
                        "from \"Vendor\"   as vendor " +
                        "    inner join \"Country\" as country on vendor.countryid = country.id";

        List<Vendor> vendors = new ArrayList<>();
        try (final Connection conn = DriverManager.getConnection(url, properties)) {
            final PreparedStatement st = conn.prepareStatement(sql);
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int vendorId = rs.getInt("vendor_id");
                String vendorName = rs.getString("vendor_vendorname");
                int countryId = rs.getInt("country_id");
                String countryName = rs.getString("country_name");
                vendors.add(new Vendor(vendorId, vendorName, new Country(countryId, countryName)));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return vendors;
    }

    public Vendor getById(int id) {
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

        try (final Connection conn = DriverManager.getConnection(url, properties)) {
            final PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            final ResultSet rs = st.executeQuery();
            if (rs.next()) {
                final int vendorId = rs.getInt("vendor_id");
                final String vendorName = rs.getString("vendor_vendorname");
                final int countryId = rs.getInt("country_id");
                final String countryName = rs.getString("country_name");

                return new Vendor(vendorId, vendorName, new Country(countryId, countryName));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return null;
    }

    @Override
    public boolean update(Vendor model) {
        final String sql = "" +
                "update public.\"Vendor\" " +
                "set vendorname = ?, countryid = ? " +
                "where id = ?;";

        try (final Connection conn = DriverManager.getConnection(url, properties)) {
            final PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, model.getVendorName());
            st.setInt(2, model.getCountry().getId());
            st.setInt(3, model.getId());
            final int result = st.executeUpdate();

            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        final String sql = "delete from public.\"Vendor\" where id = ?;";

        try (final Connection conn = DriverManager.getConnection(url, properties)) {
            final PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            final int result = st.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return false;
    }

}
