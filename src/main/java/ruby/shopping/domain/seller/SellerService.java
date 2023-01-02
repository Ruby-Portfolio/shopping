package ruby.shopping.domain.seller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.seller.dtos.SellerCreateRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerService {

    private final SellerRepository sellerRepository;

    public void createSeller(SellerCreateRequest sellerCreateRequest, Account account) {
        Seller seller = Seller.builder()
                .sellerName(sellerCreateRequest.getSellerName())
                .accountNumber(sellerCreateRequest.getAccountNumber())
                .accountHolder(sellerCreateRequest.getAccountHolder())
                .account(account)
                .build();

        sellerRepository.save(seller);
    }
}
