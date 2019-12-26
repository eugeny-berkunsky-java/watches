package model;

import manage.DBException;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static main.Settings.getConnection;

public class WatchDAO implements DAO<Watch> {

    @Override
    public Watch create(Watch model) throws SQLException {
        final String sql = "insert into public.\"Watch\" (brand, type, price, qty, vendor_id) " +
                "values (?, ?, ?, ?, ?) returning *;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, model.getBrand());
            st.setString(2, model.getType().toString());
            st.setBigDecimal(3, model.getPrice());
            st.setInt(4, model.getQty());
            st.setInt(5, model.getVendor().getId());

            st.execute();
            final ResultSet rs = st.getGeneratedKeys();
            rs.next();
            return getById(rs.getInt("id"));
        }
    }

    @Override
    public List<Watch> getAll() throws SQLException {
        final String sql = "" +
                "select watch.id          as watch_id," +
                "       watch.brand       as watch_brand, " +
                "       watch.type        as watch_type, " +
                "       watch.price       as watch_price, " +
                "       watch.qty         as watch_qty, " +
                "       vendor.id         as watch_vendor_id, " +
                "       vendor.vendorname as watch_vendor_vendorName, " +
                "       country.id        as watch_vendor_country_id, " +
                "       country.name      as watch_vendor_country_name " +
                "from public.\"Watch\" as watch " +
                "         inner join public.\"Vendor\" as vendor on watch.vendor_id = vendor.id " +
                "         inner join public.\"Country\" as country on vendor.countryid= country.id";


        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            return executeAndReturnCollection(st, this::createWatchFromResultSet);
        }
    }

    @Override
    public Watch getById(int id) throws SQLException {
        final String sql = "" +
                "select watch.id          as watch_id," +
                "       watch.brand       as watch_brand, " +
                "       watch.type        as watch_type, " +
                "       watch.price       as watch_price, " +
                "       watch.qty         as watch_qty, " +
                "       vendor.id         as watch_vendor_id, " +
                "       vendor.vendorname as watch_vendor_vendorName, " +
                "       country.id        as watch_vendor_country_id, " +
                "       country.name      as watch_vendor_country_name " +
                "from public.\"Watch\" as watch " +
                "         inner join public.\"Vendor\" as vendor on watch.vendor_id = vendor.id " +
                "         inner join public.\"Country\" as country on vendor.countryid = country.id " +
                "where watch.id = ?;";

        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return executeAndReturnObject(st, this::createWatchFromResultSet);
        }
    }

    @Override
    public boolean update(Watch model) throws SQLException {
        final String sql = "update public.\"Watch\" set brand = ?, type=?, price=?, qty=?, " +
                "vendor_id = ? where id = ?;";

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
        final String sql = "delete from public.\"Watch\" where id = ?;";
        try (final PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }

    private Watch createWatchFromResultSet(ResultSet rs) {
        try {
            int watchId = rs.getInt("watch_id");
            String watchBrand = rs.getString("watch_brand");
            Watch.WatchType watchType = Watch.WatchType.valueOf(rs.getString("watch_type"));
            BigDecimal watchPrice = rs.getBigDecimal("watch_price");
            int watchQty = rs.getInt("watch_qty");

            int vendorId = rs.getInt("watch_vendor_id");
            String vendorName = rs.getString("watch_vendor_vendorName");

            int countryId = rs.getInt("watch_vendor_country_id");
            String countryName = rs.getString("watch_vendor_country_name");

            return new Watch(watchId, watchBrand, watchType, watchPrice, watchQty,
                    new Vendor(vendorId, vendorName,
                            new Country(countryId, countryName)));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
