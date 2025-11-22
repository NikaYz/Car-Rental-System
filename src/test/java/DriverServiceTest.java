import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DriverServiceTest {

    @Mock
    private DatabaseManager mockDbManager;
    
    private DriverService driverService;
    
    // Define the common count query string (from DriverService, it uses lowercase 'count')
    private static final String COUNT_QUERY = "SELECT count(*) AS record_count FROM drivers;";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        driverService = new DriverService(mockDbManager);
    }
    
    // --- Test 1: addDriver (FIXED MOCK) ---
    @Test
    void addDriver_shouldCallExecuteUpdateWithCorrectSql() throws SQLException {
        // ARRANGE: Mock the internal call to getNextDriverId() using the EXACT query string.
        ResultSet mockNextIdRs = mock(ResultSet.class);
        
        // When the exact count query is executed, return the mock ResultSet
        when(mockDbManager.executeQuery(COUNT_QUERY)).thenReturn(mockNextIdRs);
        
        // Simulate initial count of 0, so the new ID will be 1
        when(mockNextIdRs.next()).thenReturn(true).thenReturn(false); 
        when(mockNextIdRs.getLong("record_count")).thenReturn(0L); 

        // ACT
        driverService.addDriver("John", "Doe", 9876543210L, "Sedan", "Honda City", "DL1C1234");

        // ASSERT: Verify the INSERT query was executed
        verify(mockDbManager, times(1)).executeUpdate(
            argThat(query -> query.contains("INSERT INTO drivers values (1") 
                     && query.contains("'John'")
                     && query.contains("1);"))
        );
        verify(mockNextIdRs).close();
    }
    
    // --- Test 2: updateDriver ---
    @Test
    void updateDriver_shouldCallExecuteUpdateWithCorrectUpdateSql() throws SQLException {
        driverService.updateDriver(201L, "Jane", "Smith", 9998887770L, "SUV", "Hyundai Creta", "HR2D5678");
        
        verify(mockDbManager, times(1)).executeUpdate(
            argThat(query -> query.contains("UPDATE drivers SET phone_no = 9998887770") 
                     && query.contains("car_type = 'SUV'")
                     && query.contains("WHERE driver_id = 201"))
        );
    }
    
    // --- Test 3: deactivateDriver ---
    @Test
    void deactivateDriver_shouldSetWorkingInCompanyToFalse() throws SQLException {
        driverService.deactivateDriver(205L);
        
        verify(mockDbManager, times(1)).executeUpdate(
            argThat(query -> query.contains("UPDATE drivers SET working_in_company = false WHERE driver_id = 205;"))
        );
    }
    
    // --- Test 4: getAvailableDrivers ---
    @Test
    void getAvailableDrivers_shouldExecuteComplexQueryWithDates() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs);
        driverService.getAvailableDrivers("2025-01-01", "2025-01-05");

        verify(mockDbManager).executeQuery(
            argThat(query -> query.contains("d.working_in_company = TRUE")
                     && query.contains("r.rental_end >= '2025-01-01'"))
        );
    }

    // --- Test 5: getDriverCount (FIXED MOCK) ---
    @Test
    void getDriverCount_shouldReturnCorrectCount() throws SQLException {
        // ARRANGE: Mock the ResultSet to return a specific count using the EXACT query string.
        ResultSet mockCountRs = mock(ResultSet.class);
        
        // When the exact count query is executed, return the mock ResultSet
        when(mockDbManager.executeQuery(COUNT_QUERY)).thenReturn(mockCountRs);
        
        // Simulate a count of 12
        when(mockCountRs.next()).thenReturn(true).thenReturn(false); 
        when(mockCountRs.getInt("record_count")).thenReturn(12); 

        // ACT
        int result = driverService.getDriverCount();

        // ASSERT
        assertEquals(12, result);
        verify(mockCountRs).close();
    }
    @Test
    void getDriverByPhone_shouldCallExecuteQueryAndReturnResultSet() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs);
        
        // ACT & ASSERT the return value
        ResultSet result = driverService.getDriverByPhone(9876543210L);
        assertNotNull(result, "ResultSet should not be null.");
        verify(mockDbManager).executeQuery(argThat(query -> query.contains("where 9876543210 = drivers.phone_no")));
    }

    @Test
    void getAllDrivers_shouldCallExecuteQueryAndReturnResultSet() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs);
        
        // ACT & ASSERT the return value
        ResultSet result = driverService.getAllDrivers();
        assertNotNull(result, "ResultSet should not be null.");
        verify(mockDbManager).executeQuery("SELECT * from drivers;");
    }
   
}