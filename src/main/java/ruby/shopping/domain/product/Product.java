package ruby.shopping.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ruby.shopping.domain.product.enums.Category;
import ruby.shopping.domain.seller.Seller;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 5000)
    private String description;
    @Column(nullable = false)
    private Integer price;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;
    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;

    @ManyToOne(fetch = LAZY)
    private Seller seller;

    @Builder
    public Product(Long id, String name, String description, Integer price, Category category, Seller seller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.seller = seller;
    }
}
