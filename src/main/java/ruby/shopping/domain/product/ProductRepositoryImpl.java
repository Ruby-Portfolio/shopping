package ruby.shopping.domain.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import ruby.shopping.domain.product.dtos.ProductSearchRequest;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.seller.QSeller;

import java.util.List;

import static ruby.shopping.domain.order.QOrder.order;
import static ruby.shopping.domain.orderProduct.QOrderProduct.orderProduct;
import static ruby.shopping.domain.product.QProduct.product;
import static ruby.shopping.domain.seller.QSeller.*;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Product> findBySearch(ProductSearchRequest productSearchRequest) {
        int page = productSearchRequest.getPage() - 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);

        List<Product> products = jpaQueryFactory.selectFrom(product)
                .leftJoin(seller, product.seller).fetchJoin()
                .where(getExpressionProductSearch(productSearchRequest))
                .limit(pageSize)
                .offset(pageable.getOffset())
                .orderBy(product.id.desc())
                .fetch();

        JPAQuery<Product> countQuery = jpaQueryFactory.selectFrom(product)
                .leftJoin(seller, product.seller)
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
            booleanExpression.and(product.category.eq(category));
        }

        return booleanExpression;
    }
}
