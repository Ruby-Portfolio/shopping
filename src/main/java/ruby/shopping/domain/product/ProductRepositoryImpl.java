package ruby.shopping.domain.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import ruby.shopping.domain.product.dtos.ProductItemDto;
import ruby.shopping.domain.product.dtos.ProductSearchRequest;
import ruby.shopping.domain.product.dtos.QProductItemDto;
import ruby.shopping.domain.product.enums.Category;

import java.util.List;

import static ruby.shopping.domain.product.QProduct.product;
import static ruby.shopping.domain.seller.QSeller.seller;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ProductItemDto> findBySearch(ProductSearchRequest productSearchRequest) {
        int page = productSearchRequest.getPage() - 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);

        List<ProductItemDto> products = jpaQueryFactory
                .select(
                        new QProductItemDto(product.id, product.name, product.price, product.category, seller.sellerName)
                )
                .from(product)
                .leftJoin(product.seller, seller)
                .where(getExpressionProductSearch(productSearchRequest))
                .limit(pageSize)
                .offset(pageable.getOffset())
                .orderBy(product.id.desc())
                .fetch();

        JPAQuery<Product> countQuery = jpaQueryFactory.selectFrom(product)
                .leftJoin(product.seller, seller)
                .where(getExpressionProductSearch(productSearchRequest));

        return PageableExecutionUtils.getPage(products, pageable, () -> countQuery.fetch().size());
    }

    public BooleanExpression getExpressionProductSearch(ProductSearchRequest productSearchRequest) {
        String keyword = productSearchRequest.getKeyword();
        BooleanExpression booleanExpression = product.name.contains(keyword)
                .and(product.description.contains(keyword));

        String categoryString = productSearchRequest.getCategory();
        if (categoryString != null) {
            Category category = Category.valueOf(categoryString);
            booleanExpression = booleanExpression.and(product.category.eq(category));
        }

        return booleanExpression;
    }
}
