# jpashop
순수 JPA와 Spring MVC를 통해 Web Application을 만들었습니다.  
또한 `MemberRepository`는 JPA Repository에서 Spring Data JPA Repository로 변경해보기도 하였습니다.

lang: Java  
DB: H2  
skill: JPA + Spring Boot  
view template: Thymeleaf  

- Entity 설계 시 주의점
- 회원 도메인 개발
  - 회원 Repository
  - 회원 서비스
- 상품 도메인 개발
  - 상품 엔티티 개발
  - 상품 서비스 개발
- 주문 도메인 개발
  - 주문, 주문상품 엔티티 개발
  - 주문서비스 개발
  - 주문 검색기능 개발
- 웹 계층 개발
  - 홈 화면과 레이아웃
  - 회원등록
  - 회원목록 조회
  - 변경감지와 병합
  - 상품주문
  - 주문목록 검색, 취소


![a](https://user-images.githubusercontent.com/37053970/147380519-8e13b046-e4cf-454d-9b67-28ea8705eff0.png)

[Notion 정리 보러 가기](https://cheddar-limpet-07e.notion.site/Jpa-shop-df7ad37696df49b6a19407a688b5bf6e)  
[강의원본 링크](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1#curriculum)
