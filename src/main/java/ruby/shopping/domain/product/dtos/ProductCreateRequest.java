package ruby.shopping.domain.product.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {

    private String name;
    private String description;
    private Integer price;
    private String category;
    private Long sellerId;
}
