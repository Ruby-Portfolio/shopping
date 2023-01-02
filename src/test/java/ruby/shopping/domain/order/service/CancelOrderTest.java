package ruby.shopping.domain.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.order.Order;
import ruby.shopping.domain.order.OrderRepository;
import ruby.shopping.domain.order.OrderService;
import ruby.shopping.domain.order.exception.OrderNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CancelOrderTest {
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("주문 정보를 찾을 수 없을 경우 예외 처리")
    void cancelOrder_notFoundOrder() {
        Long orderId = 1L;
        Account account = new Account("", "");
        Mockito.when(orderRepository.findByIdAndAccount(orderId, account))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.cancelOrder(orderId, account))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("주문 취소 성공")
    void cancelOrder_success() {
        Long orderId = 1L;
        Account account = new Account("", "");
        Mockito.when(orderRepository.findByIdAndAccount(orderId, account))
                .thenReturn(Optional.of(Order.builder().build()));

        assertThatCode(() -> orderService.cancelOrder(orderId, account))
                .doesNotThrowAnyException();
    }
}