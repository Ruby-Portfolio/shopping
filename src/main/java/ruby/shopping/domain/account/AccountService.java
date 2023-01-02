package ruby.shopping.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.shopping.domain.account.dtos.AccountLoginRequest;
import ruby.shopping.domain.account.dtos.AccountSignUpRequest;
import ruby.shopping.domain.account.enums.Authority;
import ruby.shopping.domain.account.exception.AccountExistsEmailException;
import ruby.shopping.domain.account.exception.AccountNotFoundException;
import ruby.shopping.security.AccountDetails;
import ruby.shopping.security.jwt.JwtTokenProvider;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signUp(AccountSignUpRequest accountSignUpRequest) {
        accountRepository.findByEmail(accountSignUpRequest.getEmail())
                .ifPresent(account -> {
                    throw new AccountExistsEmailException();
                });

        Account newAccount = Account.builder()
                .email(accountSignUpRequest.getEmail())
                .password(passwordEncoder.encode(accountSignUpRequest.getPassword()))
                .build();
        newAccount.addAuthority(Authority.USER);

        accountRepository.save(newAccount);
    }

    @Transactional(readOnly = true)
    public String login(AccountLoginRequest accountLoginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(accountLoginRequest.getEmail(), accountLoginRequest.getPassword());

        try {
            // authenticationToken 과 loadUserByUsername 에서 반환하는 User 객체와 비교
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenProvider.createToken(accountLoginRequest.getEmail());
        } catch (AuthenticationException e) {
            throw new AccountNotFoundException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Account account =  accountRepository.findByEmail(email)
                .orElseThrow(AccountNotFoundException::new);

        List<GrantedAuthority> authorities = account.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.name()))
                .collect(Collectors.toList());

        return new AccountDetails(account, authorities);
    }
}
