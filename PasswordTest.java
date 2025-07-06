import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Test admin123 password
        String password = "admin123";
        String hash = encoder.encode(password);
        // System.out.println("admin123 BCrypt hash: " + hash);
        
        // Verify database hash
        String dbHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa";
        boolean matches = encoder.matches(password, dbHash);
        // System.out.println("Password verification result: " + matches);
        
        // Generate new hash for database update
        String newHash = encoder.encode(password);
        // System.out.println("New hash value: " + newHash);
    }
} 