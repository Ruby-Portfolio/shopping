package ruby.shopping.domain.account.controller;

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
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.account.dtos.AccountLoginRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginTest {

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
    @DisplayName("이메일과 비밀번호가 일치하는 계정을 찾을 수 없을 경우 404 응답")
    void login_notFound() throws Exception {
        AccountLoginRequest accountLoginRequest = new AccountLoginRequest();
        accountLoginRequest.setEmail(email);
        accountLoginRequest.setPassword("qwer1234!@213");

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountLoginRequest))
                )
                .andExpect(status().isNotFound())
                .andExpect(header().doesNotExist("Authorization"));
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