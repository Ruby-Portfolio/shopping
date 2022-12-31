package ruby.shopping.domain.product.dtos;

import lombok.Getter;
import lombok.Setter;
import ruby.shopping.common.valid.*;

@Getter
@Setter
public class ProductCreateRequest {

    @ProductNamePattern
    private String name;
    @ProductDescriptionPattern
    private String description;
    @PricePattern
    private Integer price;
    @CategoryPattern
    private String category;
    @IdPattern
    private Long sellerId;
}
