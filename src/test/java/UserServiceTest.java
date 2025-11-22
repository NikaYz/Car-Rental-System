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

public class UserServiceTest {

    @Mock
    private DatabaseManager mockDbManager;
    
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(mockDbManager);
    }
    
    // --- Test 1: authenticate (Covers the successful path) ---
    @Test
    void authenticate_shouldReturnCorrectRole() throws SQLException {
        // ARRANGE: Mock the ResultSet to return an 'admin' role
        ResultSet mockRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(anyString())).thenReturn(mockRs);
        
        // Simulate result set containing the role
        when(mockRs.next()).thenReturn(true).thenReturn(false); 
        when(mockRs.getString("ro")).thenReturn("admin"); 

        // ACT
        String role = userService.authenticate("superuser", "securepwd");

        // ASSERT
        assertEquals("admin", role);
        
        // Verify that the correct query was executed
        verify(mockDbManager).executeQuery(
            argThat(query -> query.contains("SELECT role as ro FROM users") 
                     && query.contains("username = 'superuser'")
                     && query.contains("password = 'securepwd'"))
        );
        verify(mockRs).close();
    }
    
    // --- Test 2: addEmployee ---
    @Test
    void addEmployee_shouldCallExecuteUpdateWithInsertSql() throws SQLException {
        // ACT
        userService.addEmployee(5, "newuser", "pass123", "sub");

        // ASSERT: Verify the INSERT query was executed exactly once
        verify(mockDbManager, times(1)).executeUpdate(
            argThat(query -> query.contains("INSERT INTO users values (5") 
                     && query.contains("'newuser'")
                     && query.contains("'sub'")
        ));
    }
    
    // --- Test 3: deleteEmployee ---
    @Test
    void deleteEmployee_shouldCallExecuteUpdateWithDeleteSql() throws SQLException {
        // ACT
        userService.deleteEmployee("olduser", "oldpass");

        // ASSERT: Verify the DELETE query was executed exactly once
        verify(mockDbManager, times(1)).executeUpdate(
            argThat(query -> query.contains("DELETE from users where") 
                     && query.contains("username = 'olduser'")
                     && query.contains("password = 'oldpass'")
        ));
    }
    
    // --- Test 4: getNextUserId ---
    @Test
    void getNextUserId_shouldReturnCountPlusOne() throws SQLException {
        // ARRANGE: Mock the ResultSet to return a count of 5
        ResultSet mockCountRs = mock(ResultSet.class);
        when(mockDbManager.executeQuery(contains("COUNT(*) AS record_count FROM users"))).thenReturn(mockCountRs);
        
        // Simulate a count of 5
        when(mockCountRs.next()).thenReturn(true).thenReturn(false); 
        when(mockCountRs.getInt("record_count")).thenReturn(5); 

        // ACT
        int nextId = userService.getNextUserId();

        // ASSERT
        assertEquals(6, nextId);
        verify(mockCountRs).close();
    }
}