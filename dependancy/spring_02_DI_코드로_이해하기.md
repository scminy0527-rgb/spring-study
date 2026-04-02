# Spring DI - 코드로 이해하기

> 이전 글에서 Spring Framework의 개념을 정리했다.
> 이번 글에서는 실제 코드를 보면서 DI가 어떻게 동작하는지 이해해보자.

---

## 1. @Component - Spring에 클래스 보고하기

Spring Boot는 애플리케이션이 실행될 때 `@Component`가 붙은 클래스를 탐색해서
**IoC 컨테이너(ApplicationContext)** 에 객체(Bean)로 등록한다.

```java
@Component
@Data
public class Computer {
}
```

```java
@Component
@Data
public class Member {
    private String name;
}
```

`@Component`를 붙이는 것은 Spring에게 이렇게 말하는 것과 같다.

> *"이 클래스, 내가 만들었으니까 당신이 관리해줘."*

Spring은 등록된 Bean들의 의존관계를 파악하고, 필요한 곳에 자동으로 주입한다.
개발자는 `new`로 직접 객체를 생성하는 대신, **선언만 하면** Spring이 알아서 넣어준다.

---

## 2. 필드 주입 vs 생성자 주입

DI 방식에는 크게 두 가지가 있다. 이 둘의 차이를 이해하는 것이 핵심이다.

### 필드 주입 (지양)

```java
@Component
public class Coding {
    @Autowired
    private Computer computer; // 객체 생성 후 나중에 주입
}
```

**내부 동작 순서:**

```
1단계: 기본 생성자로 객체 생성
         └─ computer = null  (기본값, 아직 아무것도 없음)

2단계: 객체 생성 완료 후 @Autowired가 값 주입
         └─ computer = 0x1a2b3c  (null → 해시값으로 재할당)
```

**문제점:**
- `null → 실제 주소값`으로 **재할당**이 발생한다
- 주입된 값이 이후에도 바뀔 수 있어 **불변성 보장 불가**
- **순환참조** 발생 여부를 서버 실행 전까지 알 수 없다
  - 예: `Coding → Computer → Coding → Computer → ...` 무한 참조

### 생성자 주입 (권장)

```java
// Coding.java - 실제 프로젝트 코드
@Component
@Data
@RequiredArgsConstructor
public class Coding {
    final private Computer computer; // 생성과 동시에 초기화, 이후 불변

    private String type;
    private String content;
}
```

**내부 동작 순서:**

```
객체 생성 시작
  └─ 생성자(Computer computer) 호출
       └─ computer = 0x1a2b3c  (처음부터 실제 주소값으로 할당)
                                  이 순간 final 확정 → 이후 변경 불가
```

`null`을 거치지 않고 **처음부터 실제 값으로 시작**하기 때문에 `final`과 완벽히 호환된다.

---

## 3. final과 생성자의 관계 - 불변성 보장

`final` 키워드는 **객체 생성 시점에 딱 한 번만 초기화** 가능하다.
이것이 필드 주입과 생성자 주입의 결정적 차이다.

| 방식 | 흐름 | final 사용 가능? |
|---|---|---|
| 필드 주입 | 생성 → 나중에 주입 (재할당) | 불가 |
| 생성자 주입 | 생성과 동시에 주입 (최초 할당) | 가능 |

**비유로 이해하기:**

요리를 하는데 칼이 필요하다고 가정하자.
국을 저으면서 국자를 쓰고 있는데, 도중에 갑자기 나무막대기로 바뀌면 안 된다.
`final`은 이런 상황을 막아주는 장치이고, 생성자 주입은 처음부터 국자를 손에 쥐고 시작하는 방식이다.

```java
// Food.java - 요리에는 도구(칼)가 필요하므로 주입
@Component
@Data
public class Food {
    private final Knife knife; // 한 번 주입되면 변경 불가
}
```

```java
// Login.java - Member 없이는 로그인이 불가능하므로 주입
@Component
@Data
public class Login {
    private final Member member;
}
```

> `final`을 쓰고 싶다면 **생성자 주입이 논리적으로 유일한 선택**이다.

---

## 4. Lombok으로 생성자 자동 생성

매번 생성자를 직접 작성하는 건 번거롭다. Lombok 어노테이션으로 자동 생성한다.

| 어노테이션 | 생성자 생성 범위 |
|---|---|
| `@Data` | `@RequiredArgsConstructor` 포함 (final 필드만) |
| `@RequiredArgsConstructor` | `final` 또는 `@NonNull` 필드만 대상 |
| `@AllArgsConstructor` | 모든 필드 대상 |
| `@NoArgsConstructor` | 기본 생성자 (인자 없음) |

> `@Data`에는 이미 `@RequiredArgsConstructor`가 포함되어 있다.
> DI 목적이라면 `@RequiredArgsConstructor`가 의도를 더 명확히 드러낸다.

---

## 5. 테스트에서는 필드 주입 허용

테스트 클래스는 Spring이 관리하는 Bean이 아니기 때문에 생성자 주입 구조를 만들기 어렵다.
따라서 테스트에서는 예외적으로 `@Autowired` 필드 주입을 사용한다.

```java
// DiTest.java
@SpringBootTest
@Slf4j
public class DiTest {

    // 테스트에서는 어쩔 수 없이 필드 주입 사용
    @Autowired
    private Coding coding;

    @Autowired
    private Food food;

    @Autowired
    private Login login;

    @Test
    public void codingTest() {
        log.info("coding : {}", coding.getComputer()); // 해시값 출력
    }

    @Test
    public void foodTest() {
        log.info("food {}", food.getKnife()); // 주입된 Knife 객체 확인
    }

    @Test
    public void loginTest() {
        log.info("멤버 객체 주소값: {}", login.getMember());
    }
}
```

- `@Autowired` **없으면** → `null` 출력 (주입 안 됨)
- `@Autowired` **있으면** → 실제 객체의 해시값 출력 (주입 성공)

이를 통해 Spring이 실제로 의존성을 주입해줬는지 확인할 수 있다.

---

## 6. 인터페이스 + 업캐스팅으로 결합도 낮추기

DI의 진정한 힘은 **인터페이스와 결합**될 때 발휘된다.

### 인터페이스 구현 = 타입 획득

```java
// qualifier/Computer.java - 인터페이스 (타입 정의)
public interface Computer {
    public int getScreenSize();
}
```

```java
// Desktop.java - 구체 구현체
@Component
@Data
public class Desktop implements Computer {
    @Override
    public int getScreenSize() {
        return 2560;
    }
}
```

```java
// Laptop.java - 또 다른 구체 구현체
@Component
@Data
public class Laptop implements Computer {
    @Override
    public int getScreenSize() {
        return 1920;
    }
}
```

`Desktop`은 이제 **두 가지 타입**을 가진다:
- `Desktop` 타입 (자기 자신)
- `Computer` 타입 (인터페이스)

인터페이스를 구현하면 **인터페이스의 타입도 함께 획득**한다. 이 덕분에 업캐스팅이 가능하다.

### 업캐스팅 / 다운캐스팅

```java
// 업캐스팅 - Computer 타입으로 바라봄 (자동)
Computer computer = new Desktop();
computer.getScreenSize(); // 인터페이스에 정의된 것만 사용 가능

// 다운캐스팅 - 다시 구체 타입으로 바라봄 (명시적)
Desktop desktop = (Desktop) computer;
desktop.getScreenSize(); // Desktop 고유 기능까지 사용 가능
```

인터페이스는 단순히 메서드를 강제하는 것이 아니라, **타입 체계를 설계하는 도구**다.

---

## 7. @Qualifier - 같은 타입의 빈이 여러 개일 때

`Computer` 인터페이스를 구현한 빈이 `Desktop`, `Laptop` 두 개라면
Spring은 어떤 것을 주입해야 할지 모른다. 이때 `@Qualifier`로 명시적으로 지정한다.

```java
// ComputerTest.java
@SpringBootTest
@Slf4j
public class ComputerTest {

    @Autowired
    @Qualifier("desktop") // Desktop 빈을 명시적으로 지정
    private Computer computer;

    @Test
    public void computerTest() {
        log.info("computer : {}", computer);
        log.info("screen size : {}", computer.getScreenSize()); // 2560
    }
}
```

`Restaurant` 인터페이스도 마찬가지다.

```java
// RestaurantTest.java
@SpringBootTest
@Slf4j
public class RestaurantTest {

    @Autowired @Qualifier("vips")
    private Restaurant vips;

    @Autowired @Qualifier("outback")
    private Restaurant outback;

    @Test
    public void restaurantTest() {
        log.info("vips salad bar : {}", vips.isSaladBarAvailable());      // false
        log.info("outback salad bar : {}", outback.isSaladBarAvailable()); // true
        log.info("vips stake price : {}", vips.stakePrice());
        log.info("outback stake price : {}", outback.stakePrice());        // 100000
    }
}
```

`Outback`과 `Vips` 모두 `Restaurant` 타입으로 업캐스팅된 상태로 주입되지만,
실제 동작은 각 구현체의 메서드가 실행된다 **(다형성)**.

---

## 8. 결합도(Coupling) - DI를 써야 하는 진짜 이유

### 결합도가 높은 방식 (new 직접 생성)

```java
public class Cook {
    private KitchenKnife kitchenKnife = new KitchenKnife();
    private BreadKnife breadKnife = new BreadKnife();
    private ChefKnife chefKnife = new ChefKnife();
    // Cook이 모든 칼의 종류를 직접 알고 책임져야 함
    // 칼이 추가되거나 바뀌면 Cook 코드도 수정해야 함
}
```

### 결합도를 낮추는 방식 (인터페이스 + DI)

```java
public interface Knife { } // 칼이라는 개념(타입)만 정의

@Component
@Data
public class Food {
    private final Knife knife; // 어떤 칼인지 몰라도 됨, Spring이 주입
}
```

**구조 비교:**

```
높은 결합도:
  Cook ──new──▶ KitchenKnife
       ──new──▶ BreadKnife
       ──new──▶ ChefKnife
  (Cook이 모든 구체 클래스에 의존)

낮은 결합도:
  Food ──의존──▶ Knife (인터페이스)
                      ↑
                 Spring이 주입
           (KitchenKnife or BreadKnife...)
  (Food는 Knife 타입만 알면 됨)
```

### 핵심 비교

> `new`로 직접 생성 = 클래스가 구체 구현체의 모든 것을 책임짐 → **강한 결합**
> DI로 주입 = 인터페이스 타입만 선언, 나머지는 Spring이 책임 → **느슨한 결합**

**느슨한 결합의 이점:**
- 코드 변경 없이 다른 구현체로 교체 가능
- 테스트 시 Mock 객체를 쉽게 주입 가능
- 유지보수성과 확장성이 높아짐

---

## 전체 흐름 정리

```
@Component
  └─ Spring 컨테이너에 Bean 등록 (보고)

final + @RequiredArgsConstructor
  └─ 생성자 주입으로 불변성 보장
       └─ 객체 생성과 동시에 주입 → final 확정 → 이후 변경 불가

인터페이스 implements
  └─ 타입 획득 → 업캐스팅 가능
       └─ 구체 클래스를 몰라도 됨 → 결합도 낮아짐

@Qualifier
  └─ 같은 타입 Bean이 여러 개일 때 명시적 지정

테스트 @Autowired
  └─ 테스트 클래스는 Bean이 아니므로 필드 주입 예외적 허용
```

> **DI의 본질:**
> 필요한 것을 직접 만들지 않고 주입받아 사용함으로써,
> 클래스 간 결합도를 낮추고 **변경에 강한 코드**를 설계하는 것.
