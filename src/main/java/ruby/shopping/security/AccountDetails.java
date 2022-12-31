package ruby.shopping.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class AccountDetails extends User {
    private final String email;

    public AccountDetails(String email, String password, List<GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.email = email;
    }
}
