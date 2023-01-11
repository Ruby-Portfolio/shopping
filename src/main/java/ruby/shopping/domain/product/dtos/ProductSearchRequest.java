package ruby.shopping.domain.product.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {
    private String keyword = "";
    private String category;
    private int page = 0;
}
