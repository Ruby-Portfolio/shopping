package ruby.shopping.domain.order;

import ruby.shopping.domain.account.Account;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findByAccountFetchOrderProduct(Account account);
}
