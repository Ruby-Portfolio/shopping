package ruby.shopping.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.product.dtos.ProductCreateRequest;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.seller.Seller;
import ruby.shopping.domain.seller.SellerRepository;
import ruby.shopping.domain.seller.exception.SellerNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public void createProduct(ProductCreateRequest productCreateRequest, Account account) {
        Seller seller = sellerRepository.findByIdAndAccount(productCreateRequest.getSellerId(), account)
                .orElseThrow(SellerNotFoundException::new);

        Product product = Product.builder()
                .name(productCreateRequest.getName())
                .description(productCreateRequest.getDescription())
                .price(productCreateRequest.getPrice())
                .category(Category.valueOf(productCreateRequest.getCategory()))
                .seller(seller)
                .build();

        productRepository.save(product);
    }
}
