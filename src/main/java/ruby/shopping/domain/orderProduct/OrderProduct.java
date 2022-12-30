package ruby.shopping.domain.orderProduct;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ruby.shopping.domain.order.Order;
import ruby.shopping.domain.product.Product;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer count;

    @OneToOne(fetch = LAZY)
    private Product product;
    @ManyToOne(fetch = LAZY)
    private Order order;
}
