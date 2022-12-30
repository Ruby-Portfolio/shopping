package ruby.shopping.domain.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ruby.shopping.common.ErrorResponse;
import ruby.shopping.domain.account.dtos.AccountSignUpRequest;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "회원가입 요청값 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "회원 ID 중복",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signUp")
    public void signup(@RequestBody @Valid AccountSignUpRequest accountSignUpRequest) {
        accountService.signUp(accountSignUpRequest);
    }
}
