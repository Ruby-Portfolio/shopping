package ruby.shopping.domain.seller.dtos;

import lombok.Getter;
import lombok.Setter;
import ruby.shopping.common.valid.AccountNumberPattern;
import ruby.shopping.common.valid.UsernamePattern;

@Getter
@Setter
public class SellerCreateRequest {

    @UsernamePattern
    private String sellerName;
    @AccountNumberPattern
    private String accountNumber;
    @UsernamePattern
    private String accountHolder;
}
