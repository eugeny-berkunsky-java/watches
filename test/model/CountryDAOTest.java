package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CountryDAOTest {

    private CountryDAO dao;

    private static void writeToFile(Path filename, String... lines) {
        if (filename == null || lines == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(filename.toFile())) {
            Arrays.stream(lines).forEach(writer::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
        }
    }

    private static List<String> readFromFile(Path filename) {
        if (filename == null) {
            return Collections.emptyList();
        }

        try (BufferedReader reader = Files.newBufferedReader(filename)) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return Collections.emptyList();
        }
    }

    @BeforeEach
    public void init() {
        dao = new CountryDAO();
    }

    @Test
    void generateCountries() {
        assertEquals(0, dao.getAll().size());

        dao.generateCountries();
        assertEquals(4, dao.getAll().size());
    }

    @Test
    void load(@TempDir Path tempDir) {
        assertEquals(0, dao.getAll().size());

        Path filename = tempDir.resolve("countries-load-test.txt");
        writeToFile(filename,
                " 1 | country 1",
                " 2 | country 2",
                "bad string",
                "",
                " 3 | country 3",
                " 4 | country 4 |"
        );

        dao.load(filename.toString());
        assertEquals(4, dao.getAll().size());
        assertEquals(1, dao.getAll().get(0).getId());
        assertEquals(4, dao.getAll().get(3).getId());
    }

    @Test
    void save(@TempDir Path tempDir) {
        Path filename = tempDir.resolve("countries-save-test.txt");

        dao.generateCountries();
        dao.save(filename.toString());

        List<String> lines = readFromFile(filename);
        assertEquals(4, lines.size());
        assertEquals("1|Italy", lines.get(0));
        assertEquals("2|France", lines.get(1));
        assertEquals("3|UK", lines.get(2));
        assertEquals("4|China", lines.get(3));
    }

    @Test
    void getAll() {
        assertEquals(0, dao.getAll().size());

        dao.generateCountries();
        assertEquals(4, dao.getAll().size());

        List<Country> countries = dao.getAll();
        countries.add(new Country(5, "test country"));
        assertEquals(4, dao.getAll().size());
    }

    @Test
    void getById() {
        assertEquals(0, dao.getAll().size());

        dao.generateCountries();
        assertEquals(1, dao.getById(1).getId());
        assertNull(dao.getById(-1));
    }

    @Test
    void getByName() {
        assertEquals(0, dao.getAll().size());

        dao.generateCountries();
        Country country = dao.getByName("italy");
        assertNotNull(country);
        assertEquals(1, country.getId());
        assertEquals("Italy", country.getName());

        assertNull(dao.getByName(null));
        assertNull(dao.getByName(""));
        assertNull(dao.getByName("test country"));

    }


    @Test
    void create() {
        assertEquals(0, dao.getAll().size());

        dao.generateCountries();
        assertEquals(4, dao.getAll().size());

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
        dao.generateCountries();
        assertEquals(4, dao.getAll().size());

        dao.delete(new Country(-1, ""));
        assertEquals(4, dao.getAll().size());

        Country country = dao.getById(1);
        dao.delete(country);
        assertEquals(3, dao.getAll().size());
    }
}