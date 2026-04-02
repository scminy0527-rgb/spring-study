# Spring Boot DI (의존성 주입) - 결합도를 낮추는 바람직한 코드 설계

## 1. Spring Boot에 클래스 등록하기 - @Component

Spring Boot는 애플리케이션이 실행될 때 `@Component` 어노테이션이 붙은 클래스를 자동으로 탐색해서 **IoC 컨테이너**에 객체(Bean)로 등록한다.

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

`@Component`를 붙이는 것은 Spring에게 *"이 클래스, 내가 만들었으니까 당신이 관리해줘"* 라고 보고하는 것과 같다.

Spring은 이렇게 등록된 빈들의 의존관계를 파악하고, 필요한 곳에 자동으로 주입해준다.

---

## 2. 필드 주입이 아닌 생성자 주입을 써야 하는 이유

DI를 사용할 때 주입 방식에는 크게 두 가지가 있다.

### 필드 주입 (지양)

```java
@Component
public class Coding {
    @Autowired
    private Computer computer; // 객체 생성 후 나중에 주입 (재할당)
}
```

**필드 주입의 내부 동작 순서:**
```
1단계: 기본 생성자로 객체 생성
         └─ computer = null  (기본값)

2단계: 객체 생성 완료 후 @Autowired가 값 주입
         └─ computer = 0x1a2b3c  (null → 해시값, 재할당)
```

이 방식의 문제점:
- `null → 실제 주소값` 으로 **재할당**이 발생한다
- 주입된 값이 나중에 바뀔 수 있어 **불변성을 보장할 수 없다**
- **순환참조** 발생 여부를 서버 실행 전까지 알 수 없다
  - 예: `Coding → Computer → Coding → ...` 무한 참조

### 생성자 주입 (권장)

```java
@Component
@Data
@RequiredArgsConstructor
public class Coding {
    final private Computer computer; // 생성과 동시에 초기화, 이후 불변
}
```

**생성자 주입의 내부 동작 순서:**
```
객체 생성 시작
  └─ 생성자(Computer computer) 호출
       └─ computer = 0x1a2b3c  (처음부터 실제 주소값으로 할당)
                                  이 순간 final 확정 → 이후 변경 불가
```

`null`을 거치지 않고 **처음부터 실제 값으로 시작**하기 때문에 `final`과 완벽히 호환된다.

---

## 3. final과 생성자의 관계 - 불변성 보장

`final` 키워드는 **객체 생성 시점에 딱 한 번만 초기화**가 가능하다.

| 방식 | 흐름 | final 사용 가능? |
|---|---|---|
| 필드 주입 | 생성 → 나중에 주입 (재할당) | 불가 |
| 생성자 주입 | 생성과 동시에 주입 (최초 할당) | 가능 |

`final`을 쓰고 싶다면 **생성자 주입이 논리적으로 유일한 선택**이다.

### Lombok으로 생성자 자동 생성

매번 생성자를 직접 작성하는 건 번거롭기 때문에 Lombok 어노테이션을 활용한다.

| 어노테이션 | 생성자 생성 범위 |
|---|---|
| `@Data` | `@RequiredArgsConstructor` 포함 (final 필드만) |
| `@RequiredArgsConstructor` | `final` 또는 `@NonNull` 필드만 대상 |
| `@AllArgsConstructor` | 모든 필드 대상 |

> `@Data`에는 이미 `@RequiredArgsConstructor`가 포함되어 있다.
> DI 목적이라면 `@RequiredArgsConstructor`가 더 명확하다.

실제 코드에서 `Coding`, `Food`, `Login` 모두 같은 패턴을 사용한다.

```java
// Login.java - Member 없이는 로그인이 불가능하므로 주입
@Component
@Data
public class Login {
    private final Member member;
}
```

```java
// Food.java - 요리에는 도구(칼)가 필요하므로 주입
@Component
@Data
public class Food {
    private final Knife knife;
}
```

---

## 4. 테스트에서는 필드 주입 허용

테스트 클래스는 Spring이 관리하는 빈이 아니기 때문에 생성자 주입 구조를 만들기 어렵다.
따라서 테스트에서는 예외적으로 `@Autowired` 필드 주입을 사용한다.

```java
@SpringBootTest
@Slf4j
public class DiTest {

    // 테스트에서는 어쩔 수 없이 필드 주입 사용
    @Autowired
    private Coding coding;

    @Autowired
    private Food food;

    @Test
    public void codingTest() {
        log.info("coding : {}", coding.getComputer()); // 해시값 출력 확인
    }

    @Test
    public void foodTest() {
        log.info("food {}", food.getKnife()); // 주입된 Knife 객체 확인
    }
}
```

- `@Autowired`가 없으면 → `null` 출력
- `@Autowired`가 있으면 → 실제 객체의 해시값 출력

이를 통해 Spring이 실제로 의존성을 주입해줬는지 확인할 수 있다.

---

## 5. 인터페이스 + 업캐스팅으로 결합도 낮추기

DI의 진정한 힘은 **인터페이스와 결합**될 때 나온다.

### 인터페이스 구현 = 타입 획득

```java
// Computer.java - 인터페이스 (타입 정의)
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

`Desktop`은 이제 두 가지 타입을 가진다:
- `Desktop` 타입 (자기 자신)
- `Computer` 타입 (인터페이스)

이 덕분에 **업캐스팅**이 가능하다.

```java
// 업캐스팅 - Computer 타입으로 바라봄 (자동)
Computer computer = new Desktop();
computer.getScreenSize(); // 인터페이스에 정의된 것만 사용 가능

// 다운캐스팅 - 다시 구체 타입으로 바라봄 (명시적)
Desktop desktop = (Desktop) computer;
```

---

## 6. @Qualifier - 같은 타입의 빈이 여러 개일 때

`Computer` 인터페이스를 구현한 빈이 `Desktop`, `Laptop` 두 개이면 Spring은 어떤 것을 주입해야 할지 모른다.
이때 `@Qualifier`로 명시적으로 지정한다.

```java
@SpringBootTest
@Slf4j
public class ComputerTest {

    @Autowired
    @Qualifier("desktop") // Desktop 빈을 명시적으로 지정
    private Computer computer;

    @Test
    public void computerTest() {
        log.info("computer : {}", computer);
        log.info("computer screen size : {}", computer.getScreenSize()); // 2560
    }
}
```

`Restaurant` 인터페이스도 마찬가지다.

```java
@SpringBootTest
@Slf4j
public class RestaurantTest {

    @Autowired @Qualifier("vips")
    private Restaurant vips;

    @Autowired @Qualifier("outback")
    private Restaurant outback;

    @Test
    public void restaurantTest() {
        log.info("vips salad bar : {}", vips.isSaladBarAvailable());    // false
        log.info("outback salad bar : {}", outback.isSaladBarAvailable()); // true
        log.info("vips stake price : {}", vips.stakePrice());
        log.info("outback stake price : {}", outback.stakePrice());     // 100000
    }
}
```

---

## 7. 결합도(Coupling) - DI를 써야 하는 진짜 이유

### 결합도가 높은 방식 (new 직접 생성)

```java
public class Cook {
    private KitchenKnife kitchenKnife = new KitchenKnife();
    private BreadKnife breadKnife = new BreadKnife();
    private ChefKnife chefKnife = new ChefKnife();
    // Cook이 칼의 모든 종류를 직접 알고 있어야 함
    // 칼이 바뀌면 Cook 코드도 수정해야 함
}
```

### 결합도를 낮추는 방식 (인터페이스 + DI)

```java
public interface Knife { } // 칼이라는 개념만 정의

@Component
@Data
public class Food {
    private final Knife knife; // 구체적인 칼을 몰라도 됨, Spring이 주입
}
```

```
높은 결합도:  Cook ──직접 생성──▶ KitchenKnife
                   ──직접 생성──▶ BreadKnife
                   ──직접 생성──▶ ChefKnife

낮은 결합도:  Food ──의존──▶ Knife (인터페이스)
                                  ↑
                             Spring이 주입
                        (KitchenKnife or BreadKnife...)
```

### 핵심 비교

> `new`로 직접 생성 = 클래스가 구체 구현체의 모든 것을 책임짐 → **강한 결합**
> DI로 주입 = 인터페이스 타입만 선언, 나머지는 Spring이 책임 → **느슨한 결합**

느슨한 결합 덕분에:
- 코드 변경 없이 다른 구현체로 교체 가능
- 테스트 시 Mock 객체를 쉽게 주입 가능
- 유지보수성과 확장성이 높아짐

---

## 정리

```
@Component          → Spring 컨테이너에 빈 등록 (보고)
final + 생성자 주입  → 불변성 보장 (한 번 주입되면 변경 불가)
@RequiredArgsConstructor → final 필드 생성자 자동 생성 (Lombok)
인터페이스 구현      → 타입 획득 → 업캐스팅 가능
업캐스팅 + DI       → 구체 클래스를 몰라도 됨 → 결합도 낮아짐
@Qualifier          → 같은 타입의 빈이 여러 개일 때 명시적 지정
테스트의 @Autowired → 필드 주입 예외적 허용 (테스트 클래스는 빈이 아니므로)
```

> **DI의 본질**: 필요한 것을 직접 만들지 않고 주입받아 사용함으로써,
> 클래스 간 결합도를 낮추고 변경에 강한 코드를 설계하는 것.
