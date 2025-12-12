import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "a123"; // mot de passe en clair
        String hashedPassword = encoder.encode(rawPassword);
        System.out.println(hashedPassword);
    }
}
