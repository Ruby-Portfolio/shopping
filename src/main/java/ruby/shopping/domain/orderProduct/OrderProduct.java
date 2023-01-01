package ruby.shopping.domain.orderProduct;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ruby.shopping.domain.order.Order;
import ruby.shopping.domain.product.Product;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

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

    @Builder
    public OrderProduct(Integer count, Product product, Order order) {
        this.count = count;
        this.product = product;
        this.order = order;
    }
}
