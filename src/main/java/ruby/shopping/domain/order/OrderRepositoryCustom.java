package ruby.shopping.domain.order;

import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.order.dtos.OrderResponse;

import java.util.List;

public interface OrderRepositoryCustom {

    List<OrderResponse> findByAccountFetchOrderProduct(Account account);
}
