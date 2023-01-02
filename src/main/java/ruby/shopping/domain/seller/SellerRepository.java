package ruby.shopping.domain.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.shopping.domain.account.Account;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByIdAndAccount(Long id, Account account);
}
