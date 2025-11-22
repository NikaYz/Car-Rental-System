import java.sql.*;

public class DriverService {
    private DatabaseManager dbManager;
    
    public DriverService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    
    public void addDriver(String fname, String lname, long phoneNo, String carType, 
                         String carModel, String carNo) throws SQLException {
        long drivId = getNextDriverId();
        String insertDriver = "INSERT INTO drivers values (" + drivId + ", '" + fname +  
                             "', '" + lname + "', " + phoneNo + ", '" + carType + 
                             "', '" + carModel + "', '" + carNo + "', 1);";
        dbManager.executeUpdate(insertDriver);
    }
    
    public void updateDriver(long driverId, String fname, String lname, long phoneNo, 
                           String carType, String carModel, String carNo) throws SQLException {
        String updateQuery = "UPDATE drivers SET phone_no = " + phoneNo + 
                           ", first_name = '" + fname + "', last_name = '" + lname + 
                           "', car_model = '" + carModel + "', car_no = '" + carNo + 
                           "', car_type = '" + carType + "' WHERE driver_id = " + driverId + ";";
        dbManager.executeUpdate(updateQuery);
    }
    
    public void deactivateDriver(long driverId) throws SQLException {
        String query = "UPDATE drivers SET working_in_company = false WHERE driver_id = " + driverId + ";";
        dbManager.executeUpdate(query);
    }
    
    public ResultSet getDriverByPhone(long phoneNo) throws SQLException {
        String query = "SELECT * from drivers where " + phoneNo + " = drivers.phone_no;";
        return dbManager.executeQuery(query);
    }
    
    public ResultSet getAllDrivers() throws SQLException {
        return dbManager.executeQuery("SELECT * from drivers;");
    }
    
    public ResultSet getAvailableDrivers(String startDate, String endDate) throws SQLException {
        String query = "SELECT d.driver_id, d.car_model, d.car_type, cp.rent_per_day FROM drivers d " +
                      "JOIN carinfo cp ON d.car_type = cp.type " + 
                      "LEFT JOIN rentalinfo r ON d.driver_id = r.driver_id " + 
                      "where (r.driver_id IS NULL " + 
                      "OR NOT (r.rental_end >= '" + startDate + "' AND r.rental_start <= '" + endDate + "' )) " +
                      "AND d.working_in_company = TRUE;";
        return dbManager.executeQuery(query);
    }
    
    public int getDriverCount() throws SQLException {
        ResultSet rs = dbManager.executeQuery("SELECT count(*) AS record_count FROM drivers;");
        int count = 0;
        while(rs.next()) {
            count = rs.getInt("record_count");
        }
        rs.close();
        return count;
    }
    
    private long getNextDriverId() throws SQLException {
        ResultSet rs = dbManager.executeQuery("SELECT count(*) AS record_count FROM drivers;");
        long count = 0;
        while(rs.next()) {
            count = rs.getLong("record_count") + 1;
        }
        rs.close();
        return count;
    }
}