package ru.somarov.templates.java.concurrency.thread_confinement.thread_local;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ThreadLocalImplementation {

    private static final String DB_URL = "";
    private static ThreadLocal<Connection> connectionHolder
            = ThreadLocal.withInitial(() -> {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    });

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}
