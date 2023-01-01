package ruby.shopping.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.order.dtos.OrderCancelRequest;
import ruby.shopping.domain.order.dtos.OrderCreateRequest;
import ruby.shopping.domain.orderProduct.OrderProduct;
import ruby.shopping.domain.orderProduct.OrderProductRepository;
import ruby.shopping.domain.product.Product;
import ruby.shopping.domain.product.ProductRepository;
import ruby.shopping.domain.product.exception.ProductNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    public void createOrder(OrderCreateRequest orderCreateRequest, Account account) {
        List<Long> productIds = orderCreateRequest.getOrderProducts().stream()
                .map(OrderCreateRequest.OrderProductDto::getProductId)
                .collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productIds);

        Order order = Order.builder()
                .account(account)
                .build();
        orderRepository.save(order);

        List<OrderCreateRequest.OrderProductDto> orderProducts = orderCreateRequest.getOrderProducts();
        orderProducts.forEach(
                orderProductDto -> {
                    Product targetProduct = products.stream()
                            .filter(product -> product.getId().equals(orderProductDto.getProductId()))
                            .findFirst()
                            .orElseThrow(ProductNotFoundException::new);
                    OrderProduct orderProduct = OrderProduct.builder()
                            .order(order)
                            .product(targetProduct)
                            .count(orderProductDto.getCount())
                            .build();
                    orderProductRepository.save(orderProduct);
                }
        );
    }

    public void cancelOrder(OrderCancelRequest orderCancelRequest, Account account) {}
}
