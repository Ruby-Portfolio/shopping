package ruby.shopping.domain.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.product.QProduct;

import java.util.List;

import static ruby.shopping.domain.order.QOrder.order;
import static ruby.shopping.domain.orderProduct.QOrderProduct.orderProduct;
import static ruby.shopping.domain.product.QProduct.*;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Order> findByAccountFetchOrderProduct(Account account) {
        return jpaQueryFactory.selectFrom(order)
                .leftJoin(order.orderProducts, orderProduct).fetchJoin()
                .leftJoin(orderProduct.product, product).fetchJoin()
                .where(order.account.eq(account))
                .distinct()
                .fetch();
    }
}
