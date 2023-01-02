package ruby.shopping.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/** 공통 예외처리 응답 Dto */
@Getter
public class ErrorResponse {

    @Schema(description = "예외 메시지")
    private final String message;
    @Schema(description = "요청값 검증예외 목록")
    private Map<String, String> validations;

    @Builder
    public ErrorResponse(String message) {
        this.message = message;
    }

    public void addValidation(FieldError fieldError) {
        if (validations == null) this.validations = new HashMap<>();

        this.validations.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
