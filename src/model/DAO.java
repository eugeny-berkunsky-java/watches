package model;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    T create(T model) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws SQLException;

    boolean update(T model) throws SQLException;

    boolean delete(int id) throws SQLException;
}
