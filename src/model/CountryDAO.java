package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryDAO {
    public List<Country> countries;
    Country ct1 = new Country(1,"Italy");//Cartier
    Country ct2 = new Country(2,"France");//Bell & Ross
    Country ct3 = new Country(3,"UK");//Bremont
    Country ct4 = new Country(4,"China");//Beijing Watch Factory

    public List<Country> generateCountries(){
        countries=new ArrayList<>();
        Collections.addAll(countries,ct1, ct2, ct3, ct4);
        return countries;
    }
}
