package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VendorDAOTest {
    private VendorDAO dao;

    public VendorDAOTest() {
        dao = new VendorDAO();
    }

    @Test
    void generateVendors() {
        assertEquals(0, dao.getAll().size());

        dao.generateVendors();
        assertEquals(3, dao.getAll().size());
    }

    @Test
    void add() {
        assertEquals(0, dao.getAll().size());

        Vendor vendor1 = new Vendor("test vendor1", new Country(1, "test country"));
        dao.add(vendor1);
        assertEquals(1, dao.getAll().size());

        dao.add(vendor1);
        assertEquals(1, dao.getAll().size());

        dao.add(null);
        assertEquals(1, dao.getAll().size());

        //
        Vendor vendor2 = new Vendor("test vendor2", new Country(1, "test country"));
        dao.add(vendor2);
        assertEquals(2, dao.getAll().size());

        dao.add(new Vendor("TEST vendor2", new Country(1, "test country")));
        assertEquals(2, dao.getAll().size());

        dao.add(new Vendor("", new Country(1, "test country")));
        assertEquals(2, dao.getAll().size());

        dao.add(new Vendor("  ", new Country(1, "test country")));
        assertEquals(2, dao.getAll().size());

    }

    @Test
    void getAll() {
        assertEquals(0, dao.getAll().size());
        dao.generateVendors();

        assertEquals(3, dao.getAll().size());

        List<Vendor> list = dao.getAll();
        list.add(new Vendor("test vendor", new Country(1, "test country")));
        assertEquals(3, dao.getAll().size());
    }
}