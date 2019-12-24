package model;

import java.util.List;

public interface DAO<T> {
    T create(T model);

    List<T> getAll();

    T getById(int id);

    boolean update(T model);

    boolean delete(int id);
}
