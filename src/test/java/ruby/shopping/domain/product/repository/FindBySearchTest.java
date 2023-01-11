package ruby.shopping.domain.product.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import ruby.shopping.TestQueryDslConfig;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.product.Product;
import ruby.shopping.domain.product.ProductRepository;
import ruby.shopping.domain.product.dtos.ProductSearchRequest;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.seller.Seller;
import ruby.shopping.domain.seller.SellerRepository;

import javax.persistence.EntityManager;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class FindBySearchTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void before() {
        String email = "test@gmail.com";
        String password = "1234qwer!@";
        Account account = Account.builder()
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

        IntStream.range(0, 77).forEach(idx -> {
            String productName = idx % 2 == 0 ? "플루트" : "노트북" + idx;
            Category category = idx % 2 == 0 ? Category.HOBBY : Category.HOME_APPLIANCES;

            Product product = Product.builder()
                    .name(productName)
                    .price(10000000)
                    .description(productName + "입니다!")
                    .category(category)
                    .seller(seller)
                    .build();
            productRepository.save(product);
        });

        entityManager.flush();
        entityManager.clear();
    }

    @AfterEach
    void after() {
        productRepository.deleteAll();
        sellerRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 목록 검색 조회시 키워드에 일치하는 상품이 없을 경우")
    void findBySearch_notContainsKeyword() {
        String keyword = "플루트";
        String category = Category.HAT.name();
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setKeyword(keyword);
        productSearchRequest.setCategory(category);
        productSearchRequest.setPage(1);

        Page<Product> productPage = productRepository.findBySearch(productSearchRequest);
        assertThat(productPage.getNumber()).isEqualTo(0);
        assertThat(productPage.getTotalPages()).isEqualTo(0);
    }

    @Test
    @DisplayName("상품 목록 검색 조회시 카테고리가 일치하는 상품이 없을 경우")
    void findBySearch_notFoundCategory() {
        String keyword = "바이올린";
        String category = Category.HOBBY.name();
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setKeyword(keyword);
        productSearchRequest.setCategory(category);
        productSearchRequest.setPage(1);

        Page<Product> productPage = productRepository.findBySearch(productSearchRequest);
        assertThat(productPage.getNumber()).isEqualTo(0);
        assertThat(productPage.getTotalPages()).isEqualTo(0);
    }

    @Test
    @DisplayName("상품 목록 검색 조회")
    void findBySearch_success() {
        String keyword = "플루트";
        String category = Category.HOBBY.name();
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setKeyword(keyword);
        productSearchRequest.setCategory(category);
        productSearchRequest.setPage(4);

        Page<Product> productPage = productRepository.findBySearch(productSearchRequest);
        assertThat(productPage.getContent().stream()
                        .allMatch(product -> product.getName().contains(keyword))
        ).isTrue();
        assertThat(productPage.getContent().stream()
                .allMatch(product -> product.getDescription().contains(keyword))
        ).isTrue();
        assertThat(productPage.getContent().stream()
                .allMatch(product -> product.getCategory().equals(Category.valueOf(category)))
        ).isTrue();
        assertThat(productPage.getNumber()).isEqualTo(3);
        assertThat(productPage.getTotalPages()).isEqualTo(4);
    }

    @Test
    @DisplayName("키워드 없이 상품 목록 검색 조회")
    void findBySearch_success_noneKeyword() {
        String category = Category.HOBBY.name();
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setCategory(category);
        productSearchRequest.setPage(4);

        Page<Product> productPage = productRepository.findBySearch(productSearchRequest);
        assertThat(productPage.getContent().stream()
                .allMatch(product -> product.getCategory().equals(Category.valueOf(category)))
        ).isTrue();
        assertThat(productPage.getNumber()).isEqualTo(3);
        assertThat(productPage.getTotalPages()).isEqualTo(4);
    }

    @Test
    @DisplayName("카테고리 없이 상품 목록 검색 조회")
    void findBySearch_success_noneCategory() {
        String keyword = "플루트";
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setKeyword(keyword);
        productSearchRequest.setPage(4);

        Page<Product> productPage = productRepository.findBySearch(productSearchRequest);
        assertThat(productPage.getContent().stream()
                .allMatch(product -> product.getName().contains(keyword))
        ).isTrue();
        assertThat(productPage.getContent().stream()
                .allMatch(product -> product.getDescription().contains(keyword))
        ).isTrue();
        assertThat(productPage.getNumber()).isEqualTo(3);
        assertThat(productPage.getTotalPages()).isEqualTo(4);
    }

    @Test
    @DisplayName("검색 조건 없이 상품 목록 검색 조회")
    void findBySearch_success_noneCondition() {
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setPage(4);

        Page<Product> productPage = productRepository.findBySearch(productSearchRequest);
        assertThat(productPage.getTotalElements()).isEqualTo(productRepository.count());
    }
}