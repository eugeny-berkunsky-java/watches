package manage;

import model.Country;
import model.DAO;
import model.DAOFactory;
import model.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import utils.DBException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class VendorManagerTest {

    private VendorManager manager;
    private DAO<Vendor> dao;

    @BeforeEach
    public void init() {

        dao = mock(DAO.class);

        DAOFactory factory = mock(DAOFactory.class);
        when(factory.getVendorsDAO()).thenReturn(dao);

        manager = new VendorManager(factory);
    }

    @Test
    void getAll() throws SQLException {
        assertEquals(0, manager.getAll().size());
        verify(dao, times(1)).getAll();

        //
        when(dao.getAll()).thenReturn(null);
        assertEquals(0, manager.getAll().size());
    }

    @Test
    void getAllSQLException() throws SQLException {
        when(dao.getAll()).thenThrow(new SQLException());

        assertEquals(0, manager.getAll().size());
        verify(dao, times(1)).getAll();
    }

    @Test
    void getAllDBException() throws SQLException {
        when(dao.getAll()).thenThrow(new DBException(new Exception()));

        assertEquals(0, manager.getAll().size());
        verify(dao, times(1)).getAll();
    }

    @Test
    void addVendorAboveTrashArgs() throws SQLException {
        assertFalse(manager.addVendor(null, 1).isPresent());
        verify(dao, times(0)).create(any());

        assertFalse(manager.addVendor("abc", -1).isPresent());
        verify(dao, times(0)).create(any());

        assertFalse(manager.addVendor(" ", -1).isPresent());
        verify(dao, times(0)).create(any());

        assertFalse(manager.addVendor(" ", 1).isPresent());
        verify(dao, times(0)).create(any());

        assertFalse(manager.addVendor(null, -1).isPresent());
        verify(dao, times(0)).create(any());
    }

    @Test
    void addVendorAboveCorrectArgs() throws SQLException {
        manager.addVendor("  abc  ", 2);
        final ArgumentCaptor<Vendor> args = ArgumentCaptor.forClass(Vendor.class);
        verify(dao).create(args.capture());
        final Vendor value = args.getValue();
        assertEquals(-1, value.getId());
        assertEquals("abc", value.getVendorName());
        assertEquals(2, value.getCountry().getId());
    }

    @Test
    void addVendorBelowTrashArgs() throws SQLException {
        when(dao.create(any())).thenReturn(null);

        assertFalse(manager.addVendor("abc", 1).isPresent());
        verify(dao, times(1)).create(any());
    }

    @Test
    void addVendorBelowCorrectArgs() throws SQLException {
        final Vendor vendor = new Vendor(1, "abc", new Country(1, "abc"));
        when(dao.create(any())).thenReturn(vendor);

        assertTrue(manager.addVendor("abc", 1).isPresent());
    }

    @Test
    void addVendorCatchSQLException() throws SQLException {
        when(dao.create(any())).thenThrow(new SQLException());

        assertFalse(manager.addVendor("abc", 1).isPresent());
        verify(dao, times(1)).create(any());
    }

    @Test
    void addVendorCatchDBException() throws SQLException {
        when(dao.create(any())).thenThrow(new DBException(new SQLException()));

        assertFalse(manager.addVendor("abc", 1).isPresent());
        verify(dao, times(1)).create(any());
    }

    @Test
    void updateVendorAboveTrashArgs() throws SQLException {
        assertFalse(manager.updateVendor(null));
        verify(dao, times(0)).update(any());

        assertFalse(manager.updateVendor(new Vendor(-1, "abc", new Country(-1, "abc"))));
        verify(dao, times(0)).update(any());

        assertFalse(manager.updateVendor(new Vendor(1, "", new Country(1, null))));
        verify(dao, times(0)).update(any());

        assertFalse(manager.updateVendor(new Vendor(1, "  ", new Country(1, null))));
        verify(dao, times(0)).update(any());

        assertFalse(manager.updateVendor(new Vendor(-1, "abc", null)));
        verify(dao, times(0)).update(any());

        assertFalse(manager.updateVendor(new Vendor(1, "abc", null)));
        verify(dao, times(0)).update(any());

        assertFalse(manager.updateVendor(new Vendor(1, "abc", new Country(-1, "abc"))));
        verify(dao, times(0)).update(any());
    }

    @Test
    void updateVendorAboveCorrectArgs() throws SQLException {
        final Vendor vendor = new Vendor(10, "  abc  ", new Country(20, null));

        final ArgumentCaptor<Vendor> args = ArgumentCaptor.forClass(Vendor.class);

        manager.updateVendor(vendor);
        verify(dao, times(1)).update(args.capture());
        final Vendor value = args.getValue();
        assertEquals(10, value.getId());
        assertEquals("abc", value.getVendorName());
        assertEquals(20, value.getCountry().getId());
    }

    @Test
    void updateVendorBelowArgs() throws SQLException {
        final Vendor vendor = new Vendor(10, "  abc  ", new Country(20, null));

        when(dao.update(any())).thenReturn(true);
        assertTrue(manager.updateVendor(vendor));

        //
        when(dao.update(any())).thenReturn(false);
        assertFalse(manager.updateVendor(vendor));
    }

    @Test
    void updateVendorSQLException() throws SQLException {
        final Vendor vendor = new Vendor(10, "  abc  ", new Country(20, null));

        when(dao.update(any())).thenThrow(new SQLException());
        assertFalse(manager.updateVendor(vendor));
    }

    @Test
    void updateVendorDBException() throws SQLException {
        final Vendor vendor = new Vendor(10, "  abc  ", new Country(20, null));

        when(dao.update(any())).thenThrow(new DBException(new SQLException()));
        assertFalse(manager.updateVendor(vendor));
    }


    @Test
    void deleteVendorAboveTrashArgs() throws SQLException {
        assertFalse(manager.deleteVendor(-1));
        verify(dao, times(0)).delete(anyInt());
    }

    @Test
    void deleteVendorAboveCorrectArgs() throws SQLException {
        when(dao.delete(anyInt())).thenReturn(true);

        assertTrue(manager.deleteVendor(10));
        verify(dao, times(1)).delete(anyInt());
    }

    @Test
    void deleteVendorBelowArgs1() throws SQLException {
        when(dao.delete(anyInt())).thenReturn(true);
        assertTrue(manager.deleteVendor(1));
        verify(dao, times(1)).delete(anyInt());
    }

    @Test
    void deleteVendorBelowArgs2() throws SQLException {
        when(dao.delete(anyInt())).thenReturn(false);
        assertFalse(manager.deleteVendor(1));
        verify(dao, times(1)).delete(anyInt());
    }

    @Test
    void deleteVendorCatchSQLException() throws SQLException {
        when(dao.delete(anyInt())).thenThrow(new SQLException());
        assertFalse(manager.deleteVendor(10));
    }

    @Test
    void deleteVendorCatchDBException() throws SQLException {
        when(dao.delete(anyInt())).thenThrow(new DBException(new SQLException()));
        assertFalse(manager.deleteVendor(10));
    }
}