package ruby.shopping.domain.orderProduct;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.shopping.domain.order.Order;
import ruby.shopping.domain.order.dtos.OrderCreateRequest;
import ruby.shopping.domain.product.Product;
import ruby.shopping.domain.product.ProductRepository;
import ruby.shopping.domain.product.exception.ProductNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    public void addOrderProductByOrder(List<OrderCreateRequest.OrderProductDto> orderProducts, Order order) {
        List<Long> productIds = orderProducts.stream()
                .map(OrderCreateRequest.OrderProductDto::getProductId)
                .collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productIds);

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
}
