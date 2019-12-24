package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VendorDAOTest {
    private VendorDAO dao;

    public VendorDAOTest() {
        dao = new VendorDAO(null);
    }

    @Test
    void generateVendors() {
        assertEquals(0, dao.getAll().size());
        assertEquals(3, dao.getAll().size());
    }

    @Test
    void add() {
        assertEquals(0, dao.getAll().size());

        Vendor vendor1 = new Vendor(1, "test vendor1", new Country(1, "test country"));
        dao.create(vendor1);
        assertEquals(1, dao.getAll().size());

        dao.create(vendor1);
        assertEquals(1, dao.getAll().size());

        dao.create(null);
        assertEquals(1, dao.getAll().size());

        //
        Vendor vendor2 = new Vendor(1, "test vendor2", new Country(1, "test country"));
        dao.create(vendor2);
        assertEquals(2, dao.getAll().size());

        dao.create(new Vendor(1, "TEST vendor2", new Country(1, "test country")));
        assertEquals(2, dao.getAll().size());

        dao.create(new Vendor(1, "", new Country(1, "test country")));
        assertEquals(2, dao.getAll().size());

        dao.create(new Vendor(1, "  ", new Country(1, "test country")));
        assertEquals(2, dao.getAll().size());

    }

    @Test
    void getAll() {
        assertEquals(0, dao.getAll().size());

        assertEquals(3, dao.getAll().size());

        List<Vendor> list = dao.getAll();
        list.add(new Vendor(1, "test vendor", new Country(1, "test country")));
        assertEquals(3, dao.getAll().size());
    }
}