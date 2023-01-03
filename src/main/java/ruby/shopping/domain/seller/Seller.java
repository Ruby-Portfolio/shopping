package ruby.shopping.domain.seller;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ruby.shopping.domain.account.Account;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String sellerName;
    @Column(nullable = false, length = 30)
    private String accountNumber;
    @Column(nullable = false, length = 20)
    private String accountHolder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Builder
    public Seller(String sellerName, String accountNumber, String accountHolder, Account account) {
        this.sellerName = sellerName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.account = account;
    }
}
