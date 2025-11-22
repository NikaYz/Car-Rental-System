import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerServiceTest {
    
    @Mock
    private DatabaseManager mockDbManager;
    
    private CustomerService customerService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(mockDbManager);
    }
    
    // --- Test 1: addCustomer (FIXED MOCK) ---
    @Test
    void addCustomer_shouldCallExecuteUpdateWithCorrectInsertSql() throws SQLException {
        // ARRANGE: Mock getNextCustomerId() (which uses "SELECT COUNT(*)...") to return 1
        ResultSet mockNextIdRs = mock(ResultSet.class);
        String nextIdQuery = "SELECT COUNT(*) AS record_count FROM customers;"; 
        
        // When the exact count query is executed, return the mock ResultSet
        when(mockDbManager.executeQuery(nextIdQuery)).thenReturn(mockNextIdRs);
        
        when(mockNextIdRs.next()).thenReturn(true).thenReturn(false); 
        when(mockNextIdRs.getInt("record_count")).thenReturn(0); 

        // ACT
        customerService.addCustomer("Alice", "Smith", "alice@test.com", 5551234567L);

        // ASSERT 
        verify(mockDbManager, times(1)).executeUpdate(
            argThat(query -> query.contains("INSERT INTO customers values (1") 
                     && query.contains("'Alice'")
                     && query.contains("5551234567"))
        );
        verify(mockNextIdRs).close();
    }
    
    // --- Test 2: updateCustomer ---
    @Test
    void updateCustomer_shouldCallExecuteUpdateWithCorrectUpdateSql() throws SQLException {
        customerService.updateCustomer(101L, "Robert", "Jones", "robert.jones@new.com", 5559876543L);
        
        verify(mockDbManager, times(1)).executeUpdate(
            argThat(query -> query.contains("UPDATE customers SET phoneno = 5559876543") 
                     && query.contains("email = 'robert.jones@new.com'")
                     && query.contains("WHERE customer_id = 101"))
        );
    }
    
    // --- Test 3: getCustomerCount (FIXED MOCK) ---
    @Test
    void getCustomerCount_shouldReturnCorrectCount() throws SQLException {
        // ARRANGE: Mock the ResultSet to return a specific count using the EXACT query string.
        ResultSet mockCountRs = mock(ResultSet.class);
        String countQuery = "SELECT count(*) AS record_count FROM customers;"; 
        
        // When the exact count query is executed, return the mock ResultSet
        when(mockDbManager.executeQuery(countQuery)).thenReturn(mockCountRs);
        
        // Simulate a count of 5
        when(mockCountRs.next()).thenReturn(true).thenReturn(false); 
        when(mockCountRs.getInt("record_count")).thenReturn(5); 

        // ACT
        int result = customerService.getCustomerCount();

        // ASSERT
        assertEquals(5, result);
        verify(mockCountRs).close();
    }

    @Test
    void getCustomerByPhone_shouldCallExecuteQueryAndReturnResultSet() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs);
        
        // ACT & ASSERT the return value
        ResultSet result = customerService.getCustomerByPhone(9998887770L);
        assertNotNull(result, "ResultSet should not be null.");
        verify(mockDbManager).executeQuery(argThat(query -> query.contains("where 9998887770 = customers.phoneno")));
    }

    @Test
    void getAllCustomers_shouldCallExecuteQueryAndReturnResultSet() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs);
        
        // ACT & ASSERT the return value
        ResultSet result = customerService.getAllCustomers();
        assertNotNull(result, "ResultSet should not be null.");
        verify(mockDbManager).executeQuery("SELECT * from customers;");
    }
}