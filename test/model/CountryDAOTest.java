package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CountryDAOTest {

    private CountryDAO dao;

    @BeforeEach
    public void init() {
        dao = new CountryDAO();
    }

    @Test
    void getAll() {
        assertNotNull(dao.getAll());
    }

    @Test
    void getById() {
        assertNotNull(dao.getById(1));
        assertNull(dao.getById(-1));
    }

    @Test
    void create() {
        UUID uuid = UUID.randomUUID();
        Country country = dao.create(new Country(-1, uuid.toString()));
        assertEquals(uuid.toString(), country.getName());
        System.out.println(country);
    }

    @Test
    void update() {
        final List<Country> list = dao.getAll();
        assertTrue(list.size() > 0);

        Country country = list.get(list.size() - 1);
        final UUID uuid = UUID.randomUUID();
        assertNotEquals(uuid.toString(), country.getName());

        country.setName(uuid.toString());
        assertTrue(dao.update(country));

        final Country updatedCountry = dao.getById(country.getId());
        assertEquals(country.getName(), updatedCountry.getName());
    }

    @Test
    void delete() {
        final List<Country> list = dao.getAll();
        assertTrue(list.size() > 0);

        Country country = list.get(list.size() - 1);

        assertTrue(dao.delete(country.getId()));

        final List<Country> updatedList = dao.getAll();
        assertTrue(updatedList.size() < list.size());
        assertNull(dao.getById(country.getId()));
    }

}