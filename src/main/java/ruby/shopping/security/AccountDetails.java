package ruby.shopping.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ruby.shopping.domain.account.Account;

import java.util.List;

@Getter
public class AccountDetails extends User {
    private final Account account;

    public AccountDetails(Account account, List<GrantedAuthority> authorities) {
        super(account.getEmail(), account.getPassword(), authorities);
        this.account = account;
    }
}
