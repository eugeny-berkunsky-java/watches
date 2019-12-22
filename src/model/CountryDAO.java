package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CountryDAO {

    private int id = 0;
    private List<Country> countries;

    public CountryDAO() {
        countries = new ArrayList<>();
    }

    private int getNextId() {
        return ++id;
    }

    private void updateCounter() {
        id = Collections.max(countries, Comparator.comparingInt(Country::getId)).getId();
    }

    public void generateCountries() {

        countries.addAll(Arrays.asList(
                new Country(1, "Italy"),
                new Country(2, "France"),
                new Country(3, "UK"),
                new Country(4, "China")
        ));

        updateCounter();
    }

    public void load(String filename) {
        List<Country> result = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {

            while (reader.ready()) {
                Country country = stringToCountry(reader.readLine());
                if (country != null) {
                    result.add(country);
                }
            }

            countries = result;

            updateCounter();

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void save(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            countries.stream()
                    .map(this::countryToString)
                    .forEach(writer::println);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public Country create(String name) {
        if (name == null || name.trim().length() == 0) {
            return null;
        }

        Country country = getByName(name);
        if (country != null) {
            return country;
        } else {
            int id = getNextId();
            countries.add(new Country(id, name));
            return getById(id);
        }
    }

    public List<Country> getAll() {
        return new ArrayList<>(countries);
    }

    public Country getById(int id) {
        return countries.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Country getByName(String name) {
        return countries.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void delete(Country country) {
        if (country == null) {
            return;
        }

        countries.removeIf(c -> c.getId() == country.getId());
    }


    private Country stringToCountry(String line) {
        try {
            String[] elements = line.split("\\|");
            int id = Integer.parseInt(elements[0].trim());
            String name = elements[1].trim();
            return new Country(id, name);
        } catch (Exception e) {
            return null;
        }
    }

    private String countryToString(Country country) {
        return String.format("%d|%s", country.getId(), country.getName());
    }
}
