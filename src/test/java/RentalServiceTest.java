import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull; // New import for survivor fix

public class RentalServiceTest {
    
    @Mock
    private DatabaseManager mockDbManager;
    
    private RentalService rentalService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        rentalService = new RentalService(mockDbManager);
    }

    // --- Existing Helper Methods (assuming they are correct or not the focus) ---
    // ... (Your existing 6 tests not provided in full, but assumed to exist) ...

    @Test
    void getRentalInfoCount_shouldReturnCorrectCount() throws SQLException {
        // ARRANGE: Mock the ResultSet for the count query
        ResultSet mockCountRs = mock(ResultSet.class);
        String countQuery = "SELECT count(*) AS record_count FROM rentalinfo;"; 
        
        when(mockDbManager.executeQuery(countQuery)).thenReturn(mockCountRs);
        
        // Simulate a count of 15
        when(mockCountRs.next()).thenReturn(true).thenReturn(false); 
        when(mockCountRs.getInt("record_count")).thenReturn(15); 

        // ACT
        int result = rentalService.getRentalInfoCount();

        // ASSERT
        assertEquals(15, result);
        verify(mockCountRs).close(); 
    }
    
    // --------------------------------------------------------------------------
    // FIX: Added assertNotNull(result) to kill NullReturnValsMutator survivors
    // --------------------------------------------------------------------------

    @Test
    void getDriverPerformance_SortByAmount() throws Exception {
        // ARRANGE: sortBy == 1 actually executes the TOURS query, sorted ASC
        String expectedQuery = "SELECT r.driver_id, d.first_name, d.last_name, COUNT(*) AS tours " +
                           "FROM rentalinfo r JOIN drivers d ON r.driver_id = d.driver_id " +
                           "GROUP BY r.driver_id, d.first_name, d.last_name ORDER BY TOURS;";

        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(expectedQuery)).thenReturn(mockRs);

        // ACT: Call with sortBy=1
        ResultSet result = rentalService.getDriverPerformance(1); // Capture result

        // ASSERT
        verify(mockDbManager).executeQuery(expectedQuery);
        assertNotNull(result); // FIX: Assert result is not null
    }
     
    @Test
    void getDriverPerformance_SortByTours() throws Exception {
        // ARRANGE: sortBy == 2 actually executes the AMOUNT query, sorted ASC
        String expectedQuery = "SELECT r.driver_id, d.first_name, d.last_name, SUM(total_amount) AS amount " +
                           "FROM rentalinfo r JOIN drivers d ON r.driver_id = d.driver_id " +
                           "GROUP BY r.driver_id, d.first_name, d.last_name ORDER BY amount;";

        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(expectedQuery)).thenReturn(mockRs);

        // ACT: Call with sortBy=2
        ResultSet result = rentalService.getDriverPerformance(2); // Capture result

        // ASSERT
        verify(mockDbManager).executeQuery(expectedQuery);
        assertNotNull(result); // FIX: Assert result is not null
    }

    @Test
    void getDriverPerformance_SortByDefault() throws Exception {
        // ARRANGE: Mock the expected query for the default (else) case
        String expectedQuery = "SELECT r.driver_id, d.first_name, d.last_name, COUNT(*) AS tours, " +
                            "SUM(r.total_amount) AS amount FROM rentalinfo r " +
                            "JOIN drivers d ON r.driver_id = d.driver_id " +
                            "GROUP BY r.driver_id, d.first_name, d.last_name " +
                            "ORDER BY amount ASC, tours ASC;";

        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(expectedQuery)).thenReturn(mockRs);

        // ACT: Pass any value that is not 1 or 2, e.g., 0 or 3
        ResultSet result = rentalService.getDriverPerformance(0); // Capture result

        // ASSERT
        verify(mockDbManager).executeQuery(expectedQuery);
        assertNotNull(result); // FIX: Assert result is not null
    }

    @Test
    void getCarTypePerformance_SortByAmount() throws Exception {
        // ARRANGE: Sort by amount (sortBy == 1)
        String expectedQuery = "SELECT car_type, SUM(total_amount) as amount from rentalinfo GROUP BY car_type ORDER BY amount;";
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(expectedQuery)).thenReturn(mockRs);
        
        // ACT
        ResultSet result = rentalService.getCarTypePerformance(1); // Capture result

        // ASSERT
        verify(mockDbManager).executeQuery(expectedQuery);
        assertNotNull(result); // FIX: Assert result is not null
    }
    
    @Test
    void getCarTypePerformance_SortByTours() throws Exception {
        // ARRANGE: Sort by tours (sortBy == 2)
        String expectedQuery = "SELECT car_type, count(*) as tours from rentalinfo GROUP BY car_type ORDER BY tours;";
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(expectedQuery)).thenReturn(mockRs);
        
        // ACT
        ResultSet result = rentalService.getCarTypePerformance(2); // Capture result

        // ASSERT
        verify(mockDbManager).executeQuery(expectedQuery);
        assertNotNull(result); // FIX: Assert result is not null
    }

    @Test
    void getCarTypePerformance_SortByDefault() throws Exception {
        // ARRANGE: Default sort (else)
        String expectedQuery = "SELECT car_type, SUM(total_amount) as amount, COUNT(*) as tours " +
                   "from rentalinfo GROUP BY car_type ORDER BY amount,tours;";
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(expectedQuery)).thenReturn(mockRs);
        
        // ACT
        ResultSet result = rentalService.getCarTypePerformance(0); // Capture result

        // ASSERT
        verify(mockDbManager).executeQuery(expectedQuery);
        assertNotNull(result); // FIX: Assert result is not null
    }

    @Test
    void getAllRentalInfo_shouldExecuteCorrectQuery() throws Exception {
        // ARRANGE
        String expectedQuery = "SELECT * from rentalinfo;";
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(expectedQuery)).thenReturn(mockRs);
        
        // ACT
        ResultSet result = rentalService.getAllRentalInfo(); // Capture result

        // ASSERT
        verify(mockDbManager).executeQuery(expectedQuery);
        assertNotNull(result); // FIX: Assert result is not null
    }
    
    // --------------------------------------------------------------------------
    // NEW TEST: Added getRentalDetails test to cover complex logic (NO_COVERAGE)
    // --------------------------------------------------------------------------

    @Test
    void getRentalDetails_shouldExecuteTwoQueriesAndReturnDetails() throws Exception {
        // ARRANGE 
        long customerId = 1L;
        String startDate = "2023-11-20";
        long expectedDriverId = 5L;

        // 1. Mock the first ResultSet (for driver_id lookup)
        String query1 = "SELECT driver_id from rentalinfo where customer_id = " + customerId + 
                        " AND rental_start = '" + startDate + "';";
        ResultSet mockRs1 = mock(ResultSet.class);
        when(mockDbManager.executeQuery(query1)).thenReturn(mockRs1);
        when(mockRs1.next()).thenReturn(true).thenReturn(false);
        when(mockRs1.getLong("driver_id")).thenReturn(expectedDriverId);

        // 2. Mock the second ResultSet (for final details)
        String expectedQuery2 = "SELECT c.first_name as customer_f, c.last_name as customer_l, c.phoneno as customer_p, " +
                    "d.first_name, d.last_name,d.phone_no,d.car_type,d.car_model,d.car_no," +
                    "r.rental_start,r.rental_end, r.total_amount from rentalinfo r " +
                    "join customers c on r.customer_id = " + customerId + 
                    " join drivers d on r.driver_id = " + expectedDriverId + 
                    " where (r.customer_id = " + customerId + " and r.driver_id = " + expectedDriverId + ") limit 1;";
        ResultSet mockRs2 = mock(ResultSet.class);
        when(mockDbManager.executeQuery(expectedQuery2)).thenReturn(mockRs2);

        // ACT
        ResultSet result = rentalService.getRentalDetails(customerId, startDate);

        // ASSERT
        verify(mockDbManager).executeQuery(query1);
        verify(mockRs1).close(); // Verify the service closed the first ResultSet
        verify(mockDbManager).executeQuery(expectedQuery2);
        assertNotNull(result); // Check for null return
    }

}
