package ruby.shopping.domain.product.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ProductsResponse {

    @Schema(description = "상품 목록")
    private final List<ProductItemDto> products;
    @Schema(description = "조회 페이지", example = "1")
    private final int page;
    @Schema(description = "페이지 전체 수", example = "1")
    private final int totalPage;

    public ProductsResponse(Page<ProductItemDto> productsPage) {
        this.products = productsPage.getContent();
        this.page = productsPage.getNumber() + 1;
        this.totalPage = productsPage.getTotalPages();
    }
}
