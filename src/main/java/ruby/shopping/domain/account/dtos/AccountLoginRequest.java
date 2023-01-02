package ruby.shopping.domain.account.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountLoginRequest {

    @Schema(description = "회원 이메일", example = "ruby0723@gmail.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @Schema(description = "비밀번호", example = "qwer1234%")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
