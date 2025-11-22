import java.sql.*;

public class RentalService {
    private DatabaseManager dbManager;
    
    public RentalService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    
    public void bookCar(long customerId, int driverId, String carType, 
                       String startDate, String endDate, int totalAmount) throws SQLException {
        String query = "INSERT INTO rentalinfo values (" + customerId + ", " + driverId +  
                      ", '" + carType + "', '" + startDate + "', '" + endDate + 
                      "', " + totalAmount + ");";
        dbManager.executeUpdate(query);
    }
    
    public ResultSet getRentalDetails(long customerId, String startDate) throws SQLException {
        String query = "SELECT driver_id from rentalinfo where customer_id = " + customerId + 
                      " AND rental_start = '" + startDate + "';";
        ResultSet rs = dbManager.executeQuery(query);
        long drivId = 0;
        while(rs.next()) {
            drivId = rs.getLong("driver_id");
        }
        rs.close();
        
        query = "SELECT c.first_name as customer_f, c.last_name as customer_l, c.phoneno as customer_p, " +
                "d.first_name, d.last_name,d.phone_no,d.car_type,d.car_model,d.car_no," +
                "r.rental_start,r.rental_end, r.total_amount from rentalinfo r " +
                "join customers c on r.customer_id = " + customerId + 
                " join drivers d on r.driver_id = " + drivId + 
                " where (r.customer_id = " + customerId + " and r.driver_id = " + drivId + ") limit 1;";
        return dbManager.executeQuery(query);
    }
    
    public int getRentalCount(String period, String year, String month) throws SQLException {
        String query;
        if (period.equals("yearly")) {
            query = "SELECT COUNT(*) AS record_count FROM rentalinfo WHERE YEAR(rental_start) = '" + year + "';";
        } else {
            query = "SELECT COUNT(*) AS record_count FROM rentalinfo WHERE YEAR(rental_start) = '" + year + 
                   "' AND MONTH(rental_start) = '" + month + "';";
        }
        ResultSet rs = dbManager.executeQuery(query);
        int count = 0;
        while(rs.next()) {
            count = rs.getInt("record_count");
        }
        rs.close();
        return count;
    }
    
    public long getTotalSales(String period, String year, String month) throws SQLException {
        String query;
        if (period.equals("yearly")) {
            query = "SELECT SUM(total_amount) AS total FROM rentalinfo WHERE YEAR(rental_start) = '" + year + "';";
        } else {
            query = "SELECT SUM(total_amount) AS total FROM rentalinfo WHERE YEAR(rental_start) = '" + year + 
                   "' AND MONTH(rental_start) = '" + month + "';";
        }
        ResultSet rs = dbManager.executeQuery(query);
        long total = 0;
        while(rs.next()) {
            total = rs.getLong("total");
        }
        rs.close();
        return total;
    }
    
    public ResultSet getDriverPerformance(int sortBy) throws SQLException {
        String query;
        if (sortBy == 1) {
            query = "SELECT r.driver_id, d.first_name, d.last_name, COUNT(*) AS tours " +
                   "FROM rentalinfo r JOIN drivers d ON r.driver_id = d.driver_id " +
                   "GROUP BY r.driver_id, d.first_name, d.last_name ORDER BY TOURS;";
        } else if (sortBy == 2) {
            query = "SELECT r.driver_id, d.first_name, d.last_name, SUM(total_amount) AS amount " +
                   "FROM rentalinfo r JOIN drivers d ON r.driver_id = d.driver_id " +
                   "GROUP BY r.driver_id, d.first_name, d.last_name ORDER BY amount;";
        } else {
            query = "SELECT r.driver_id, d.first_name, d.last_name, COUNT(*) AS tours, " +
                   "SUM(r.total_amount) AS amount FROM rentalinfo r " +
                   "JOIN drivers d ON r.driver_id = d.driver_id " +
                   "GROUP BY r.driver_id, d.first_name, d.last_name " +
                   "ORDER BY amount ASC, tours ASC;";
        }
        return dbManager.executeQuery(query);
    }
    
    public ResultSet getCarTypePerformance(int sortBy) throws SQLException {
        String query;
        if (sortBy == 1) {
            query = "SELECT car_type, SUM(total_amount) as amount from rentalinfo GROUP BY car_type ORDER BY amount;";
        } else if (sortBy == 2) {
            query = "SELECT car_type, count(*) as tours from rentalinfo GROUP BY car_type ORDER BY tours;";
        } else {
            query = "SELECT car_type, SUM(total_amount) as amount, COUNT(*) as tours " +
                   "from rentalinfo GROUP BY car_type ORDER BY amount,tours;";
        }
        return dbManager.executeQuery(query);
    }
    
    public ResultSet getAllRentalInfo() throws SQLException {
        return dbManager.executeQuery("SELECT * from rentalinfo;");
    }
    
    public int getRentalInfoCount() throws SQLException {
        ResultSet rs = dbManager.executeQuery("SELECT count(*) AS record_count FROM rentalinfo;");
        int count = 0;
        while(rs.next()) {
            count = rs.getInt("record_count");
        }
        rs.close();
        return count;
    }
}