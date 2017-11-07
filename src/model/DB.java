package model;

import java.sql.*;

public class DB {
    private static final String DBNAME = "test";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";
    private static final String JDBCDRIVER = "com.mysql.jdbc.Driver";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBCDRIVER);
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DBNAME, USERNAME, PASSWORD);
    }

}
