package model;

import utils.DBException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    default T executeAndReturnObject(PreparedStatement st, Function<ResultSet, T> toObject) {
        try {
            final ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return toObject.apply(resultSet);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    T create(T model) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws SQLException;

    boolean update(T model) throws SQLException;

    boolean delete(int id) throws SQLException;
}
