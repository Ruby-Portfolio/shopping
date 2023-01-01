package ruby.shopping.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.product.dtos.ProductCreateRequest;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.seller.Seller;
import ruby.shopping.domain.seller.SellerRepository;
import ruby.shopping.domain.seller.exception.SellerNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    SellerRepository sellerRepository;

    @InjectMocks
    ProductService productService;

    @Test
    @DisplayName("셀러 정보를 찾을 수 없을 경우 예외 처리")
    void createProduct_notFoundSeller() {
        Account account = new Account("", "");
        Mockito.when(sellerRepository.findByIdAndAccount(1L, account))
                .thenReturn(Optional.empty());

        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setName("상품명");
        productCreateRequest.setDescription("상품 설명");
        productCreateRequest.setPrice(10000);
        productCreateRequest.setSellerId(1L);
        productCreateRequest.setCategory(Category.CAMPING.name());

        assertThatThrownBy(() -> productService.createProduct(productCreateRequest, account))
                .isInstanceOf(SellerNotFoundException.class);
    }

    @Test
    @DisplayName("상품 등록 성공")
    void createProduct_success() {
        Account account = new Account("", "");
        Seller seller = Seller.builder()
                .sellerName("셀러")
                .accountNumber("10114112344112")
                .accountHolder("예금주")
                .build();
        Mockito.when(sellerRepository.findByIdAndAccount(1L, account))
                .thenReturn(Optional.of(seller));

        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setName("상품명");
        productCreateRequest.setDescription("상품 설명");
        productCreateRequest.setPrice(10000);
        productCreateRequest.setSellerId(1L);
        productCreateRequest.setCategory(Category.CAMPING.name());

        assertThatCode(() -> productService.createProduct(productCreateRequest, account))
                .doesNotThrowAnyException();
    }
}