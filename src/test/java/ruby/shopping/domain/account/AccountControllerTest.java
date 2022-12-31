package ruby.shopping.domain.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ruby.shopping.common.ExceptionController;
import ruby.shopping.common.valid.EmailPattern;
import ruby.shopping.common.valid.PasswordPattern;
import ruby.shopping.domain.account.dtos.AccountLoginRequest;
import ruby.shopping.domain.account.dtos.AccountSignUpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    String email = "test@gmail.com";
    String password = "1234qwer!@";
    Account existsAccount;

    @BeforeEach
    void before() {
        existsAccount = Account.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        accountRepository.save(existsAccount);
    }

    @AfterEach
    void after() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입시 요청 값이 잘못된 경우 400 응답")
    void signUp_badRequest() throws Exception {
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest();
        accountSignUpRequest.setEmail("rubygmail.com");
        accountSignUpRequest.setPassword("qwer1234");

        mockMvc.perform(post("/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountSignUpRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionController.BIND_EXCEPTION_MESSAGE))
                .andExpect(jsonPath("$.validations.email").value(EmailPattern.MESSAGE))
                .andExpect(jsonPath("$.validations.password").value(PasswordPattern.MESSAGE));
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 요청시 409 응답")
    void signUp_duplicate() throws Exception {
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest();
        accountSignUpRequest.setEmail(email);
        accountSignUpRequest.setPassword("qwer1234!@");

        mockMvc.perform(post("/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountSignUpRequest))
                )
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() throws Exception {
        AccountSignUpRequest accountSignUpRequest = new AccountSignUpRequest();
        accountSignUpRequest.setEmail("ruby@gmail.com");
        accountSignUpRequest.setPassword("qwer1234!@");

        mockMvc.perform(post("/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountSignUpRequest))
                )
                .andExpect(status().isCreated());

        Account savedAccount = accountRepository.findByEmail(accountSignUpRequest.getEmail()).orElseThrow();
        assertThat(savedAccount.getEmail()).isEqualTo(accountSignUpRequest.getEmail());
        assertThat(passwordEncoder.matches(accountSignUpRequest.getPassword(), savedAccount.getPassword())).isTrue();
    }

    @Test
    @DisplayName("이메일과 비밀번호가 일치하는 계정을 찾을 수 없을 경우 404 응답")
    void login_notFound() throws Exception {
        AccountLoginRequest accountLoginRequest = new AccountLoginRequest();
        accountLoginRequest.setEmail(email);
        accountLoginRequest.setPassword("qwer1234!@213");

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountLoginRequest))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("이메일과 비밀번호가 일치하는 계정이 있을 경우 로그인 성공")
    void login_success() throws Exception {
        AccountLoginRequest accountLoginRequest = new AccountLoginRequest();
        accountLoginRequest.setEmail(email);
        accountLoginRequest.setPassword(password);

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountLoginRequest))
                )
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }
}