import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CarServiceTest {

    @Mock
    private DatabaseManager mockDbManager;
    
    private CarService carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        carService = new CarService(mockDbManager);
    }
    
    // --- Test 1: addCarModel (FIXED MOCK) ---
    @Test
    void addCarModel_shouldCallExecuteUpdateWithCorrectSql() throws SQLException {
        // ARRANGE: Mock the internal call to getNextCarId() using the EXACT query string.
        ResultSet mockCountRs = mock(ResultSet.class);
        String countQuery = "SELECT COUNT(*) AS record_count FROM carinfo;"; // The exact query string from CarService
        
        // When the exact count query is executed, return the mock ResultSet
        when(mockDbManager.executeQuery(countQuery)).thenReturn(mockCountRs);
        
        // Simulate initial count of 0, so the new ID will be 1
        when(mockCountRs.next()).thenReturn(true).thenReturn(false); 
        when(mockCountRs.getInt("record_count")).thenReturn(0); 

        // ACT
        carService.addCarModel("SUV", 7, "Spacious and reliable", 5000);

        // ASSERT: Verify the INSERT query was executed
        verify(mockDbManager, times(1)).executeUpdate(
            argThat(query -> query.contains("INSERT INTO carinfo values (1") 
                     && query.contains("'SUV'")
                     && query.contains("5000"))
        );
        verify(mockCountRs).close();
    }
    
    // --- Test 2: getRentPerDay ---
    @Test
    void getRentPerDay_shouldReturnCorrectPrice() throws SQLException {
        // ARRANGE: Mock the ResultSet to return a specific rent price
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs);
        
        // Simulate result set containing the rent
        when(mockRs.next()).thenReturn(true).thenReturn(false); 
        when(mockRs.getInt("rent_per_day")).thenReturn(3500); 

        // ACT
        int result = carService.getRentPerDay("Sedan");

        // ASSERT
        assertEquals(3500, result);
        verify(mockRs).close();
    }
    
    // --- Test 3: getAvailableCarTypes ---
    @Test
    void getAvailableCarTypes_shouldCallExecuteQueryAndReturnResultSet() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs);
        
        // ACT & ASSERT the return value
        ResultSet result = carService.getAvailableCarTypes();
        assertNotNull(result, "ResultSet should not be null to kill NullReturnValsMutator mutant.");
        verify(mockDbManager).executeQuery("SELECT type FROM carinfo;");
    }
    @Test
    void getAllCarInfo_shouldCallExecuteQueryWithCorrectSql() throws SQLException {
        // ARRANGE
        ResultSet mockRs = mock(ResultSet.class);
        // Ensure executeQuery returns a mock ResultSet for any SQL query
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs); 

        // ACT
        carService.getAllCarInfo();

        // ASSERT
        // Verify that the exact SELECT query for all car info was executed
        verify(mockDbManager).executeQuery("SELECT type, seater, description, rent_per_day from carInfo");
    }

    @Test
    void addCarModel_shouldUseCorrectNextIdWhenCarsExist() throws SQLException {
        // ARRANGE: Mock the internal call to getNextCarId()
        ResultSet mockCountRs = mock(ResultSet.class);
        String countQuery = "SELECT COUNT(*) AS record_count FROM carinfo;";
        
        when(mockDbManager.executeQuery(countQuery)).thenReturn(mockCountRs);
        
        // Simulate initial count of 5, so the new ID will be 6
        when(mockCountRs.next()).thenReturn(true).thenReturn(false); 
        when(mockCountRs.getInt("record_count")).thenReturn(5); // Existing records

        // ACT
        carService.addCarModel("Truck", 2, "Heavy duty", 7500);

        // ASSERT: Verify the INSERT query was executed with ID 6
        verify(mockDbManager, times(1)).executeUpdate(
            argThat(query -> query.contains("INSERT INTO carinfo values (6") 
                     && query.contains("'Truck'")
                     && query.contains("7500"))
        );
        verify(mockCountRs).close();
    }

     // Covers the `while(rs.next())` loop exit condition immediately, ensuring the `price = 0` return is covered.
    @Test
    void getRentPerDay_shouldReturnZeroIfCarNotFound() throws SQLException {
        // ARRANGE: Mock the ResultSet to return NO data
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs);
        
        // Simulate an empty result set (rs.next() immediately returns false)
        when(mockRs.next()).thenReturn(false); 

        // ACT
        int result = carService.getRentPerDay("Van (Nonexistent)");

        // ASSERT: Should return 0, as initialized
        assertEquals(0, result);
        verify(mockRs).close();
    }
}