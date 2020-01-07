package manage;

import model.Country;
import model.DAO;
import model.DAOFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import utils.DBException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountriesManagerTest {

    private DAO<Country> countryDAO;
    private CountriesManager manager;

    @BeforeEach
    void setUp() {
        countryDAO = Mockito.mock(DAO.class);
        DAOFactory factory = Mockito.mock(DAOFactory.class);
        when(factory.getCountriesDAO()).thenReturn(countryDAO);
        manager = new CountriesManager(factory);

    }

    @Test
    void getAll() throws SQLException {
        when(countryDAO.getAll()).thenReturn(Collections.emptyList());
        assertEquals(0, manager.getAll().size());

        //
        final Country country = new Country(-1, "");
        when(countryDAO.getAll()).thenReturn(Arrays.asList(country, country));
        assertEquals(2, manager.getAll().size());

        //
        when(countryDAO.getAll()).thenReturn(null);
        assertEquals(0, manager.getAll().size());
    }

    @Test
    void getAllAndCatchSQLException() throws SQLException {
        when(countryDAO.getAll()).thenThrow(new SQLException());
        assertEquals(0, manager.getAll().size());
    }

    @Test
    void getAllAndCatchDBException() throws SQLException {
        when(countryDAO.getAll()).thenThrow(new DBException(new Exception()));
        assertEquals(0, manager.getAll().size());
    }

    @Test
    void addCountryErrorArgsAbove() throws SQLException {
        assertFalse(manager.addCountry(null).isPresent());
        Mockito.verify(countryDAO, Mockito.times(0)).create(Mockito.any());

        assertFalse(manager.addCountry("").isPresent());
        Mockito.verify(countryDAO, Mockito.times(0)).create(Mockito.any());

        assertFalse(manager.addCountry("  ").isPresent());
        Mockito.verify(countryDAO, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void addCountryErrorArgsBelow() throws SQLException {
        when(countryDAO.create(Mockito.any())).thenReturn(null);
        assertFalse(manager.addCountry("abc").isPresent());
        Mockito.verify(countryDAO, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void addCountryTrim() throws SQLException {
        manager.addCountry(" abc  ");

        final ArgumentCaptor<Country> args = ArgumentCaptor.forClass(Country.class);
        Mockito.verify(countryDAO).create(args.capture());


        final Country value = args.getValue();

        assertEquals(-1, value.getId());
        assertEquals("abc", value.getName());
    }

    @Test
    void updateCountryCheckAboveArgs() throws SQLException {
        assertFalse(manager.updateCountry(-1, "abc"));
        Mockito.verify(countryDAO, Mockito.times(0)).update(Mockito.any());

        assertFalse(manager.updateCountry(1, ""));
        Mockito.verify(countryDAO, Mockito.times(0)).update(Mockito.any());

        assertFalse(manager.updateCountry(1, "  "));
        Mockito.verify(countryDAO, Mockito.times(0)).update(Mockito.any());

        assertFalse(manager.updateCountry(1, null));
        Mockito.verify(countryDAO, Mockito.times(0)).update(Mockito.any());
    }


    @Test
    void updateCountryCheckBelowArgs() throws SQLException {
        when(manager.updateCountry(1, " abc ")).thenReturn(true);
        assertTrue(manager.updateCountry(1, " abc "));

        final ArgumentCaptor<Country> args = ArgumentCaptor.forClass(Country.class);
        Mockito.verify(countryDAO, Mockito.times(1)).update(Mockito.any());
        Mockito.verify(countryDAO).update(args.capture());

        final Country value = args.getValue();
        assertEquals(1, value.getId());
        assertEquals("abc", value.getName());
    }


    @Test
    void updateCountryCheckSQLException() throws SQLException {
        //
        when(countryDAO.update(Mockito.any())).thenThrow(new SQLException());
        assertFalse(manager.updateCountry(1, " abc "));
    }

    @Test
    void updateCountryCheckDBException() throws SQLException {
        when(countryDAO.update(Mockito.any())).thenThrow(new DBException(new Exception()));
        assertFalse(manager.updateCountry(1, " abc "));
    }

    @Test
    void deleteCountry() throws SQLException {
        assertFalse(manager.deleteCountry(-1));
        Mockito.verify(countryDAO, Mockito.times(0)).delete(-1);
    }

    @Test
    void deleteCountryCatchDBException() throws SQLException {
        when(countryDAO.delete(1)).thenThrow(new DBException(new Exception()));

        assertFalse(manager.deleteCountry(1));
        verify(countryDAO, times(1)).delete(1);
    }

    @Test
    void deleteCountryCatchSQLException() throws SQLException {
        when(countryDAO.delete(1)).thenThrow(new SQLException());

        assertFalse(manager.deleteCountry(1));
        verify(countryDAO, times(1)).delete(1);
    }

    @Test
    void deleteCountryCheckArgs() throws SQLException {
        manager.deleteCountry(1);

        verify(countryDAO).delete(1);
    }
}