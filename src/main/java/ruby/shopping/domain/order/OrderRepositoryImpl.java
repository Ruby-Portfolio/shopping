package ruby.shopping.domain.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.order.dtos.OrderResponse;
import ruby.shopping.domain.order.dtos.QOrderResponse;

import java.util.List;

import static ruby.shopping.domain.order.QOrder.order;
import static ruby.shopping.domain.orderProduct.QOrderProduct.orderProduct;
import static ruby.shopping.domain.product.QProduct.product;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderResponse> findByAccountFetchOrderProduct(Account account) {
        return jpaQueryFactory.
                select(
                    new QOrderResponse(order.id, order.createAt, product.price.multiply(orderProduct.count).sum())
                )
                .from(order)
                .leftJoin(order.orderProducts, orderProduct)
                .leftJoin(orderProduct.product, product)
                .where(order.account.eq(account))
                .groupBy(order.id)
                .fetch();
    }
}
