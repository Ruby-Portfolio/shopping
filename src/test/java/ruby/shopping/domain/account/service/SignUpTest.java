package ruby.shopping.domain.account.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.account.AccountService;
import ruby.shopping.domain.account.dtos.AccountSignUpRequest;
import ruby.shopping.domain.account.exception.AccountExistsEmailException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class SignUpTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AccountService accountService;

    @Test
    @DisplayName("이메일에 해당하는 계정이 이미 존재하는 경우 이메일 중복 예외처리")
    void signUp_existsEmail() {
        String email = "ruby@gmail.com";
        String password = "qwer1234!@";
        Account account = Account.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .build();
        Mockito.when(accountRepository.findByEmail(email))
                .thenReturn(Optional.of(account));

        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest();
        accountSignUpRequest.setEmail(email);
        accountSignUpRequest.setPassword(password);

        assertThatThrownBy(() -> accountService.signUp(accountSignUpRequest))
                .isInstanceOf(AccountExistsEmailException.class);
    }

    @Test
    @DisplayName("이메일에 해당하는 계정이 없을 경우 회원가입 성공")
    void signUp_success() {
        String email = "ruby@gmail.com";
        String password = "qwer1234!@";
        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest();
        accountSignUpRequest.setEmail(email);
        accountSignUpRequest.setPassword(password);

        assertThatCode(() -> accountService.signUp(accountSignUpRequest))
                .doesNotThrowAnyException();
    }
}