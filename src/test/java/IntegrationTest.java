import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    private DatabaseManager dbManager;
    private DriverService driverService;
    private UserService userService;
    private CustomerService customerService;
    private CarService carService;
    private RentalService rentalService;

    // H2 Configuration
    private static final String H2_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String H2_USER = "sa";
    private static final String H2_PASS = "";

    @BeforeEach
    void setUp() throws Exception {
        dbManager = new DatabaseManager(H2_URL, H2_USER, H2_PASS);
        
        try (Statement stmt = dbManager.getConnection().createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS drivers (" +
                         "driver_id INT, " + 
                         "first_name VARCHAR(30) NOT NULL, " +
                         "last_name VARCHAR(30), " +
                         "phone_no BIGINT UNIQUE, " +
                         "car_type VARCHAR(30) NOT NULL, " +
                         "car_model VARCHAR(30) NOT NULL, " +
                         "car_no INT NOT NULL, " + 
                         "working_in_company BOOLEAN DEFAULT TRUE, " +
                         "PRIMARY KEY (driver_id, car_no))");


            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "user_id INT PRIMARY KEY, " +
                         "username VARCHAR(255) UNIQUE, " +
                         "password VARCHAR(255), " +
                         "role VARCHAR(30))");


            stmt.execute("CREATE TABLE IF NOT EXISTS carinfo (" +
                         "id INT UNIQUE, " +
                         "type VARCHAR(10) PRIMARY KEY, " +
                         "seater SMALLINT NOT NULL, " +
                         "description VARCHAR(255), " +
                         "rent_per_day INT NOT NULL)");

        
            stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                         "customer_id INT PRIMARY KEY, " +
                         "first_name VARCHAR(30) NOT NULL, " +
                         "last_name VARCHAR(30), " +
                         "email VARCHAR(50) NOT NULL, " +
                         "phoneno BIGINT NOT NULL UNIQUE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS rentalinfo (" +
                         "customer_id INT, " +
                         "driver_id INT, " +
                         "car_type VARCHAR(10), " +
                         "rental_start DATE NOT NULL, " +
                         "rental_end DATE NOT NULL, " +
                         "total_amount INT, " +
                         "PRIMARY KEY (customer_id, driver_id, rental_start))");
            
            // --- 2. INSERT TEST DATA ---
            // Note: car_no uses Integers (123) now, not Strings
            stmt.execute("INSERT INTO drivers (driver_id, first_name, last_name, phone_no, car_type, car_model, car_no, working_in_company) " +
                         "VALUES (1, 'John', 'Doe', 9876543210, 'SUV', 'Toyota Highlander', 123, TRUE)");
            
            stmt.execute("INSERT INTO drivers (driver_id, first_name, last_name, phone_no, car_type, car_model, car_no, working_in_company) " +
                         "VALUES (2, 'Alice', 'Smith', 1234567890, 'Sedan', 'Honda Accord', 124, TRUE)");
            
            stmt.execute("INSERT INTO drivers (driver_id, first_name, last_name, phone_no, car_type, car_model, car_no, working_in_company) " +
                         "VALUES (3, 'Bob', 'Johnson', 1122334455, 'Truck', 'Ford F-150', 125, FALSE)");
            
            stmt.execute("INSERT INTO drivers (driver_id, first_name, last_name, phone_no, car_type, car_model, car_no, working_in_company) " +
                         "VALUES (4, 'Charlie', 'Williams', 5566778899, 'SUV', 'Toyota Highlander', 126, TRUE)");

            // Note: carinfo uses 'id'
            stmt.execute("INSERT INTO carinfo (id, type, seater, description, rent_per_day) " +
                         "VALUES (1, 'SUV', 5, 'Comfortable family car', 100)");
            stmt.execute("INSERT INTO carinfo (id, type, seater, description, rent_per_day) " +
                         "VALUES (2, 'Sedan', 4, 'Compact car', 75)");
            stmt.execute("INSERT INTO carinfo (id, type, seater, description, rent_per_day) " +
                         "VALUES (3, 'Luxury', 4, 'Premium features', 250)");
            stmt.execute("INSERT INTO carinfo (id, type, seater, description, rent_per_day) " +
                         "VALUES (4, 'Van', 12, 'Spacious van', 150)");
        }

        driverService = new DriverService(dbManager);
        userService = new UserService(dbManager);
        customerService = new CustomerService(dbManager);
        carService = new CarService(dbManager);
        rentalService = new RentalService(dbManager);
    }

    @AfterEach
    void tearDown() throws SQLException {
        dbManager.executeUpdate("DROP ALL OBJECTS");
        dbManager.close();
    }

    // --- TEST 1: SQL Injection Protection ---
    @Test
    void testAddDriver_KillsSqlMutation() throws SQLException {
        // FIXED: Changed "TN-01" to "999" because car_no is now INT
        assertDoesNotThrow(() -> driverService.addDriver("Integration", "Tester", 99999L, "SUV", "X1", "999"));
    }

    // --- TEST 2: Transaction Commit ---
    @Test
    void testCommit_KillsVoidCallMutation() throws SQLException {
        dbManager.setAutoCommit(false);
        // FIXED: Changed "KA-55" to "888" because car_no is now INT
        driverService.addDriver("Transaction", "Check", 88888L, "Sedan", "City", "888");
        dbManager.commit(); 
        ResultSet rs = driverService.getDriverByPhone(88888L);
        assertTrue(rs.next(), "Mutant Killed: Data not found, commit() likely removed.");
    }

    // --- TEST 3: Return Values ---
    @Test
    void testAuth_KillsReturnValueMutation() throws SQLException {
        dbManager.executeUpdate("INSERT INTO users VALUES (1, 'testUser', 'testPass', 'admin')");
        String role = userService.authenticate("testUser", "testPass");
        assertNotNull(role, "Mutant Killed: Return value was null."); 
        assertEquals("admin", role, "Mutant Killed: Return value did not match DB data.");
    }

    // --- TEST 4: Math Logic (ID Generation) ---
    @Test
    void testIdGeneration_KillsMathMutant() throws SQLException {
        // FIXED: Changed "A1" to "111" (INT)
        driverService.addDriver("Math", "Test", 999L, "TypeA", "ModelA", "111");
        
        ResultSet rs = dbManager.executeQuery("SELECT driver_id FROM drivers WHERE phone_no = 999");
        assertTrue(rs.next());
        long newId = rs.getLong("driver_id");
        
        // We have 4 drivers (ID 1-4) in Setup. Next should be 5.
        // If MathMutator changes (count + 1) to (count - 1), result will be 3 (which already exists or is wrong).
        assertEquals(5, newId, "Next Driver ID should be 5.");
    }

    // --- TEST 5: Conditional Logic (If/Else) ---
    @Test
    void testSalesLogic_KillsConditionalMutants() throws SQLException {
        dbManager.executeUpdate("INSERT INTO rentalinfo VALUES (1, 1, 'SUV', '2023-05-01', '2023-05-05', 500)");
        dbManager.executeUpdate("INSERT INTO rentalinfo VALUES (1, 1, 'SUV', '2023-06-01', '2023-06-05', 1000)");

        long monthlySales = rentalService.getTotalSales("monthly", "2023", "06");
        assertEquals(1000, monthlySales, "Should only sum June.");

        long yearlySales = rentalService.getTotalSales("yearly", "2023", "");
        assertEquals(1500, yearlySales, "Should sum everything.");
    }

    // --- TEST 6: Date Boundaries ---
    @Test
    void testAvailability_KillsBoundaryMutants() throws SQLException {
        // Driver 1 has rental Jan 10-20
        dbManager.executeUpdate("INSERT INTO rentalinfo VALUES (1, 1, 'SUV', '2023-01-10', '2023-01-20', 500)");

        // Case A: Trip OVERLAPS -> Driver 1 HIDDEN
        ResultSet rsInside = driverService.getAvailableDrivers("2023-01-15", "2023-01-18");
        while(rsInside.next()) {
            if(rsInside.getInt("driver_id") == 1) {
                fail("Driver 1 should NOT be available. Conditional Logic broken.");
            }
        }

        // Case B: Trip AFTER -> Driver 1 VISIBLE
        ResultSet rsAfter = driverService.getAvailableDrivers("2023-01-21", "2023-01-25");
        boolean found = false;
        while(rsAfter.next()) {
            if(rsAfter.getInt("driver_id") == 1) found = true;
        }
        assertTrue(found, "Driver 1 SHOULD be available.");
    }

    // --- TEST 7: Full Workflow ---
    @Test
    void testFullBookingWorkflow() throws SQLException {
        customerService.addCustomer("Customer", "One", "cust@test.com", 1112223334L);

        long customerId = 0;
        ResultSet rsCust = customerService.getCustomerByPhone(1112223334L);
        if (rsCust.next()) customerId = rsCust.getLong("customer_id");
        
        long driverId = 1; 

        rentalService.bookCar(customerId, (int)driverId, "SUV", "2023-10-01", "2023-10-05", 2500);
        
        ResultSet rsRental = rentalService.getRentalDetails(customerId, "2023-10-01");
        assertTrue(rsRental.next(), "Booking should exist");
    }
}