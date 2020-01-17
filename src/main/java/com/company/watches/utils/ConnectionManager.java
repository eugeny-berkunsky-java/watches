package com.company.watches.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private static InitialContext context;

    private static ThreadLocal<TConnection> tConnection = new ThreadLocal<>();

    public static Connection getConnection() {
        try {
            if (context == null) {
                context = new InitialContext();
            }

            return tConnection.get() != null
                    ? tConnection.get()
                    : ((DataSource) context.lookup("java:/comp/env/jdbc/watches")).getConnection();

        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startTransaction() {
        try {

            if (tConnection.get() == null) {
                tConnection.set(new TConnection(getConnection()));
                tConnection.get().setAutoCommit(false);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public static void endTransaction() {
        try {
            if (tConnection.get() != null) {
                tConnection.get().setAutoCommit(true);
                tConnection.get().getConnection().close();
                tConnection.remove();
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
}
