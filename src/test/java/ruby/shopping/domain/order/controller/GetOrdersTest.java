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

import java.time.format.DateTimeFormatter;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GetOrdersTest {

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
                .sellerName("?????????")
                .accountNumber("013124512311")
                .accountHolder("????????????")
                .account(account)
                .build();
        sellerRepository.save(seller);
        
        product = Product.builder()
                .name("?????????")
                .price(10000000)
                .description("?????????")
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
    @DisplayName("?????? ?????? ????????? ????????? ?????? ?????? ????????? ?????? 401 ??????")
    void getOrders_unauthorized() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void getOrders_success() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .header(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].orderId").value(order.getId()))
                .andExpect(jsonPath("$.[0].createAt")
                        .value(product.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.[0].totalPrice").value(product.getPrice() * orderProduct.getCount()));
    }
}