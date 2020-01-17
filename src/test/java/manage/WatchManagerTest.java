package manage;

import com.company.watches.dao.DAO;
import com.company.watches.dao.DAOContainer;
import com.company.watches.manage.WatchManager;
import com.company.watches.model.Vendor;
import com.company.watches.model.Watch;
import com.company.watches.utils.DBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.company.watches.model.Watch.WatchType.ANALOGUE;
import static com.company.watches.model.Watch.WatchType.DIGITAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WatchManagerTest {

    private DAO<Watch> dao;
    private WatchManager manager;

    public static Stream<Arguments> addWatchAboveTrashArgsProvider() {
        return Stream.of(
                Arguments.of(" ", ANALOGUE, BigDecimal.ZERO, 1, 1),
                Arguments.of("", ANALOGUE, BigDecimal.ZERO, 1, 1),
                ///
                Arguments.of(null, null, null, 0, -1),
                Arguments.of(null, null, null, 0, 1),
                Arguments.of(null, null, null, 1, -1),
                Arguments.of(null, null, null, 1, 1),

                Arguments.of(null, null, BigDecimal.ZERO, 0, -1),
                Arguments.of(null, null, BigDecimal.ZERO, 0, 1),
                Arguments.of(null, null, BigDecimal.ZERO, 1, -1),
                Arguments.of(null, null, BigDecimal.ZERO, 1, 1),

                Arguments.of(null, ANALOGUE, null, 0, -1),
                Arguments.of(null, ANALOGUE, null, 0, 1),
                Arguments.of(null, ANALOGUE, null, 1, -1),
                Arguments.of(null, ANALOGUE, null, 1, 1),

                Arguments.of(null, ANALOGUE, BigDecimal.ZERO, 0, -1),
                Arguments.of(null, ANALOGUE, BigDecimal.ZERO, 0, 1),
                Arguments.of(null, ANALOGUE, BigDecimal.ZERO, 1, -1),
                Arguments.of(null, ANALOGUE, BigDecimal.ZERO, 1, 1),
                //
                Arguments.of("brand", null, null, 0, -1),
                Arguments.of("brand", null, null, 0, 1),
                Arguments.of("brand", null, null, 1, -1),
                Arguments.of("brand", null, null, 1, 1),

                Arguments.of("brand", null, BigDecimal.ZERO, 0, -1),
                Arguments.of("brand", null, BigDecimal.ZERO, 0, 1),
                Arguments.of("brand", null, BigDecimal.ZERO, 1, -1),
                Arguments.of("brand", null, BigDecimal.ZERO, 1, 1),

                Arguments.of("brand", ANALOGUE, null, 0, -1),
                Arguments.of("brand", ANALOGUE, null, 0, 1),
                Arguments.of("brand", ANALOGUE, null, 1, -1),
                Arguments.of("brand", ANALOGUE, null, 1, 1),

                Arguments.of("brand", ANALOGUE, BigDecimal.ZERO, 0, -1),
                Arguments.of("brand", ANALOGUE, BigDecimal.ZERO, 0, 1),
                Arguments.of("brand", ANALOGUE, BigDecimal.ZERO, 1, -1)
        );
    }

    public static Stream<Arguments> updateWatchAboveTrashArgsProvider() {
        return Stream.of(
                Arguments.of(1, " ", ANALOGUE, BigDecimal.ONE, 1, 1),
                Arguments.of(1, "", ANALOGUE, BigDecimal.ONE, 1, 1),

                Arguments.of(-1, null, null, null, 0, -1),
                Arguments.of(-1, null, null, null, 0, 1),
                Arguments.of(-1, null, null, null, 1, -1),
                Arguments.of(-1, null, null, null, 1, 1),
                Arguments.of(-1, null, null, BigDecimal.ONE, 0, -1),
                Arguments.of(-1, null, null, BigDecimal.ONE, 0, 1),
                Arguments.of(-1, null, null, BigDecimal.ONE, 1, -1),
                Arguments.of(-1, null, null, BigDecimal.ONE, 1, 1),
                Arguments.of(-1, null, ANALOGUE, null, 0, -1),
                Arguments.of(-1, null, ANALOGUE, null, 0, 1),
                Arguments.of(-1, null, ANALOGUE, null, 1, -1),
                Arguments.of(-1, null, ANALOGUE, null, 1, 1),
                Arguments.of(-1, null, ANALOGUE, BigDecimal.ONE, 0, -1),
                Arguments.of(-1, null, ANALOGUE, BigDecimal.ONE, 0, 1),
                Arguments.of(-1, null, ANALOGUE, BigDecimal.ONE, 1, -1),
                Arguments.of(-1, null, ANALOGUE, BigDecimal.ONE, 1, 1),
                Arguments.of(-1, "brand", null, null, 0, -1),
                Arguments.of(-1, "brand", null, null, 0, 1),
                Arguments.of(-1, "brand", null, null, 1, -1),
                Arguments.of(-1, "brand", null, null, 1, 1),
                Arguments.of(-1, "brand", null, BigDecimal.ONE, 0, -1),
                Arguments.of(-1, "brand", null, BigDecimal.ONE, 0, 1),
                Arguments.of(-1, "brand", null, BigDecimal.ONE, 1, -1),
                Arguments.of(-1, "brand", null, BigDecimal.ONE, 1, 1),
                Arguments.of(-1, "brand", ANALOGUE, null, 0, -1),
                Arguments.of(-1, "brand", ANALOGUE, null, 0, 1),
                Arguments.of(-1, "brand", ANALOGUE, null, 1, -1),
                Arguments.of(-1, "brand", ANALOGUE, null, 1, 1),
                Arguments.of(-1, "brand", ANALOGUE, BigDecimal.ONE, 0, -1),
                Arguments.of(-1, "brand", ANALOGUE, BigDecimal.ONE, 0, 1),
                Arguments.of(-1, "brand", ANALOGUE, BigDecimal.ONE, 1, -1),
                Arguments.of(-1, "brand", ANALOGUE, BigDecimal.ONE, 1, 1),
                Arguments.of(1, null, null, null, 0, -1),
                Arguments.of(1, null, null, null, 0, 1),
                Arguments.of(1, null, null, null, 1, -1),
                Arguments.of(1, null, null, null, 1, 1),
                Arguments.of(1, null, null, BigDecimal.ONE, 0, -1),
                Arguments.of(1, null, null, BigDecimal.ONE, 0, 1),
                Arguments.of(1, null, null, BigDecimal.ONE, 1, -1),
                Arguments.of(1, null, null, BigDecimal.ONE, 1, 1),
                Arguments.of(1, null, ANALOGUE, null, 0, -1),
                Arguments.of(1, null, ANALOGUE, null, 0, 1),
                Arguments.of(1, null, ANALOGUE, null, 1, -1),
                Arguments.of(1, null, ANALOGUE, null, 1, 1),
                Arguments.of(1, null, ANALOGUE, BigDecimal.ONE, 0, -1),
                Arguments.of(1, null, ANALOGUE, BigDecimal.ONE, 0, 1),
                Arguments.of(1, null, ANALOGUE, BigDecimal.ONE, 1, -1),
                Arguments.of(1, null, ANALOGUE, BigDecimal.ONE, 1, 1),
                Arguments.of(1, "brand", null, null, 0, -1),
                Arguments.of(1, "brand", null, null, 0, 1),
                Arguments.of(1, "brand", null, null, 1, -1),
                Arguments.of(1, "brand", null, null, 1, 1),
                Arguments.of(1, "brand", null, BigDecimal.ONE, 0, -1),
                Arguments.of(1, "brand", null, BigDecimal.ONE, 0, 1),
                Arguments.of(1, "brand", null, BigDecimal.ONE, 1, -1),
                Arguments.of(1, "brand", null, BigDecimal.ONE, 1, 1),
                Arguments.of(1, "brand", ANALOGUE, null, 0, -1),
                Arguments.of(1, "brand", ANALOGUE, null, 0, 1),
                Arguments.of(1, "brand", ANALOGUE, null, 1, -1),
                Arguments.of(1, "brand", ANALOGUE, null, 1, 1),
                Arguments.of(1, "brand", ANALOGUE, BigDecimal.ONE, 0, -1),
                Arguments.of(1, "brand", ANALOGUE, BigDecimal.ONE, 0, 1),
                Arguments.of(1, "brand", ANALOGUE, BigDecimal.ONE, 1, -1)
        );
    }

    @BeforeEach
    void setUp() {
        dao = mock(DAO.class);
        final DAOContainer container = mock(DAOContainer.class);
        when(container.getWatchDAO()).thenReturn(dao);

        manager = new WatchManager(container);
    }

    @Test
    void getAllBelowArgsTrashArgs() throws SQLException {
        when(dao.getAll()).thenReturn(null);

        assertEquals(0, manager.getAll().size());
        verify(dao).getAll();
    }

    @Test
    void getAllBelowCorrectArgs() throws SQLException {
        final Watch watch = mock(Watch.class);

        when(dao.getAll()).thenReturn(Arrays.asList(watch, watch, watch));

        assertEquals(3, manager.getAll().size());
        verify(dao).getAll();
    }

    @Test
    void getAllCatchExceptions() throws SQLException {
        when(dao.getAll()).thenThrow(new SQLException(), new DBException(new Exception()));

        assertEquals(0, manager.getAll().size());
        verify(dao, times(1)).getAll();

        assertEquals(0, manager.getAll().size());
        verify(dao, times(2)).getAll();
    }

    @ParameterizedTest
    @MethodSource("addWatchAboveTrashArgsProvider")
    void addWatchAboveTrashArgs(String brand, Watch.WatchType type, BigDecimal price, int qty,
                                int vendorId) throws SQLException {

        assertFalse(manager.addWatch(brand, type, price, qty, vendorId).isPresent());

        verify(dao, never()).create(any(Watch.class));
    }

    @Test
    void addWatchAboveCorrectArgs() throws SQLException {
        manager.addWatch(" brand ", ANALOGUE, BigDecimal.ONE, 2, 3);
        verify(dao, times(1)).create(any(Watch.class));

        final ArgumentCaptor<Watch> args = ArgumentCaptor.forClass(Watch.class);
        verify(dao).create(args.capture());
        final Watch value = args.getValue();
        assertEquals(-1, value.getId());
        assertEquals("brand", value.getBrand());
        assertEquals(ANALOGUE, value.getType());
        assertEquals(BigDecimal.ONE, value.getPrice());
        assertEquals(2, value.getQty());
        assertEquals(3, value.getVendor().getId());
    }

    /*
        @Test
        void addWatchBelowTrashArgs() throws SQLException {

            when(dao.create(any(Watch.class))).thenReturn(null);
            assertFalse(manager.addWatch("brand", ANALOGUE, BigDecimal.ONE, 1, 2).isPresent());

            //
            final Watch watch1 = mock(Watch.class);
            when(watch1.getId()).thenReturn(-1);
            when(dao.create(any(Watch.class))).thenReturn(watch1);
            assertFalse(manager.addWatch("brand", ANALOGUE, BigDecimal.ONE, 1, 2).isPresent());

            //
            final Watch watch2 = mock(Watch.class);
            when(watch2.getId()).thenReturn(1);
            when(watch2.getVendor()).thenReturn(null);

            when(dao.create(any(Watch.class))).thenReturn(watch2);
            assertFalse(manager.addWatch("brand", ANALOGUE, BigDecimal.ONE, 1, 2).isPresent());

            //
            final Watch watch3 = mock(Watch.class, RETURNS_DEEP_STUBS);
            when(watch3.getId()).thenReturn(1);
            when(watch3.getVendor().getId()).thenReturn(-1);

            when(dao.create(any(Watch.class))).thenReturn(watch3);
            assertFalse(manager.addWatch("brand", ANALOGUE, BigDecimal.ONE, 1, 2).isPresent());
        }
    */
/*
    @Test
    void addWatchBelowCorrectArgs() throws SQLException {
        final Watch watch = new Watch(1, "brand", ANALOGUE, BigDecimal.ONE, 1,
                new Vendor(2, "vendor",
                        new Country(3, "country")));

        when(dao.create(any(Watch.class))).thenReturn(watch);

        assertTrue(manager.addWatch("brand", ANALOGUE, BigDecimal.ONE, 1, 2).isPresent());
    }
*/
    @Test
    void addWatchCatchExceptions() throws SQLException {
        when(dao.create(any(Watch.class))).thenThrow(new SQLException(),
                new DBException(new Exception()));

        assertFalse(manager.addWatch("brand", ANALOGUE, BigDecimal.ONE, 1, 2).isPresent());
        assertFalse(manager.addWatch("brand", ANALOGUE, BigDecimal.ONE, 1, 2).isPresent());
    }

    @ParameterizedTest
    @MethodSource("updateWatchAboveTrashArgsProvider")
    void updateWatchAboveTrashArgs(int watchId, String brand, Watch.WatchType type,
                                   BigDecimal price, int qty, int vendorId) throws SQLException {
        assertFalse(manager.updateWatch(watchId, brand, type, price, qty, vendorId));
        verify(dao, never()).update(any());
    }

    @Test
    void updateWatchAboveCorrectArgs() throws SQLException {
        when(dao.update(any(Watch.class))).thenReturn(true);
        assertTrue(manager.updateWatch(1, " brand ", ANALOGUE, BigDecimal.ONE, 2, 3));

        final ArgumentCaptor<Watch> args = ArgumentCaptor.forClass(Watch.class);
        verify(dao).update(args.capture());
        final Watch value = args.getValue();
        assertEquals(1, value.getId());
        assertEquals("brand", value.getBrand());
        assertEquals(ANALOGUE, value.getType());
        assertEquals(BigDecimal.ONE, value.getPrice());
        assertEquals(2, value.getQty());
        assertEquals(3, value.getVendor().getId());
    }

    @Test
    void updateWatchBelowArgs() throws SQLException {
        when(dao.update(any(Watch.class))).thenReturn(true, false);
        assertTrue(manager.updateWatch(1, " brand ", ANALOGUE, BigDecimal.ONE, 2, 3));
        assertFalse(manager.updateWatch(1, " brand ", ANALOGUE, BigDecimal.ONE, 2, 3));
    }

    @Test
    void updateWatchCatchExceptions() throws SQLException {
        when(dao.update(any(Watch.class))).thenThrow(new SQLException(),
                new DBException(new Exception()));

        assertFalse(manager.updateWatch(1, " brand ", ANALOGUE, BigDecimal.ONE, 2, 3));
        assertFalse(manager.updateWatch(1, " brand ", ANALOGUE, BigDecimal.ONE, 2, 3));
    }

    @Test
    void deleteWatchAboveTrashArgs() throws SQLException {
        assertFalse(manager.deleteWatch(-1));
        verify(dao, never()).delete(anyInt());
    }

    @Test
    void deleteWatchAboveCorrectArgs() throws SQLException {
        when(dao.delete(anyInt())).thenReturn(true);

        assertTrue(manager.deleteWatch(1));
        verify(dao, times(1)).delete(anyInt());
    }

    @Test
    void deleteWatchBelowArgs() throws SQLException {
        when(dao.delete(anyInt())).thenReturn(true, false);
        assertTrue(manager.deleteWatch(1));
        verify(dao, times(1)).delete(eq(1));

        assertFalse(manager.deleteWatch(2));
        verify(dao, times(1)).delete(eq(1));
    }

    @Test
    void deleteWatchCatchExceptions() throws SQLException {
        when(dao.delete(anyInt())).thenThrow(new SQLException(), new DBException(new Exception()));
        assertFalse(manager.deleteWatch(1));
        verify(dao, times(1)).delete(eq(1));

        assertFalse(manager.deleteWatch(2));
        verify(dao, times(1)).delete(eq(2));
    }

    @Test
    void getByTypeAboveTrashArgs() throws SQLException {
        assertEquals(0, manager.getByType(null).size());
        verify(dao, never()).getAll();
    }

    @Test
    void getByTypeAboveCorrectArgs() throws SQLException {
        manager.getByType(ANALOGUE);
        verify(dao, times(1)).getAll();
    }

    @Test
    void getByTypeBelowTrashArgs() throws SQLException {
        when(dao.getAll()).thenReturn(null);
        assertEquals(0, manager.getByType(ANALOGUE).size());
        assertEquals(0, manager.getByType(DIGITAL).size());
    }

    @Test
    void getByTypeBelowArgsCorrect() throws SQLException {
        final Watch analogueWatch = mock(Watch.class);
        when(analogueWatch.getType()).thenReturn(ANALOGUE);

        final Watch digitalWatch = mock(Watch.class);
        when(digitalWatch.getType()).thenReturn(DIGITAL);

        when(dao.getAll()).thenReturn(Arrays.asList(analogueWatch, digitalWatch, analogueWatch,
                digitalWatch, analogueWatch));

        assertEquals(3, manager.getByType(ANALOGUE).size());
        assertEquals(2, manager.getByType(DIGITAL).size());
    }

    @Test
    void getByTypeCatchExceptions() throws SQLException {
        when(dao.getAll()).thenThrow(new SQLException(), new DBException(new Exception()));

        assertEquals(0, manager.getByType(ANALOGUE).size());
        assertEquals(0, manager.getByType(ANALOGUE).size());
    }

    @Test
    void getAnalogueWatchNotGreaterByPriceThanAboveTrashArgs() throws SQLException {
        assertEquals(0, manager.getAnalogueWatchNotGreaterByPriceThan(null).size());
        verify(dao, never()).getAll();
    }

    @Test
    void getAnalogueWatchNotGreaterByPriceThanAboveCorrectArgs() throws SQLException {
        manager.getAnalogueWatchNotGreaterByPriceThan(BigDecimal.ZERO);
        verify(dao, times(1)).getAll();
    }

    @Test
    void getAnalogueWatchNotGreaterByPriceThanBelowTrashArgs() throws SQLException {
        when(dao.getAll()).thenReturn(null);
        assertEquals(0, manager.getAnalogueWatchNotGreaterByPriceThan(BigDecimal.ZERO).size());
        verify(dao, times(1)).getAll();
    }

    @Test
    void getAnalogueWatchNotGreaterByPriceThanBelowCorrectArgs() throws SQLException {
        final Watch aWatch1 = mock(Watch.class);
        when(aWatch1.getType()).thenReturn(ANALOGUE);
        when(aWatch1.getPrice()).thenReturn(BigDecimal.valueOf(100));

        final Watch aWatch2 = mock(Watch.class);
        when(aWatch2.getType()).thenReturn(ANALOGUE);
        when(aWatch2.getPrice()).thenReturn(new BigDecimal("99.99"));

        final Watch aWatch3 = mock(Watch.class);
        when(aWatch3.getType()).thenReturn(ANALOGUE);
        when(aWatch3.getPrice()).thenReturn(new BigDecimal("100.01"));

        final Watch dWatch1 = mock(Watch.class);
        when(dWatch1.getType()).thenReturn(DIGITAL);
        when(dWatch1.getPrice()).thenReturn(BigDecimal.valueOf(10));

        //
        when(dao.getAll()).thenReturn(Arrays.asList(aWatch1, aWatch2, dWatch1, aWatch3));

        final List<Watch> result = manager.getAnalogueWatchNotGreaterByPriceThan(BigDecimal.valueOf(100));
        verify(dao, times(1)).getAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(w -> w.getType() == ANALOGUE));
    }

    @Test
    void getAnalogueWatchNotGreaterByPriceThanCatchSQLExceptions() throws SQLException {
        when(dao.getAll()).thenThrow(new SQLException(), new DBException(new Exception()));

        assertEquals(0, manager.getAnalogueWatchNotGreaterByPriceThan(BigDecimal.TEN).size());
        verify(dao, times(1)).getAll();

        assertEquals(0, manager.getAnalogueWatchNotGreaterByPriceThan(BigDecimal.TEN).size());
        verify(dao, times(2)).getAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @NullSource
    void getByCountryAboveTrashArgs(String args) throws SQLException {
        assertEquals(0, manager.getByCountry(args).size());
        verify(dao, never()).getAll();
    }

    @Test
    void getByCountryAboveCorrectArgs() throws SQLException {
        manager.getByCountry("country");
        verify(dao, times(1)).getAll();
    }

    @Test
    void getByCountryBelowTrashArgs() throws SQLException {
        when(dao.getAll()).thenReturn(null);
        assertEquals(0, manager.getByCountry("country").size());
        verify(dao, times(1)).getAll();
    }

    @Test
    void getByCountryBelowCorrectArgs() throws SQLException {
        final Watch watch1 = mock(Watch.class, RETURNS_DEEP_STUBS);
        when(watch1.getVendor().getCountry().getName()).thenReturn("country 1");

        final Watch watch2 = mock(Watch.class, RETURNS_DEEP_STUBS);
        when(watch2.getVendor().getCountry().getName()).thenReturn("country 2");

        final Watch watch3 = mock(Watch.class, RETURNS_DEEP_STUBS);
        when(watch3.getVendor().getCountry().getName()).thenReturn("country 1");

        final Watch watch4 = mock(Watch.class, RETURNS_DEEP_STUBS);
        when(watch4.getVendor().getCountry().getName()).thenReturn("country 3");
        when(dao.getAll()).thenReturn(Arrays.asList(watch1, watch2, watch3, watch4));

        final List<Watch> result = manager.getByCountry("country 1");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(w -> w.getVendor().getCountry().getName().equals(
                "country 1")));
    }

    @Test
    void getByCountryCatchExceptions() throws SQLException {
        when(dao.getAll()).thenThrow(new SQLException(), new DBException(new Exception()));

        assertEquals(0, manager.getByCountry("country").size());
        verify(dao, times(1)).getAll();

        assertEquals(0, manager.getByCountry("country").size());
        verify(dao, times(2)).getAll();
    }

    @Test
    void getVendorByTotalSumNotGreaterThanAboveTrashArgs() throws SQLException {
        assertEquals(0, manager.getVendorByTotalSumNotGreaterThan(null).size());
        verify(dao, never()).getAll();
    }

    @Test
    void getVendorByTotalSumNotGreaterThanAboveCorrectArgs() throws SQLException {
        assertEquals(0, manager.getVendorByTotalSumNotGreaterThan(BigDecimal.ONE).size());
        verify(dao, times(1)).getAll();
    }

    @Test
    void getVendorByTotalSumNotGreaterThanBelowTrashArgs() throws SQLException {
        when(dao.getAll()).thenReturn(null);
        assertEquals(0, manager.getVendorByTotalSumNotGreaterThan(BigDecimal.ONE).size());
    }

    @Test
    void getVendorByTotalSumNotGreaterThanBelowCorrectArgs() throws SQLException {
        final Vendor v1 = mock(Vendor.class);
        when(v1.getId()).thenReturn(1);
        when(v1.getName()).thenReturn("vendor1");

        final Vendor v2 = mock(Vendor.class);
        when(v2.getId()).thenReturn(2);
        when(v2.getName()).thenReturn("vendor2");

        final Vendor v3 = mock(Vendor.class);
        when(v3.getId()).thenReturn(3);
        when(v3.getName()).thenReturn("vendor3");
        //
        final Watch w1 = mock(Watch.class);
        when(w1.getId()).thenReturn(1);
        when(w1.getVendor()).thenReturn(v1);
        when(w1.getQty()).thenReturn(1);
        when(w1.getPrice()).thenReturn(BigDecimal.valueOf(10));

        final Watch w2 = mock(Watch.class);
        when(w2.getId()).thenReturn(2);
        when(w2.getVendor()).thenReturn(v2);
        when(w2.getQty()).thenReturn(2);
        when(w2.getPrice()).thenReturn(BigDecimal.valueOf(20));

        final Watch w3 = mock(Watch.class);
        when(w3.getId()).thenReturn(3);
        when(w3.getVendor()).thenReturn(v1);
        when(w3.getQty()).thenReturn(3);
        when(w3.getPrice()).thenReturn(BigDecimal.valueOf(30));

        final Watch w4 = mock(Watch.class);
        when(w4.getId()).thenReturn(4);
        when(w4.getVendor()).thenReturn(v2);
        when(w4.getQty()).thenReturn(4);
        when(w4.getPrice()).thenReturn(BigDecimal.valueOf(40));

        final Watch w5 = mock(Watch.class);
        when(w5.getId()).thenReturn(5);
        when(w5.getVendor()).thenReturn(v3);
        when(w5.getQty()).thenReturn(5);
        when(w5.getPrice()).thenReturn(BigDecimal.valueOf(50));

        when(dao.getAll()).thenReturn(Arrays.asList(w5, w3, w2, w4, w1));

        List<Vendor> result = manager.getVendorByTotalSumNotGreaterThan(BigDecimal.valueOf(50));
        assertEquals(0, result.size());

        //
        result = manager.getVendorByTotalSumNotGreaterThan(BigDecimal.valueOf(100));
        assertEquals(1, result.size());
        assertEquals("vendor1", result.get(0).getName());

        //
        result = manager.getVendorByTotalSumNotGreaterThan(BigDecimal.valueOf(200));
        assertEquals(2, result.size());
        result.sort(Comparator.comparing(Vendor::getId));
        assertEquals("vendor1", result.get(0).getName());
        assertEquals("vendor2", result.get(1).getName());

        //
        result = manager.getVendorByTotalSumNotGreaterThan(BigDecimal.valueOf(250));
        assertEquals(3, result.size());
        result.sort(Comparator.comparing(Vendor::getId));
        assertEquals("vendor1", result.get(0).getName());
        assertEquals("vendor2", result.get(1).getName());
        assertEquals("vendor3", result.get(2).getName());
    }

    @Test
    void getVendorByTotalSumNotGreaterThanCatchExceptions() throws SQLException {
        when(dao.getAll()).thenThrow(new SQLException(), new DBException(new Exception()));
        assertEquals(0, manager.getAnalogueWatchNotGreaterByPriceThan(BigDecimal.ONE).size());
        assertEquals(0, manager.getAnalogueWatchNotGreaterByPriceThan(BigDecimal.ONE).size());
    }
}