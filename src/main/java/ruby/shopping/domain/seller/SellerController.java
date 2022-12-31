package ruby.shopping.domain.seller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ruby.shopping.common.ErrorResponse;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.seller.dtos.SellerCreateRequest;
import ruby.shopping.security.AccountDetails;
import ruby.shopping.security.LoginAccount;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    @Operation(summary = "셀러 등록")
    @SecurityRequirement(name = "Bearer Authorization")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "셀러 등록 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "셀러 등록 요청값 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void postSeller(
            @RequestBody @Valid SellerCreateRequest sellerCreateRequest,
            @LoginAccount Account account) {
        sellerService.createSeller(sellerCreateRequest, account);
    }
}
