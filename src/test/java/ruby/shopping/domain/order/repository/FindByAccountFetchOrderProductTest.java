package ruby.shopping.domain.order.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ruby.shopping.TestQueryDslConfig;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.order.Order;
import ruby.shopping.domain.order.OrderRepository;
import ruby.shopping.domain.order.dtos.OrderResponse;
import ruby.shopping.domain.order.enums.OrderState;
import ruby.shopping.domain.orderProduct.OrderProduct;
import ruby.shopping.domain.orderProduct.OrderProductRepository;
import ruby.shopping.domain.product.Product;
import ruby.shopping.domain.product.ProductRepository;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.seller.Seller;
import ruby.shopping.domain.seller.SellerRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class FindByAccountFetchOrderProductTest {

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
    EntityManager entityManager;

    Account account;
    Order order;
    List<OrderProduct> orderProducts;

    @BeforeEach
    void before() {
        String email = "test@gmail.com";
        String password = "1234qwer!@";
        account = Account.builder()
                .email(email)
                .password(password)
                .build();
        accountRepository.save(account);

        Seller seller = Seller.builder()
                .sellerName("김루비")
                .accountNumber("013124512311")
                .accountHolder("김예금주")
                .account(account)
                .build();
        sellerRepository.save(seller);

        Product product = Product.builder()
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

        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(product)
                .count(1)
                .order(order)
                .build();
        orderProductRepository.save(orderProduct1);
        OrderProduct orderProduct2 = OrderProduct.builder()
                .product(product)
                .count(3)
                .order(order)
                .build();
        orderProductRepository.save(orderProduct2);
        orderProducts = List.of(orderProduct1, orderProduct2);

        entityManager.flush();
        entityManager.clear();
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
    @DisplayName("회원의 주문 목록 조회")
    void test_findByAccountFetchOrderProduct() {
        List<OrderResponse> orders = orderRepository.findByAccountFetchOrderProduct(account);

        int totalPrice = orderProducts.stream()
                .mapToInt(orderProduct -> (orderProduct.getCount() * orderProduct.getProduct().getPrice()))
                .sum();

        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getTotalPrice()).isEqualTo(totalPrice);
    }
}