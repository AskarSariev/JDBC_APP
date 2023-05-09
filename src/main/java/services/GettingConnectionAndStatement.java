package services;

import java.sql.*;

public class GettingConnectionAndStatement {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/developer_db";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private final static String USER = "bestuser";
    private final static String PASSWORD = "bestuser";

    public static Connection getConnection() {
        Connection connection;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sqlExpression) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sqlExpression, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return preparedStatement;
    }
}
