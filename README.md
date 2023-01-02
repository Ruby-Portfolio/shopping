# Shopping

### 개요
- 쇼핑몰 REST API 개발

### 개발기간
- 2022.12.30 ~ 2023.1.2

### 적용 기술
- Back-End
  - Java, Spring, Spring MVC, Spring Data JPA, QueryDSL
- DB
  - H2 Database

### 테이블 설계
<img width="1092" alt="image" src="https://user-images.githubusercontent.com/93859705/210193893-af3e1e69-1f11-4927-add5-5a639645c725.png">

### 기능 구현
- 회원
  - 회원가입
    - 이메일, 비밀번호를 요청값으로 받아 회원가입 요청
    - 이메일 중복 가입시 409 응답 처리
  - 로그인
    - 이메일, 비밀번호를 요청값으로 받아 로그인 요청
    - 로그인 성공시 JWT 토큰을 생성하여 응답 헤더에 포함
- 셀러
  - 셀러 등록
    - 판매자 명, 계좌번호, 예금주를 요청값으로 받아 셀러 등록 요청
- 상품
  - 상품 등록
    - 상품명, 상품 설명, 상품가격, 상품 카테고리, 셀러 아이디를 요청값으로 받아 상품 등록 요청
- 주문
  - 주문 등록
    - 주문상태, 판매자 회원 아이디, 주문 상품 목록 값을 요청값받아 주문 등록 요청
    - 주문 상품은 상품 아이디와 주문 개수 값으로 구성됨
  - 주문 취소
    - 주문 아이디 값을 요청값으로 받아 주문 취소 요청

### API Docs
- http://localhost:8080/swagger-ui.html
- JWT 인증 설정
  - API 요청시 JWT 인증 필요
    <img width="923" alt="image" src="https://user-images.githubusercontent.com/93859705/210195664-ece25cac-2d32-4a3d-8b75-1128abbbc34c.png">
    ![image](https://user-images.githubusercontent.com/93859705/210195754-499311f1-57cc-46fc-9d90-d4f81e10e479.png)
    *로그인 성공 응답으로 받은 토큰 값을 이후 요청에 적용*