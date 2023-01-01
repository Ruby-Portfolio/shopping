package ruby.shopping.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.order.dtos.OrderCancelRequest;
import ruby.shopping.domain.order.dtos.OrderCreateRequest;
import ruby.shopping.domain.orderProduct.OrderProductService;

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

    public void cancelOrder(OrderCancelRequest orderCancelRequest, Account account) {}
}
