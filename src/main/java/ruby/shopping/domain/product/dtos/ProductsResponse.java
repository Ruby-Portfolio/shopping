package ruby.shopping.domain.product.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;
import ruby.shopping.domain.product.Product;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductsResponse {

    @Schema(description = "상품 목록")
    private final List<ProductItem> products;
    @Schema(description = "조회 페이지")
    private final int page;
    @Schema(description = "페이지 전체 수")
    private final int totalPage;

    public ProductsResponse(Page<Product> productsPage) {
        this.products = productsPage.getContent().stream()
                .map(ProductItem::new)
                .collect(Collectors.toList());
        this.page = productsPage.getNumber() + 1;
        this.totalPage = productsPage.getTotalPages();
    }

    @Getter
    static class ProductItem {
        @Schema(description = "상품 아이디")
        private final Long productId;
        @Schema(description = "상품명")
        private final String productName;
        @Schema(description = "상품 가격")
        private final Integer price;
        @Schema(description = "상품 카테고리")
        private final String category;
        @Schema(description = "판매자 명")
        private final String sellerName;

        ProductItem(Product product) {
            this.productId = product.getId();
            this.productName = product.getName();
            this.price = product.getPrice();
            this.category = product.getCategory().name();
            this.sellerName = product.getSeller().getSellerName();
        }
    }
}
