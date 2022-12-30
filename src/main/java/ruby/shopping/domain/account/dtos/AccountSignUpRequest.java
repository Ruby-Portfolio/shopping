package ruby.shopping.domain.account.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ruby.shopping.common.valid.EmailPattern;
import ruby.shopping.common.valid.PasswordPattern;

@Getter
@Setter
public class AccountSignUpRequest {

    @Schema(description = "이메일", example = "rubykim0723@gmail.com")
    @EmailPattern
    private String email;
    @Schema(description = "비밀번호. 8~50 자 이내. 하나 이상의 영문, 숫자, 특수문자를 모두 포함하여아 함", example = "qwer1234%")
    @PasswordPattern
    private String password;
}
