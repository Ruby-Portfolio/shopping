package ruby.shopping.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ruby.shopping.common.ExceptionController;
import ruby.shopping.common.valid.CategoryPattern;
import ruby.shopping.common.valid.PagePattern;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.account.AccountRepository;
import ruby.shopping.domain.product.Product;
import ruby.shopping.domain.product.ProductRepository;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.seller.Seller;
import ruby.shopping.domain.seller.SellerRepository;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetProductsTest {

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
    }

    @AfterEach
    void after() {
        productRepository.deleteAll();
        sellerRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 목록 검색 조회시 요청 값이 잘못된 경우 400 응답")
    void getProducts_badRequest() throws Exception {
        String category = "알수없는 카테고리";
        int page = 0;

        mockMvc.perform(get("/api/products")
                        .param("category", category)
                        .param("page", String.valueOf(page))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionController.BIND_EXCEPTION_MESSAGE))
                .andExpect(jsonPath("$.validations.category").value(CategoryPattern.MESSAGE))
                .andExpect(jsonPath("$.validations.page").value(PagePattern.MESSAGE));
    }

    @Test
    @DisplayName("상품 목록 검색 조회시 키워드에 일치하는 상품이 없을 경우")
    void getProducts_notContainsKeyword() throws Exception {
        String keyword = "플루트";
        String category = Category.HAT.name();
        int page = 1;

        mockMvc.perform(get("/api/products")
                        .param("keyword", keyword)
                        .param("category", category)
                        .param("page", String.valueOf(page))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(0))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.totalPage").value(0));
    }

    @Test
    @DisplayName("상품 목록 검색 조회시 카테고리가 일치하는 상품이 없을 경우")
    void getProducts_notFoundCategory() throws Exception {
        String keyword = "바이올린";
        String category = Category.HOBBY.name();
        int page = 1;

        mockMvc.perform(get("/api/products")
                        .param("keyword", keyword)
                        .param("category", category)
                        .param("page", String.valueOf(page))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(0))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.totalPage").value(0));
    }

    @Test
    @DisplayName("상품 목록 검색 조회 성공")
    void getProducts_success() throws Exception {
        String keyword = "플루트";
        String category = Category.HOBBY.name();
        int page = 4;

        mockMvc.perform(get("/api/products")
                        .param("keyword", keyword)
                        .param("category", category)
                        .param("page", String.valueOf(page))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(9))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.totalPage").value(4));
    }

    @Test
    @DisplayName("키워드 없이 상품 목록 검색 조회")
    void getProducts_success_noneKeyword() throws Exception {
        String category = Category.HOBBY.name();
        int page = 4;

        mockMvc.perform(get("/api/products")
                        .param("category", category)
                        .param("page", String.valueOf(page))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(9))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.totalPage").value(4));
    }

    @Test
    @DisplayName("카테고리 없이 상품 목록 검색 조회")
    void getProducts_success_noneCategory() throws Exception {
        String keyword = "플루트";
        int page = 4;

        mockMvc.perform(get("/api/products")
                        .param("keyword", keyword)
                        .param("page", String.valueOf(page))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(9))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.totalPage").value(4));
    }

    @Test
    @DisplayName("검색 조건 없이 상품 목록 검색 조회")
    void getProducts_success_noneCondition() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(10))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalPage").value(8));
    }
}
