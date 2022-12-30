package ruby.shopping.domain.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.shopping.domain.account.dtos.AccountSignUpRequest;
import ruby.shopping.domain.account.enums.Authority;
import ruby.shopping.domain.account.exception.AccountExistsEmailException;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

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
}
