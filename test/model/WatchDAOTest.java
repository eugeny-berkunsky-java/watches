package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WatchDAOTest {

    private WatchDAO dao;

    @BeforeEach
    void init() {
        dao = new WatchDAO();
    }

    @Test
    void generateWatches() {
        assertEquals(0, dao.getAll().size());

        dao.generateWatches();
        assertEquals(7, dao.getAll().size());
    }

    @Test
    void load() {
    }

    @Test
    void save() {
    }

    @Test
    void getAll() {
        assertEquals(0, dao.getAll().size());

        Watch watch = new Watch("test brand", Watch.WatchType.ANALOGUE, BigDecimal.TEN, 1,
                new Vendor("vendor", new Country(1, "country")));

        dao.add(watch);
        assertEquals(1, dao.getAll().size());

        List<Watch> list = dao.getAll();
        list.add(watch);
        assertEquals(1, dao.getAll().size());
    }

    @Test
    void add() {
        assertEquals(0, dao.getAll().size());
        Watch watch = new Watch("test brand", Watch.WatchType.ANALOGUE, BigDecimal.TEN, 1,
                new Vendor("vendor", new Country(1, "country")));

        dao.add(watch);
        assertEquals(1, dao.getAll().size());

        dao.add(watch);
        assertEquals(1, dao.getAll().size());

        dao.add(null);
        assertEquals(1, dao.getAll().size());

    }
}