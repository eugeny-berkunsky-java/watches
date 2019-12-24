package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryDAOTest {

    private CountryDAO dao;

    @BeforeEach
    public void init() {
        dao = new CountryDAO();
    }

    @Test
    void getAll() {
        assertEquals(4, dao.getAll().size());

        List<Country> countries = dao.getAll();
        countries.add(new Country(5, "test country"));
        assertEquals(4, dao.getAll().size());
    }

    @Test
    void getById() {
        assertEquals(1, dao.getById(1).getId());
        assertNull(dao.getById(-1));
    }

    @Test
    void create() {

        Country country = dao.create("test country");
        assertEquals(5, dao.getAll().size());
        assertEquals(5, country.getId());

        //
        assertNull(dao.create(null));
        assertNull(dao.create(""));
        assertNull(dao.create("  "));

        //
        Country country1 = dao.create("Italy");
        assertNotNull(country1);
        assertEquals(1, country1.getId());
    }

    @Test
    void delete() {
        assertEquals(4, dao.getAll().size());

        dao.delete(new Country(-1, ""));
        assertEquals(4, dao.getAll().size());

        Country country = dao.getById(1);
        dao.delete(country);
        assertEquals(3, dao.getAll().size());
    }
}