import java.sql.*;

public class CustomerService {
    private DatabaseManager dbManager;
    
    public CustomerService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    
    public void addCustomer(String fname, String lname, String email, long phoneNo) throws SQLException {
        int custId = getNextCustomerId();
        String query = "INSERT INTO customers values (" + custId + ", '" + fname + 
                       "', '" + lname + "', '" + email + "', " + phoneNo + ");";
        dbManager.executeUpdate(query);
    }
    
    public void updateCustomer(long customerId, String fname, String lname, String email, long phoneNo) throws SQLException {
        String query = "UPDATE customers SET phoneno = " + phoneNo + 
                      ", email = '" + email + "', first_name = '" + fname + 
                      "', last_name = '" + lname + "' WHERE customer_id = " + customerId + ";";
        dbManager.executeUpdate(query);
    }
    
    public ResultSet getCustomerByPhone(long phoneNo) throws SQLException {
        String query = "SELECT * from customers where " + phoneNo + " = customers.phoneno;";
        return dbManager.executeQuery(query);
    }
    
    public ResultSet getAllCustomers() throws SQLException {
        return dbManager.executeQuery("SELECT * from customers;");
    }
    
    public int getCustomerCount() throws SQLException {
        ResultSet rs = dbManager.executeQuery("SELECT count(*) AS record_count FROM customers;");
        int count = 0;
        while(rs.next()) {
            count = rs.getInt("record_count");
        }
        rs.close();
        return count;
    }
    
    private int getNextCustomerId() throws SQLException {
        ResultSet rs = dbManager.executeQuery("SELECT COUNT(*) AS record_count FROM customers;");
        int count = 0;
        while(rs.next()) {
            count = rs.getInt("record_count") + 1;
        }
        rs.close();
        return count;
    }
}