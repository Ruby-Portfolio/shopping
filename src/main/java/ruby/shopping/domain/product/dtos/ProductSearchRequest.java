package ruby.shopping.domain.product.dtos;

import lombok.Getter;
import lombok.Setter;
import ruby.shopping.common.valid.CategoryPattern;
import ruby.shopping.common.valid.PagePattern;

@Getter
@Setter
public class ProductSearchRequest {
    private String keyword = "";
    @CategoryPattern
    private String category;
    @PagePattern
    private int page = 1;
}
