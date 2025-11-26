import java.sql.*;

public class DatabaseManager {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    // private static final String DB_URL = "jdbc:mysql://localhost:3306/vroomrentals?useSSL=false";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vroomrentals?useSSL=false&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    private static final String PASSWORD = "SoftwareTesting";
    
    private Connection connection;
    
    public DatabaseManager() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        this.connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
    public DatabaseManager(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }
    
    public int executeUpdate(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(query);
    }
    
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }
    
    public void commit() throws SQLException {
        connection.commit();
    }
    
    public void rollback() {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}