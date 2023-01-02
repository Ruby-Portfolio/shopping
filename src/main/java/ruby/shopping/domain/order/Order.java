package ruby.shopping.domain.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ruby.shopping.domain.account.Account;
import ruby.shopping.domain.order.enums.OrderState;
import ruby.shopping.domain.orderProduct.OrderProduct;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Orders")
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState orderState;

    @ManyToOne(fetch = LAZY)
    private Account account;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private final List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    public Order(OrderState orderState, Account account) {
        this.account = account;
        this.orderState = orderState;
    }

    public void updateState(OrderState orderState) {
        this.orderState = orderState;
    }
}
