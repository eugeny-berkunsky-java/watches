package model;

import utils.DBException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DAO<T> {

    default List<T> executeAndReturnCollection(PreparedStatement st,
                                               Function<ResultSet, T> toObject) {
        try {
            final ResultSet resultSet = st.executeQuery();
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(toObject.apply(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    default Optional<T> executeAndReturnObject(PreparedStatement st, Function<ResultSet, T> toObject) {
        try {
            final ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                final T result = toObject.apply(resultSet);
                return result == null ? Optional.empty() : Optional.of(result);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    Optional<T> create(T model) throws SQLException;

    List<T> getAll() throws SQLException;

    Optional<T> getById(int id) throws SQLException;

    boolean update(T model) throws SQLException;

    boolean delete(int id) throws SQLException;
}
