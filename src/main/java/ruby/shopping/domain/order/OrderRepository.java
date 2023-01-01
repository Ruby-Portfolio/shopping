package ruby.shopping.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import ruby.shopping.domain.account.Account;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    Optional<Order> findByIdAndAccount(Long id, Account account);
}
