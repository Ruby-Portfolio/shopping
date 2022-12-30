package ruby.shopping.domain.product.enums;

import lombok.Getter;

@Getter
public enum Category {
    CLOTHING("의류"),
    BAG("가방"),
    WALLET("지갑/파우치"),
    SHOES("신발"),
    GOLF_EQUIPMENT("골프 웨어/용품"),
    TENNIS_EQUIPMENT("테니스 웨어/용품"),
    HAT("모자/비니"),
    JEWELRY("쥬얼리"),
    FASHION_ACCESSORIES("패션소품"),
    SKIN("스킨/바디/헤어"),
    MAKE_UP("메이크업"),
    PERFUME("향수/방향"),
    SNACK("스낵/음료/시럽"),
    SEASONING("간편/양념/오일"),
    NUTRIENTS("영양제"),
    KITCHEN("홈데코/주방/욕실"),
    HOME_APPLIANCES("가전/디지털"),
    HOBBY("취미/수집"),
    CAMPING("캠핑/차량 용품"),
    BABY("베이비/키즈"),
    PET("반려동물"),
    VINTAGE("빈티지/엔티크");

    private final String value;

    Category(String value) {
        this.value = value;
    }
}
