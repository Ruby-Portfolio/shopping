package ruby.shopping.domain.product;

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
import ruby.shopping.domain.product.dtos.ProductCreateRequest;
import ruby.shopping.security.LoginAccount;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록")
    @SecurityRequirement(name = "Bearer Authorization")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "상품 등록 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "상품 등록 요청값 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "셀러의 정보를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void postProduct(
            @RequestBody @Valid ProductCreateRequest productCreateRequest,
            @LoginAccount @Parameter(hidden = true) Account account) {
        productService.createProduct(productCreateRequest, account);
    }
}
