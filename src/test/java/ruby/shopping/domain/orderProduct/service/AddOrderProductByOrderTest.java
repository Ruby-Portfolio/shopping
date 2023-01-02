package ruby.shopping.domain.orderProduct.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ruby.shopping.domain.order.Order;
import ruby.shopping.domain.order.dtos.OrderCreateRequest;
import ruby.shopping.domain.orderProduct.OrderProductRepository;
import ruby.shopping.domain.orderProduct.OrderProductService;
import ruby.shopping.domain.product.Product;
import ruby.shopping.domain.product.ProductRepository;
import ruby.shopping.domain.product.exception.ProductNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AddOrderProductByOrderTest {

    @Mock
    OrderProductRepository orderProductRepository;
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    OrderProductService orderProductService;

    @Test
    @DisplayName("상품 정보를 찾을 수 없을 경우 ProductNotFoundException 예외 처리")
    void addOrderProductByOrder_notFoundProduct() {
        Order order = Order.builder().build();
        List<Long> productIds = List.of(1L, 2L);
        List<OrderCreateRequest.OrderProductDto> orderProductDtos = productIds.stream()
                .map(id -> {
                    OrderCreateRequest.OrderProductDto orderProductDto = new OrderCreateRequest.OrderProductDto();
                    orderProductDto.setProductId(id);
                    orderProductDto.setCount(3);
                    return orderProductDto;
                }).collect(Collectors.toList());
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setOrderProducts(orderProductDtos);

        Mockito.when(productRepository.findAllById(productIds))
                .thenReturn(List.of(Product.builder().id(1L).build()));

        assertThatThrownBy(() -> orderProductService.addOrderProductByOrder(orderProductDtos, order))
                .isInstanceOf(ProductNotFoundException.class);
    }


    @Test
    @DisplayName("주문상품 추가 성공")
    void addOrderProductByOrder_success() {
        Order order = Order.builder().build();
        List<Long> productIds = List.of(1L, 2L);
        List<OrderCreateRequest.OrderProductDto> orderProductDtos = productIds.stream()
                .map(id -> {
                    OrderCreateRequest.OrderProductDto orderProductDto = new OrderCreateRequest.OrderProductDto();
                    orderProductDto.setProductId(id);
                    orderProductDto.setCount(3);
                    return orderProductDto;
                }).collect(Collectors.toList());
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setOrderProducts(orderProductDtos);

        Mockito.when(productRepository.findAllById(productIds))
                .thenReturn(List.of(Product.builder().id(1L).build(), Product.builder().id(2L).build()));

        assertThatCode(() -> orderProductService.addOrderProductByOrder(orderProductDtos, order))
                .doesNotThrowAnyException();
    }
}