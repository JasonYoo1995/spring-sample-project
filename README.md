- **사용 스택** : Spring Web, JPA, Spring AOP, JUnit4, H2 / MySQL, Thymeleaf, Bootstrap, HTML, CSS, Javascript, JQuery, Ajax
- **개발 기간** : 2021년 8월 11일 ~ 8월 18일
- **시연 영상** : [https://youtu.be/HTz62mclqMQ](https://youtu.be/HTz62mclqMQ)
- **프로젝트 내용 요약**
    - 금융 어플 TOSS와 같이, 계좌를 등록하고 편리하게 송금을 할 수 있는 서비스를 웹 사이트 기반으로 만들었습니다. 단 실제 송금은 아니고, 가상의 화폐로 시뮬레이션을 돌립니다.
    - 실무적인 성격의 DB 설계보다는, 연습적인 성격의 DB 설계를 하였습니다.
    - 로그인된 상태에서만 이용할 수 있는 서비스들에 대해서는, 모든 메서드가 호출될 때마다 세션의 존재 여부를 매번 확인하기 위해 AOP를 사용하였습니다.
    - Service(비즈니스 로직) 계층과 Repository(DAO) 계층에 대한 테스트 코드 14개를 작성하였습니다.
    - 발생 가능한 예외 상황에 대하여 customized exception 객체를 만들어 사용했습니다.
- **제작 문서**
    
    [송금 서비스 (스프링 웹 사이트) 문서](https://jasonyoo95.notion.site/f642fa22803648afa3c477885f503c14)
    
- **어려웠던 점 / 깨달은 점**
    - Persistence Context(영속성 컨텍스트)의 동작 원리를 생각하며 코딩해야 했습니다.
    - EntityManager.persist()를 통한 명시적인 영속화 방법과 Dirty Checking(변경 감지)를 통한 영속화 방법을 둘 다 사용해보면서, 헷갈리는 점들이 많았습니다.
    - EntityManager.merge()는 튜플을 Update할 때 사용하기에 적합하다는 걸 알았습니다.
    - Entity 클래스에 @OneToMany 등의 어노테이션으로 Entity 간 연관 관계를 설정하는 것이 헷갈렸습니다. Foreign Key를 관리하는 클래스가 어느 쪽인지 기억하는 것이 팁인 것 같습니다.
    - 영속성 전이(Cascade) 적용 여부에 따라 어떤 차이가 발생하는지 경험할 수 있었습니다.
    - 비즈니스 로직을 '엔티티 클래스 내부'에 넣느냐 '서비스 계층 클래스 내부'에 넣느냐에 따라 '트랜잭션 스크립트(Transaction Script) 패턴'과 '도메인 모델(Domain Model) 패턴'으로 나뉜다는 걸 알게 됐습니다.
    - JPA가 기본적으로 제공하는 메서드를 통한 쿼리가 아닌 좀 더 복잡한 쿼리나 성능 최적화가 필요한 쿼리는, JPQL을 사용해야 한다는 걸 알게 됐습니다.
        - 고민했던 흔적 : [https://www.inflearn.com/chats/283136](https://www.inflearn.com/chats/283136)
    - @Transactional이 없는 메서드가 @Transactional이 달린 메서드를 호출하는 경우, 두 메서드가 같은 클래스 내에 있으면 트랜잭션이 적용되지 않고, 두 메서드가 각각 다른 클래스에 있어야 트랜잭션이 정상적으로 수행된다는 걸 알게 됐습니다.
- **느낀 점**
    - 스프링 개발은 처음이라, 어떤 코드가 안정성과 성능이 좋은 코드인지 감이 안 왔습니다. 획일성 있는 클린 코드는 못 만든 것 같습니다.
    - 개발 초기와 중반부에도 계속적으로 지금까지 작성한 객체들에 대한 단위 테스트 코드를 통해 오류 없이 코딩하고 있는지 검증하니까, 안심도 되고, 개발 후반부에 코드를 갈아 엎을 일도 없어지며, 디버깅의 부담도 줄어든다는 것을 느꼈습니다.
    - 스프링이 엔터프라이즈 서비스를 개발하는 데에 있어서 필요한 여러 가지 도구들과 기술들을 지원하고 있다는 것을 느꼈고, 앞으로 더욱 깊이 공부해보고 싶어졌습니다.
- **참고한 자료**
    - 자바 ORM 표준 JPA 프로그래밍 (김영한 저)
