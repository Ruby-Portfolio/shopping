package ruby.shopping.domain.product;

import org.springframework.data.domain.Page;
import ruby.shopping.domain.product.dtos.ProductSearchRequest;

import java.util.List;

public interface ProductRepositoryCustom  {
    Page<Product> findBySearch(ProductSearchRequest productSearchRequest);
}
