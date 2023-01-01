package ruby.shopping.domain.order.controller;

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
import ruby.shopping.common.valid.OrderProductPattern;
import ruby.shopping.common.valid.OrderStatePattern;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.order.OrderRepository;
import ruby.shopping.domain.order.dtos.OrderCreateRequest;
import ruby.shopping.domain.order.enums.OrderState;
import ruby.shopping.domain.orderProduct.OrderProductRepository;
import ruby.shopping.domain.product.Product;
import ruby.shopping.domain.product.ProductRepository;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.product.exception.ProductNotFoundException;
import ruby.shopping.domain.seller.Seller;
import ruby.shopping.domain.seller.SellerRepository;
import ruby.shopping.security.jwt.JwtFilter;
import ruby.shopping.security.jwt.JwtTokenProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostOrderTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    OrderRepository orderRepository;
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
    Product product;
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
        
        product = Product.builder()
                .name("플루트")
                .price(10000000)
                .description("관악기")
                .category(Category.HOBBY)
                .seller(seller)
                .build();
        productRepository.save(product);

        token = jwtTokenProvider.createToken(email);
    }

    @AfterEach
    void after() {
        orderProductRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        sellerRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 등록시 로그인 하지 않은 상태일 경우 401 응답")
    void postOrder_unauthorized() throws Exception {
        OrderCreateRequest.OrderProductDto orderProductDto = new OrderCreateRequest.OrderProductDto();
        orderProductDto.setProductId(product.getId());
        orderProductDto.setCount(1);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setOrderProducts(List.of(orderProductDto));

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderCreateRequest))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("주문 등록시 요청 값이 잘못된 경우 400 응답")
    void postOrder_badRequest() throws Exception {
        OrderCreateRequest.OrderProductDto orderProductDto = new OrderCreateRequest.OrderProductDto();
        orderProductDto.setProductId(-1L);
        orderProductDto.setCount(0);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setOrderState("결제 불분명");
        orderCreateRequest.setOrderProducts(List.of(orderProductDto));

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderCreateRequest))
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionController.BIND_EXCEPTION_MESSAGE))
                .andExpect(jsonPath("$.validations.orderState").value(OrderStatePattern.MESSAGE))
                .andExpect(jsonPath("$.validations.orderProducts").value(OrderProductPattern.MESSAGE));
    }

    @Test
    @DisplayName("주문 등록시 상품 정보를 찾을 수 없을 경우 404 응답")
    void postOrder_notFoundSeller() throws Exception {
        OrderCreateRequest.OrderProductDto orderProductDto = new OrderCreateRequest.OrderProductDto();
        orderProductDto.setProductId(product.getId() + 99);
        orderProductDto.setCount(1);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setOrderState(OrderState.PAYMENT_WAITING.name());
        orderCreateRequest.setOrderProducts(List.of(orderProductDto));

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderCreateRequest))
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ProductNotFoundException.MESSAGE));
    }

    @Test
    @DisplayName("주문 등록 성공")
    void postOrder_success() throws Exception {
        OrderCreateRequest.OrderProductDto orderProductDto = new OrderCreateRequest.OrderProductDto();
        orderProductDto.setProductId(product.getId());
        orderProductDto.setCount(1);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setOrderState(OrderState.PAYMENT_WAITING.name());
        orderCreateRequest.setOrderProducts(List.of(orderProductDto));

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderCreateRequest))
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isCreated());

        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(orderProductRepository.count()).isEqualTo(1);
    }
}