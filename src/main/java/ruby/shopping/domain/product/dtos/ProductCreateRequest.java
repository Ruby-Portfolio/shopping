package ruby.shopping.domain.product.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ruby.shopping.common.valid.*;

@Getter
@Setter
public class ProductCreateRequest {

    @Schema(description = "상품명", example = "플루트")
    @ProductNamePattern
    private String name;
    @Schema(description = "상품 설명", example = "플루트는 관악기입니다.")
    @ProductDescriptionPattern
    private String description;
    @Schema(description = "상품 가격", example = "10000000")
    @PricePattern
    private Integer price;
    @Schema(description = "상품 카테고리", example = "HOBBY")
    @CategoryPattern
    private String category;
    @Schema(description = "셀러 아이디", example = "1")
    @IdPattern
    private Long sellerId;
}
