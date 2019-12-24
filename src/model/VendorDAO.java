package model;

import main.Settings;

import java.sql.*;
import java.util.*;

class VendorDAO implements DAO<Vendor> {

    private final String url;
    private final Properties properties;
    private final DAO<Country> countryDAO;

    public VendorDAO(DAO<Country> countryDAO) {
        url = Settings.getConnectionURL();
        properties = Settings.getConnectionProperties();
        this.countryDAO = countryDAO;
    }

    @Override
    public Vendor create(Vendor model) {
        return null;
    }

    @Override
    public List<Vendor> getAll() {
        final String sql = "select id, vendorname, countryid from public.\"Vendor\";";

        final List<Country> countryList = countryDAO.getAll();
        countryList.sort(Comparator.comparingInt(Country::getId));

        List<Vendor> vendors = new ArrayList<>();
        try (final Connection conn = DriverManager.getConnection(url, properties)) {
            final PreparedStatement st = conn.prepareStatement(sql);
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String vendorName = rs.getString("vendorName");
                int countryId = rs.getInt("countryId");
                Country country = findCountry(countryList, countryId);
                if (country != null) {
                    vendors.add(new Vendor(id, vendorName, country));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return vendors;
    }

    public Vendor getById(int id) {
        return null;
    }

    @Override
    public boolean update(Vendor model) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    private Country findCountry(List<Country> countries, int countryId) {
        final int index = Collections.binarySearch(countries, new Country(countryId, null),
                Comparator.comparingInt(Country::getId));
        return index >= 0 ? countries.get(index) : null;
    }
}
