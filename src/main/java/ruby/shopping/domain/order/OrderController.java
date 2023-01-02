package ruby.shopping.domain.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ruby.shopping.common.ErrorResponse;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.order.dtos.OrderCreateRequest;
import ruby.shopping.domain.order.dtos.OrdersResponse;
import ruby.shopping.security.LoginAccount;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 등록")
    @SecurityRequirement(name = "Bearer Authorization")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "주문 등록 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "주문 등록 요청값 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "상품 정보를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void postOrder(
            @RequestBody @Valid OrderCreateRequest orderCreateRequest,
            @LoginAccount @Parameter(hidden = true) Account account) {
        orderService.createOrder(orderCreateRequest, account);
    }

    @Operation(summary = "주문 목록 조회")
    @SecurityRequirement(name = "Bearer Authorization")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주문 목록 조회 성공"
            )
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public OrdersResponse getOrders(@LoginAccount @Parameter(hidden = true) Account account) {
        List<Order> orders = orderService.getOrders(account);
        return new OrdersResponse(orders);
    }

    @Operation(summary = "주문 취소")
    @SecurityRequirement(name = "Bearer Authorization")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주문 취소 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "주문 정보를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{orderId}")
    public void patchOrder(
            @PathVariable @Parameter(name = "orderId", description = "주문 아이디") Long orderId,
            @LoginAccount @Parameter(hidden = true) Account account) {
        orderService.cancelOrder(orderId, account);
    }
}
