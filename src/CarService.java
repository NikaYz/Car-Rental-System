import java.sql.*;

public class CarService {
    private DatabaseManager dbManager;
    
    public CarService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    
    public void addCarModel(String type, int seater, String description, int rentPerDay) throws SQLException {
        int id = getNextCarId();
        String query = "INSERT INTO carinfo values (" + id + ", '" + type + 
                      "', " + seater + ", '" + description + "', " + rentPerDay + ");";
        dbManager.executeUpdate(query);
    }
    
    public ResultSet getAvailableCarTypes() throws SQLException {
        return dbManager.executeQuery("SELECT type FROM carinfo;");
    }
    
    public ResultSet getAllCarInfo() throws SQLException {
        return dbManager.executeQuery("SELECT type, seater, description, rent_per_day from carInfo");
    }
    
    public int getRentPerDay(String carType) throws SQLException {
        String query = "SELECT rent_per_day from carinfo where '" + carType + "' = carinfo.type;";
        ResultSet rs = dbManager.executeQuery(query);
        int price = 0;
        while(rs.next()) {
            price = rs.getInt("rent_per_day");
        }
        rs.close();
        return price;
    }
    
    private int getNextCarId() throws SQLException {
        ResultSet rs = dbManager.executeQuery("SELECT COUNT(*) AS record_count FROM carinfo;");
        int count = 0;
        while(rs.next()) {
            count = rs.getInt("record_count") + 1;
        }
        rs.close();
        return count;
    }
}