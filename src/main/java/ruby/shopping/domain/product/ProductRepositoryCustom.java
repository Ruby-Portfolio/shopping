package ruby.shopping.domain.product;

import org.springframework.data.domain.Page;
import ruby.shopping.domain.product.dtos.ProductItemDto;
import ruby.shopping.domain.product.dtos.ProductSearchRequest;

import java.util.List;

public interface ProductRepositoryCustom  {
    Page<ProductItemDto> findBySearch(ProductSearchRequest productSearchRequest);
}
