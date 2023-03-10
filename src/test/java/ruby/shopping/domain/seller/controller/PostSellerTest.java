package ruby.shopping.domain.seller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ruby.shopping.common.ExceptionController;
import ruby.shopping.common.valid.AccountNumberPattern;
import ruby.shopping.common.valid.UsernamePattern;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.seller.SellerRepository;
import ruby.shopping.domain.seller.dtos.SellerCreateRequest;
import ruby.shopping.security.jwt.JwtFilter;
import ruby.shopping.security.jwt.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostSellerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    Account account;
    String token;

    @BeforeEach
    void before() {
        String email = "test@gmail.com";
        String password = "1234qwer!@";
        account = Account.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        accountRepository.save(account);

        token = jwtTokenProvider.createToken(email);
    }

    @AfterEach
    void after() {
        sellerRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("?????? ????????? ????????? ?????? ?????? ????????? ?????? 401 ??????")
    void postSeller_unauthorized() throws Exception {
        SellerCreateRequest sellerCreateRequest = new SellerCreateRequest();
        sellerCreateRequest.setSellerName("?????????");
        sellerCreateRequest.setAccountNumber("123034112354");
        sellerCreateRequest.setAccountHolder("?????????");

        mockMvc.perform(post("/api/sellers")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sellerCreateRequest))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ????????? ?????? 400 ??????")
    void postSeller_badRequest() throws Exception {
        SellerCreateRequest sellerCreateRequest = new SellerCreateRequest();
        sellerCreateRequest.setSellerName("?????????!@#");
        sellerCreateRequest.setAccountNumber("1230341");
        sellerCreateRequest.setAccountHolder("?????????!@#");

        mockMvc.perform(post("/api/sellers")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sellerCreateRequest))
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionController.BIND_EXCEPTION_MESSAGE))
                .andExpect(jsonPath("$.validations.sellerName").value(UsernamePattern.MESSAGE))
                .andExpect(jsonPath("$.validations.accountNumber").value(AccountNumberPattern.MESSAGE))
                .andExpect(jsonPath("$.validations.accountHolder").value(UsernamePattern.MESSAGE));
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void postSeller_success() throws Exception {
        SellerCreateRequest sellerCreateRequest = new SellerCreateRequest();
        sellerCreateRequest.setSellerName("?????????");
        sellerCreateRequest.setAccountNumber("123034112354");
        sellerCreateRequest.setAccountHolder("?????????");

        mockMvc.perform(post("/api/sellers")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sellerCreateRequest))
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isCreated());

        assertThat(sellerRepository.count()).isEqualTo(1);
    }
}