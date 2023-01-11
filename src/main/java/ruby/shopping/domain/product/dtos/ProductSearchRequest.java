package ruby.shopping.domain.product.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ruby.shopping.common.valid.CategoryPattern;
import ruby.shopping.common.valid.PagePattern;

@Getter
@Setter
public class ProductSearchRequest {
    @Schema(description = "검색어", example = "플루트")
    private String keyword = "";
    @Schema(description = "카테고리", example = "HOBBY")
    @CategoryPattern
    private String category;
    @Schema(description = "페이지 번호", example = "1")
    @PagePattern
    private int page = 1;
}
