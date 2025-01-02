package com.jawaskrip;

import java.sql.*;

public class DatabaseUtil {

    private static final String url = "jdbc:postgresql://localhost:5432/master";
    private static final String user = "postgres";
    private static final String password = "youlaughyoulose1";



    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
