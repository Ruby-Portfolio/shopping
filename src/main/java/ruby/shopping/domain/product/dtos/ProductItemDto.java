package ruby.shopping.domain.product.dtos;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import ruby.shopping.domain.product.enums.Category;

@Getter
public class ProductItemDto {
    @Schema(description = "상품 아이디" , example = "1")
    private final Long productId;
    @Schema(description = "상품명", example = "플루트")
    private final String productName;
    @Schema(description = "상품 가격", example = "10000000")
    private final Integer price;
    @Schema(description = "상품 카테고리", example = "HOBBY")
    private final String category;
    @Schema(description = "판매자 명", example = "Ruby")
    private final String sellerName;

    @QueryProjection
    public ProductItemDto(Long productId, String productName, Integer price, Category category, String sellerName) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.category = category.name();
        this.sellerName = sellerName;
    }
}
