package manage;

import model.Country;
import model.Vendor;
import model.Watch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WatchManagerTest {

    private WatchManager manager;

    @BeforeEach
    void init() {
        manager = new WatchManager();
    }

    @Test
    void getByType() {
        assertEquals(3, manager.getByType(Watch.WatchType.ANALOGUE).size());
        assertEquals(4, manager.getByType(Watch.WatchType.DIGITAL).size());
        assertEquals(0, manager.getByType(null).size());
    }

    @Test
    void getByTypeAndPriceNotGreaterThan() {
        assertEquals(0, manager.getByTypeAndPriceNotGreaterThan(
                Watch.WatchType.ANALOGUE,
                new BigDecimal("10.00", new MathContext(2))).size());

        assertEquals(2, manager.getByTypeAndPriceNotGreaterThan(
                Watch.WatchType.ANALOGUE,
                new BigDecimal("100.00", new MathContext(2))).size());

        assertEquals(2, manager.getByTypeAndPriceNotGreaterThan(
                Watch.WatchType.ANALOGUE,
                new BigDecimal("500.00", new MathContext(2))).size());

        assertEquals(3, manager.getByTypeAndPriceNotGreaterThan(
                Watch.WatchType.ANALOGUE,
                new BigDecimal("1000.00", new MathContext(2))).size());

        assertEquals(0, manager.getByTypeAndPriceNotGreaterThan(null, null).size());
    }

    @Test
    void getByCountry() {
        Country country1 = new Country(1, "Italy");
        assertEquals(2, manager.getByCountry(country1).size());

        Country country2 = new Country(100, "Japan");
        assertEquals(0, manager.getByCountry(country2).size());

        assertEquals(0, manager.getByCountry(null).size());
    }

    @Test
    void getVendorByTotalSumNotGreaterThan() {
        assertEquals(0, manager.getVendorByTotalSumNotGreaterThan(new BigDecimal("1000.00",
                new MathContext(2))).size());

        List<Vendor> vendors = manager.getVendorByTotalSumNotGreaterThan(new BigDecimal("2300.00",
                new MathContext(2)));
        assertEquals(1, vendors.size());
        assertEquals("Cartier", vendors.get(0).getVendorName());

        //
        vendors = manager.getVendorByTotalSumNotGreaterThan(new BigDecimal("2500.00",
                new MathContext(2)));
        assertEquals(2, vendors.size());

        vendors.sort(Comparator.comparing(Vendor::getVendorName));
        assertEquals("Bell & Ross", vendors.get(0).getVendorName());
        assertEquals("Cartier", vendors.get(1).getVendorName());

        //
        vendors = manager.getVendorByTotalSumNotGreaterThan(new BigDecimal("20000.00",
                new MathContext(2)));
        assertEquals(3, vendors.size());

        vendors.sort(Comparator.comparing(Vendor::getVendorName));
        assertEquals("Bell & Ross", vendors.get(0).getVendorName());
        assertEquals("Bremont", vendors.get(1).getVendorName());
        assertEquals("Cartier", vendors.get(2).getVendorName());

    }
}