package ruby.shopping.domain.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ruby.shopping.common.ErrorResponse;
import ruby.shopping.domain.account.dtos.AccountLoginRequest;
import ruby.shopping.domain.account.dtos.AccountSignUpRequest;
import ruby.shopping.security.jwt.JwtFilter;

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

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    headers = {@Header(name = "Authorization", description = "인증 처리를 위한 JWT 토큰")}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "로그인 요청값 오류. 회원 아이디 또는 비밀번호 값이 빈 값일 경우",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "로그인 실패. 회원 아이디와 비밀번호가 일치하는 회원 정보가 없을 경우",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid AccountLoginRequest accountLoginRequest) {
        String token = accountService.login(accountLoginRequest);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .build();
    }
}
