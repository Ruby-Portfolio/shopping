package ruby.shopping.domain.seller.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ruby.shopping.common.valid.AccountNumberPattern;
import ruby.shopping.common.valid.UsernamePattern;

@Getter
@Setter
public class SellerCreateRequest {

    @Schema(description = "판매자 명", example = "김용범")
    @UsernamePattern
    private String sellerName;
    @Schema(description = "계좌번호", example = "1100131241244")
    @AccountNumberPattern
    private String accountNumber;
    @Schema(description = "예금주", example = "김루비")
    @UsernamePattern
    private String accountHolder;
}
