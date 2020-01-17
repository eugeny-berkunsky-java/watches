package com.company.watches.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private static InitialContext context;

    private static TConnection tConnection;


    public static Connection getConnection() {
        try {
            if (context == null) {
                context = new InitialContext();
            }

            if (tConnection != null) {
                return tConnection;
            } else {
                return ((DataSource) context.lookup("java:/comp/env/jdbc/watches")).getConnection();
            }
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startTransaction() {
        try {
            if (tConnection == null) {
                tConnection = new TConnection(getConnection());
                tConnection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public static void endTransaction() {
        try {
            if (tConnection != null) {
                tConnection.setAutoCommit(true);
                tConnection.getConnection().close();
                tConnection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
}
