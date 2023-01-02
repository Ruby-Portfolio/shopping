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
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.order.Order;
import ruby.shopping.domain.order.OrderRepository;
import ruby.shopping.domain.order.enums.OrderState;
import ruby.shopping.domain.orderProduct.OrderProduct;
import ruby.shopping.domain.orderProduct.OrderProductRepository;
import ruby.shopping.domain.product.Product;
import ruby.shopping.domain.product.ProductRepository;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.seller.Seller;
import ruby.shopping.domain.seller.SellerRepository;
import ruby.shopping.security.jwt.JwtFilter;
import ruby.shopping.security.jwt.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CancelOrderTest {

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
    Order order;
    OrderProduct orderProduct;
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

        order = Order.builder()
                .account(account)
                .orderState(OrderState.PAYMENT_WAITING)
                .build();
        orderRepository.save(order);

        orderProduct = OrderProduct.builder()
                .product(product)
                .count(3)
                .order(order)
                .build();
        orderProductRepository.save(orderProduct);

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
    @DisplayName("주문 취소 요청시 로그인 하지 않은 상태일 경우 401 응답")
    void cancelOrder_unauthorized() throws Exception {
        mockMvc.perform(patch("/api/orders/{orderId}", order.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("주문 아이디에 해당하는 주문을 찾을 수 없을 경우 404 응답")
    void cancelOrder_NotFoundOrder() throws Exception {
        mockMvc.perform(patch("/api/orders/{orderId}", order.getId() + 99)
                        .contentType(APPLICATION_JSON)
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("주문 취소 성공")
    void cancelOrder_success() throws Exception {
        mockMvc.perform(patch("/api/orders/{orderId}", order.getId())
                        .contentType(APPLICATION_JSON)
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isOk());

        Order cancelOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(cancelOrder.getOrderState()).isEqualTo(OrderState.CANCEL);
    }
}