# Spring Framework 개념 정리

## 1. Library → API → Framework

개발 생태계에서 자주 혼용되는 세 가지 개념을 명확히 구분해야 한다.

| 용어 | 의미 |
|---|---|
| **Library** | 개발자가 작성해 놓은 코드 파일 (재사용 가능한 기능 단위) |
| **API** | 여러 라이브러리가 모여있는 패키지 (JAR 파일) |
| **Framework** | API가 굉장히 많이 모여서 덩치가 커진 것, 개발의 기본 흐름과 구조를 제공 |

### 왜 Framework가 필요한가?

개발은 개개인의 능력 차이가 큰 직종이다.
같은 기능을 만들어도 개발자마다 구조와 품질이 천차만별이기 때문에
프로젝트 결과에 큰 차이가 생긴다.

프레임워크는 이 문제를 해결하기 위해 등장했다.

> **프로그램의 기본 흐름과 구조를 미리 정해두고,**
> **모든 팀원이 이 구조 위에서 코드를 추가하는 방식으로 개발하게 만든다.**

---

## 2. Spring Framework

### 등장 배경 - 경량 프레임워크

예전 Java 프레임워크(EJB 등)는 다양한 경우를 처리하기 위해 너무 많은 기능을 포함했다.
하나의 기능을 구현하기 위해 방대한 설정과 구조가 필요했고,
전체를 이해하고 개발하기가 매우 어려웠다.

**Spring Framework**는 이 문제를 해결하기 위해 등장했다.
- 특정 기능을 위주로 간단한 JAR 파일들을 선택해서 조합하는 방식
- 필요한 것만 가져다 쓰는 **경량 프레임워크**

### Spring Framework의 장점

- **빠른 서버 제작**: 프로젝트 전체 구조를 설계할 때 유용하고 개발 속도가 빠르다
- **높은 접착성**: 다른 프레임워크들을 혼용해서 사용할 수 있다 (Mybatis, JPA, Security 등)
- **개발 생산성**: 풍부한 개발 도구 지원

---

## 3. Spring Framework 핵심 특징

### 3-1. POJO 기반의 구성

**POJO(Plain Old Java Object)** : 오래된 방식의 간단한 자바 객체

Spring 이전의 프레임워크들은 특정 클래스를 상속받거나 인터페이스를 구현해야만 했다.
Spring은 일반 Java 객체를 그대로 사용할 수 있도록 설계되었다.

```java
// 특별한 상속 없이 순수한 Java 객체
public class Member {
    private String name;
    private int age;
}
```

> 객체지향 언어의 장점을 그대로 살릴 수 있다는 의미이다.

---

### 3-2. DI(Dependency Injection) - 의존성 주입

#### 의존성(Dependency)이란?

하나의 객체가 다른 객체 없이는 제대로 된 역할을 할 수 없는 상태.

```
A객체가 B객체 없이 동작 불가능 → "A는 B에 의존적이다"
```

직접 A 클래스 안에서 B객체를 생성하면 **결합도가 단단해져** 유연성이 떨어진다.

#### 주입(Injection)이란?

외부에서 내부로 밀어 넣는 것.

```
의존성 (직접 생성):
A ──────────────▶ B
A가 B를 직접 만들고 관리

의존성 주입 (외부에서 주입):
A ◀──────── ? ──────── B
A는 B가 필요하다고 신호, ?가 B를 만들어서 주입
```

Spring에서 `?` 역할을 하는 것이 바로 **ApplicationContext (IoC 컨테이너)** 이다.

#### ApplicationContext의 역할

- `@Component`가 붙은 클래스를 탐색해서 **Bean**으로 등록
- Bean들 사이의 의존관계를 파악
- 필요한 곳에 자동으로 주입 (wiring)

```
개발자가 할 일:
  1. 클래스 만들기 + @Component 붙이기 (Spring에 보고)
  2. 필요한 것은 선언만 하기

Spring이 할 일:
  1. Bean 등록
  2. 의존관계 파악
  3. 적절한 객체 주입
```

> Bean과 Bean 사이의 의존 관계를 처리하는 방식: XML, 어노테이션, Java 방식

---

### 3-3. AOP(Aspect-Oriented Programming) 지원

**AOP(Aspect-Oriented Programming)** : 관점(Aspect)을 지향하는 프로그래밍

> AOP를 잘 쓰는 개발자가 Spring을 잘하는 개발자다.

#### 핵심 아이디어

코드에는 두 종류의 로직이 섞여 있다.

| 구분 | 설명 | 예시 |
|---|---|---|
| **종단 관심사** (메인 로직) | 실제 비즈니스 로직 | 회원가입, 주문처리, 결제 |
| **횡단 관심사** (주변 로직) | 반드시 필요하지만 비즈니스 로직은 아닌 것 | 보안, 로그, 트랜잭션, 예외처리 |

문제는 이 둘이 섞이면 메인 로직에 집중하기가 어렵다는 것이다.

```java
// AOP 없이 - 횡단 관심사가 메인 로직을 침범
public void 회원가입(Member member) {
    log.info("회원가입 시작");         // 로그 (횡단)
    checkSecurity();                   // 보안 (횡단)
    try {
        // 실제 회원가입 로직 (메인)
        memberRepository.save(member);
    } catch (Exception e) {
        // 예외처리 (횡단)
    }
    log.info("회원가입 완료");         // 로그 (횡단)
}
```

AOP를 사용하면 횡단 관심사를 **분리된 모듈**로 관리할 수 있다.

```java
// AOP 적용 후 - 메인 로직에만 집중
public void 회원가입(Member member) {
    memberRepository.save(member); // 핵심 비즈니스 로직만
}
// 로그, 보안, 예외처리는 AOP 모듈이 자동으로 처리
```

**AOP의 장점:**
- 핵심 비즈니스 로직에만 집중하여 코드 개발 가능
- 관심사 변경 시 해당 모듈만 수정 (코드 수정 최소화)
- 유지보수가 수월한 코드 구성

---

### 3-4. Transaction 관리

트랜잭션이란 여러 DB 작업을 하나의 단위로 묶는 것이다.
중간에 오류가 나면 전체를 취소(Rollback), 모두 성공하면 확정(Commit).

```java
// 어노테이션 하나로 트랜잭션 관리
@Transactional(rollbackFor = Exception.class)
public void 회원탈퇴(Long memberId) {
    deleteMemberInfo(memberId);   // 회원 정보 삭제
    deleteOrderHistory(memberId); // 주문 내역 삭제
    deletePaymentHistory(memberId); // 결제 내역 삭제
    // 하나라도 실패 시 전체 롤백
}
```

> DB 작업마다 트랜잭션 코드를 직접 작성하지 않고, 어노테이션이나 XML로 간편하게 관리할 수 있다.
> 금융, 공공기관 시스템에서 Spring이 선호되는 이유 중 하나다.

---

### 3-5. 편리한 MVC 구조

Spring MVC는 FrontController를 직접 만들 필요 없이 프레임워크가 제공한다.
URL 매핑, 요청/응답 처리 등을 어노테이션으로 간편하게 설정할 수 있다.

---

### 3-6. WAS에 종속적이지 않은 개발 환경

**JSP**는 Tomcat 서버가 실행되어야 테스트가 가능하다 → WAS에 종속적

**Spring**은 **JUnit**을 통해 WAS 없이도 기능별 단위 테스트가 가능하다.

```java
// WAS 없이 콘솔에서 빠르게 단위 테스트
@SpringBootTest
public class MemberTest {
    @Test
    public void 회원가입_테스트() {
        // 서버 실행 없이 기능만 검증
    }
}
```

> JUnit: 자바 프로그래밍 언어용 유닛 테스트 프레임워크

**장점:**
- 전체 Application을 실행하지 않아도 기능별 단위 테스트 가능
- 버그를 빠르게 발견하고 개발 시간 단축

---

## 4. 전체 특징 요약

```
Spring Framework
  ├── POJO 기반        → 순수한 Java 객체 그대로 사용 가능
  ├── DI               → 객체간 결합도를 낮추고 유연성을 높임
  ├── AOP              → 횡단 관심사 분리, 비즈니스 로직에 집중
  ├── Transaction      → 어노테이션으로 간편한 DB 작업 관리
  ├── MVC              → FrontController 제공, URL 매핑 간소화
  └── WAS 독립         → JUnit 단위 테스트, 빠른 개발 사이클
```

> Spring Framework는 단순히 편리한 도구가 아니라,
> **좋은 설계를 강제하고 유도하는 구조적 틀**이다.
