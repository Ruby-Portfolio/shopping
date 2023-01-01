package ruby.shopping.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.order.dtos.OrderCancelRequest;
import ruby.shopping.domain.order.dtos.OrderCreateRequest;
import ruby.shopping.domain.orderProduct.OrderProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;

    public void createOrder(OrderCreateRequest orderCreateRequest, Account account) {
        Order order = Order.builder()
                .account(account)
                .build();
        orderRepository.save(order);

        orderProductService.addOrderProductByOrder(orderCreateRequest.getOrderProducts(), order);
    }

    public List<Order> getOrders(Account account) {
        return orderRepository.findByAccountFetchOrderProduct(account);
    }

    public void cancelOrder(OrderCancelRequest orderCancelRequest, Account account) {}
}
