import java.sql.*;

public class UserService {
    private DatabaseManager dbManager;
    
    public UserService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    
    public String authenticate(String username, String password) throws SQLException {
        String query = "SELECT role as ro FROM users where (username = '" + username + 
                      "' and password = '" + password + "');";
        ResultSet rs = dbManager.executeQuery(query);
        String role = "";
        while(rs.next()) {
            role = rs.getString("ro");
        }
        rs.close();
        return role;
    }
    
    public void addEmployee(int empId, String username, String password, String role) throws SQLException {
        String query = "INSERT INTO users values (" + empId + ", '" + username + 
                      "', '" + password + "','" + role + "');";
        dbManager.executeUpdate(query);
    }
    
    public void deleteEmployee(String username, String password) throws SQLException {
        String query = "DELETE from users where (username = '" + username + 
                       "' and password = '" + password + "');";
        dbManager.executeUpdate(query);
    }
    
    public int getNextUserId() throws SQLException {
        ResultSet rs = dbManager.executeQuery("SELECT COUNT(*) AS record_count FROM users;");
        int count = 0;
        while(rs.next()) {
            count = rs.getInt("record_count") + 1;
        }
        rs.close();
        return count;
    }
}