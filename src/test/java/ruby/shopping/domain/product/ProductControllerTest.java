package ruby.shopping.domain.product;

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
import ruby.shopping.common.valid.*;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.product.dtos.ProductCreateRequest;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.seller.Seller;
import ruby.shopping.domain.seller.SellerRepository;
import ruby.shopping.domain.seller.exception.SellerNotFoundException;
import ruby.shopping.security.jwt.JwtFilter;
import ruby.shopping.security.jwt.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    ProductRepository productRepository;
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
    Seller seller;
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

        seller = Seller.builder()
                .sellerName("김루비")
                .accountNumber("013124512311")
                .accountHolder("김예금주")
                .account(account)
                .build();
        sellerRepository.save(seller);

        token = jwtTokenProvider.createToken(email);
    }

    @AfterEach
    void after() {
        productRepository.deleteAll();
        sellerRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 등록시 로그인 하지 않은 상태일 경우 401 응답")
    void postProduct_unauthorized() throws Exception {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setName("상품명");
        productCreateRequest.setDescription("상품 설명");
        productCreateRequest.setPrice(10000);
        productCreateRequest.setSellerId(seller.getId());
        productCreateRequest.setCategory(Category.CAMPING.name());

        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productCreateRequest))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("상품 등록시 요청 값이 잘못된 경우 400 응답")
    void postProduct_badRequest() throws Exception {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setName("");
        productCreateRequest.setDescription("");
        productCreateRequest.setPrice(0);
        productCreateRequest.setSellerId(-1L);
        productCreateRequest.setCategory("핸드폰");

        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productCreateRequest))
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionController.BIND_EXCEPTION_MESSAGE))
                .andExpect(jsonPath("$.validations.name").value(ProductNamePattern.MESSAGE))
                .andExpect(jsonPath("$.validations.description").value(ProductDescriptionPattern.MESSAGE))
                .andExpect(jsonPath("$.validations.price").value(PricePattern.MESSAGE))
                .andExpect(jsonPath("$.validations.category").value(CategoryPattern.MESSAGE))
                .andExpect(jsonPath("$.validations.sellerId").value(IdPattern.MESSAGE));
    }

    @Test
    @DisplayName("상품 등록시 셀러 정보를 찾을 수 없을 경우 404 응답")
    void postProduct_notFoundSeller() throws Exception {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setName("상품명");
        productCreateRequest.setDescription("상품 설명");
        productCreateRequest.setPrice(10000);
        productCreateRequest.setSellerId(seller.getId() + 99);
        productCreateRequest.setCategory(Category.CAMPING.name());

        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productCreateRequest))
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(SellerNotFoundException.MESSAGE));
    }

    @Test
    @DisplayName("상품 등록 성공")
    void postProduct_success() throws Exception {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setName("상품명");
        productCreateRequest.setDescription("상품 설명");
        productCreateRequest.setPrice(10000);
        productCreateRequest.setSellerId(seller.getId());
        productCreateRequest.setCategory(Category.CAMPING.name());

        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productCreateRequest))
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isCreated());

        assertThat(productRepository.count()).isEqualTo(1);
    }
}