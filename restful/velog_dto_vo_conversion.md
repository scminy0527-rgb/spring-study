# RESTful API 설계의 핵심 — DTO ↔ VO 변환 전략과 민감정보 보호

## 들어가며

RESTful API를 처음 설계할 때 가장 흔하게 저지르는 실수 중 하나는 **DB에서 꺼낸 VO(Value Object)를 그대로 화면에 내려주는 것**이다.

언뜻 보면 편리해 보이지만, 이 방식은 심각한 보안 취약점을 내포하고 있다.

---

## 1. 왜 VO를 그대로 응답으로 주면 안 되는가?

아래는 회원 정보를 담는 `MemberVO`다.

```java
@Component
@Data
public class MemberVO implements Serializable {
    private Long id;
    private String memberEmail;
    private String memberPassword; // ← 이게 문제
    private String memberName;
}
```

만약 API 응답으로 `MemberVO`를 그대로 반환하면 어떻게 될까?

```json
// GET /api/members/1 응답
{
  "id": 1,
  "memberEmail": "test123@gmail.com",
  "memberPassword": "test123!@#",  // ← 비밀번호가 그대로 노출된다
  "memberName": "홍길동"
}
```

비밀번호가 화면단(클라이언트)까지 그대로 전달된다. 해시된 비밀번호라도 외부에 노출되어선 안 된다.

**VO는 서버 내부에서 데이터를 운반하기 위한 객체다. 절대로 외부 응답 그 자체가 되어선 안 된다.**

---

## 2. 해결책 — 응답 전용 DTO (Response DTO)

화면으로 내보낼 데이터만 담는 별도의 DTO를 만든다.

```java
@Component
@Data
@Schema(description = "회원 정보")
public class MemberResponseDTO {

    @Schema(description = "회원 번호", required = true, example = "1")
    private Long id;

    @Schema(description = "회원 이메일", required = true, example = "test123@naver.com")
    private String memberEmail;

    @Schema(description = "회원 이름", example = "홍길동")
    private String memberName;

    // memberPassword 필드 자체가 없다 → 응답에 절대 포함될 수 없다
}
```

`memberPassword`가 아예 존재하지 않기 때문에 어떤 실수를 해도 비밀번호가 화면에 나갈 수 없다.
이것이 **VO와 Response DTO를 분리하는 가장 중요한 이유**다.

---

## 3. 정적 팩토리 메서드로 변환하기

`MemberVO → MemberResponseDTO` 변환은 어떻게 할까?

### 일반 생성자 방식의 문제점

```java
// 이렇게 하면 new 가 2번 등장하고, 무슨 목적인지 코드만 봐서는 바로 알기 어렵다
MemberResponseDTO dto = new MemberResponseDTO(memberVO.getId(), memberVO.getMemberEmail(), ...);
```

초기화 생성자를 만들면 기본 생성자가 사라져서 프레임워크(Spring, Jackson 등)가 객체를 생성할 때 에러가 발생할 수 있다는 부작용도 있다.

### 정적 팩토리 메서드 방식

```java
public class MemberResponseDTO {
    // ... 필드들 ...

    public static MemberResponseDTO from(MemberVO memberVO) {
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        memberResponseDTO.setId(memberVO.getId());
        memberResponseDTO.setMemberEmail(memberVO.getMemberEmail());
        memberResponseDTO.setMemberName(memberVO.getMemberName());
        return memberResponseDTO;
    }
}
```

사용하는 쪽은 이렇게 된다.

```java
// Service 레이어에서
MemberVO memberVO = memberDAO.findById(id);
MemberResponseDTO response = MemberResponseDTO.from(memberVO); // new 는 내부에 1번만
```

**`from`이라는 이름만 봐도 "외부에서 받아서 변환한다"는 의도가 명확하다.** 이것이 정적 팩토리 메서드의 가장 큰 장점 중 하나인 **이름으로 의도를 표현하는 능력**이다.

---

## 4. 요청도 마찬가지 — Request DTO 분리

응답뿐 아니라 **요청(Request)도 목적별로 DTO를 분리**해야 한다.

같은 회원 도메인이라도 가입, 로그인, 수정은 서로 필요한 파라미터가 다르다.

### 회원 가입 요청 (MemberJoinRequestDTO)

```java
@Component
@Data
@Schema(description = "회원 가입 시 필요한 입력 값")
public class MemberJoinRequestDTO {

    @Schema(description = "회원 이메일", required = true, example = "test123@gmail.com")
    private String memberEmail;

    @Schema(description = "회원 비밀번호", required = true, example = "test123!@#")
    private String memberPassword;

    @Schema(description = "회원 이름", example = "홍길동")
    private String memberName;
}
```

### 로그인 요청 (MemberLoginRequestDTO)

```java
@Component
@Data
@Schema(description = "로그인 시 필요한 사항")
public class MemberLoginRequestDTO {

    @Schema(description = "회원 이메일", required = true, example = "test123@gmail.com")
    private String memberEmail;

    @Schema(description = "회원 비밀번호", required = true, example = "test123!@#")
    private String memberPassword;

    // id, memberName 은 필요 없으므로 존재하지 않는다
}
```

### 수정 요청 (MemberUpdateRequestDTO)

```java
@Component
@Data
@Schema(description = "회원 정보 업데이트 시 필요 사항")
public class MemberUpdateRequestDTO {

    @Schema(description = "작성자 번호(패스 배리어블로 받을 예정)", example = "1")
    private Long id;

    @Schema(description = "회원 비밀번호", required = true, example = "test123!@#")
    private String memberPassword;

    @Schema(description = "회원 이름", example = "홍길동")
    private String memberName;

    // memberEmail 은 수정 대상이 아니므로 존재하지 않는다
}
```

각 DTO는 **딱 필요한 필드만** 갖는다. 이렇게 하면 클라이언트가 불필요한 값을 전송해도 서버가 받지 않으므로, 의도치 않은 데이터가 처리되는 것을 구조적으로 막을 수 있다.

---

## 5. 오버로딩 — 다형성으로 변환 메서드 통일하기

Request DTO 3종류가 있으면, 각각을 `MemberVO`로 변환하는 메서드가 필요하다.
이때 메서드 이름을 모두 `from`으로 통일하고 **매개변수 타입만 다르게** 한다.
이것이 바로 **메서드 오버로딩(Overloading)** 이다.

```java
@Component
@Data
public class MemberVO implements Serializable {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;

    // 가입 요청 → VO 변환
    public static MemberVO from(MemberJoinRequestDTO memberJoinRequestDTO) {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberName(memberJoinRequestDTO.getMemberName());
        memberVO.setMemberEmail(memberJoinRequestDTO.getMemberEmail());
        memberVO.setMemberPassword(memberJoinRequestDTO.getMemberPassword());
        return memberVO;
    }

    // 로그인 요청 → VO 변환
    public static MemberVO from(MemberLoginRequestDTO memberLoginRequestDTO) {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberEmail(memberLoginRequestDTO.getMemberEmail());
        memberVO.setMemberPassword(memberLoginRequestDTO.getMemberPassword());
        return memberVO;
    }

    // 수정 요청 → VO 변환
    public static MemberVO from(MemberUpdateRequestDTO memberUpdateRequestDTO) {
        MemberVO memberVO = new MemberVO();
        memberVO.setId(memberUpdateRequestDTO.getId());
        memberVO.setMemberName(memberUpdateRequestDTO.getMemberName());
        memberVO.setMemberPassword(memberUpdateRequestDTO.getMemberPassword());
        return memberVO;
    }
}
```

호출하는 쪽은 항상 `MemberVO.from(dto)` 하나의 형태로 사용할 수 있어서 **일관성**이 생긴다.

```java
// 가입
MemberVO vo = MemberVO.from(joinRequestDTO);

// 로그인
MemberVO vo = MemberVO.from(loginRequestDTO);

// 수정
MemberVO vo = MemberVO.from(updateRequestDTO);
```

Java 컴파일러가 매개변수 타입을 보고 어느 `from`을 호출할지 자동으로 결정해준다.

---

## 6. 전체 데이터 흐름 정리

실제 흐름을 도식화하면 다음과 같다.

```
[Client 요청]
      │
      ▼
 XxxRequestDTO         ← 클라이언트가 보낸 데이터만 딱 받음
      │
      │  MemberVO.from(requestDTO)   ← DTO → VO (정적 팩토리, VO에 위치)
      ▼
   MemberVO            ← 서버 내부에서 DB와 통신
      │
      │  MemberResponseDTO.from(vo)  ← VO → DTO (정적 팩토리, DTO에 위치)
      ▼
 MemberResponseDTO      ← 줘야 할 정보만 담아서
      │
      ▼
[Client 응답]
```

변환 책임의 위치를 정리하면:
- **DTO → VO** : `VO.from(DTO)` — VO 클래스 안에 정의
- **VO → DTO** : `DTO.from(VO)` — DTO 클래스 안에 정의

---

## 7. 조인 결과는 별도의 DTO로 — PostDTO

게시글 목록처럼 여러 테이블을 JOIN한 결과는 VO 하나로 표현이 안 된다.
이 경우에는 **조인 결과 전용 DTO**를 만든다.

```java
// SELECT TBP.ID, TBP.POST_TITLE, TBP.POST_CONTENT, TBP.MEMBER_ID, TBP.POST_READ_COUNT,
//        TBM.MEMBER_NAME, TBM.MEMBER_EMAIL
//   FROM TBL_POST TBP JOIN TBL_MEMBER TBM ON TBP.MEMBER_ID = TBM.ID

@Component
@Data
@Schema(description = "게시글 내용")
public class PostDTO implements Serializable {

    @Schema(description = "게시글 번호", required = true, example = "1")
    private Long id;

    @Schema(description = "게시글 제목", required = true, example = "제목000")
    private String postTitle;

    @Schema(description = "게시글 글 내용", required = true, example = "안녕하세요~~....")
    private String postContent;

    @Schema(description = "작성자 번호", required = true, example = "1")
    private Long memberId;

    @Schema(description = "게시글 조회 수", example = "23")
    private Long postReadCount;

    // JOIN으로 가져온 회원 정보 (memberPassword 는 없다)
    @Schema(description = "게시글 작성자 이름", example = "홍길동")
    private String memberName;

    @Schema(description = "게시글 작성자 이메일", required = true, example = "test123@gmail.com")
    private String memberEmail;
}
```

MyBatis의 `resultType`을 이 DTO로 지정하면 JOIN 쿼리 결과가 그대로 매핑된다.
`PostVO`에는 없는 `memberName`, `memberEmail`도 받을 수 있고, 민감 정보인 `memberPassword`는 처음부터 필드가 없으므로 절대 노출될 수 없다.

---

## 8. Swagger 어노테이션 — 백엔드 개발자의 책임

프론트엔드 개발자, QA, 팀원이 API를 이해하고 테스트할 수 있도록 Swagger 문서를 정확하게 작성하는 것은 **백엔드 개발자의 필수 업무**다.

```java
@Schema(description = "클래스 단위 설명")
public class SomeRequestDTO {

    @Schema(
        description = "필드가 어떤 값인지 설명",
        required = true,          // 필수 여부
        example = "실제 예시 값"   // Swagger UI에서 Try it out 시 자동 입력
    )
    private String someField;
}
```

`example`을 실제 유효한 값으로 채워두면 Swagger UI에서 테스트할 때 직접 입력할 필요가 없어서 협업 효율이 크게 올라간다.

---

## 9. 놓치기 쉬운 개념들

### 9-1. 입력값 유효성 검증 (`@Valid` + Bean Validation)

현재 코드에서 빠져있는 부분이다. Request DTO에 제약 조건 어노테이션을 추가해야 한다.

```java
public class MemberJoinRequestDTO {

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String memberEmail;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String memberPassword;

    @NotBlank(message = "이름은 필수입니다")
    private String memberName;
}
```

컨트롤러에서는 `@Valid`를 붙여준다.

```java
@PostMapping("/join")
public ResponseEntity<Void> join(@Valid @RequestBody MemberJoinRequestDTO memberJoinRequestDTO) {
    // ...
}
```

유효성 검증을 DTO 레벨에서 선언적으로 처리하면, Service 레이어를 비즈니스 로직에만 집중시킬 수 있다.

### 9-2. `@JsonIgnore` 는 미봉책이다

간혹 `MemberVO`에서 비밀번호를 숨기기 위해 이렇게 처리하는 경우가 있다.

```java
public class MemberVO {
    @JsonIgnore // JSON 직렬화에서 제외
    private String memberPassword;
}
```

이 방법은 동작은 하지만 **구조적 해결책이 아니다.** `MemberVO`를 응답으로 계속 사용하는 구조 자체가 문제이며, 나중에 `@JsonIgnore`를 실수로 지웠을 때 비밀번호가 그대로 노출된다. DTO를 분리하는 것이 올바른 설계다.

### 9-3. 정적 팩토리 메서드의 이름 컨벤션

`from`은 가장 일반적인 이름이지만, 용도에 따라 관례적으로 다른 이름도 사용한다.

| 이름 | 용도 |
|------|------|
| `from(T t)` | 다른 타입의 단일 객체에서 변환할 때 |
| `of(a, b, c)` | 여러 개의 값을 받아서 생성할 때 |
| `valueOf(T t)` | 타입 변환 (기본 타입 ↔ 박싱 타입 등) |
| `create()` | 매번 새 인스턴스 생성 강조 |

`MemberResponseDTO.from(memberVO)` 처럼 `from`을 사용하는 것은 **Effective Java (조슈아 블로크)**에서 권장하는 컨벤션이기도 하다.

### 9-4. VO 변환 자동화 — MapStruct

팀 규모가 커지거나 필드가 많아지면, 정적 팩토리 메서드의 `setXxx` 코드가 길어지고 실수하기 쉬워진다.
**MapStruct** 라이브러리를 사용하면 컴파일 타임에 변환 코드를 자동 생성해준다.

```java
// MapStruct 인터페이스만 선언하면 구현체를 자동 생성
@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberResponseDTO toResponseDTO(MemberVO memberVO);
    MemberVO from(MemberJoinRequestDTO dto);
}
```

지금처럼 학습 단계에서는 직접 `from`을 작성해서 흐름을 이해하는 것이 중요하다. 원리를 이해한 뒤에 MapStruct를 도입하면 훨씬 자연스럽게 사용할 수 있다.

---

## 마치며

RESTful API에서 DTO와 VO를 분리하는 핵심 원칙을 정리하면 다음과 같다.

| 원칙 | 이유 |
|------|------|
| **VO는 절대 응답으로 직접 반환하지 않는다** | 민감정보(비밀번호 등) 노출 방지 |
| **응답 전용 ResponseDTO를 별도로 만든다** | 노출할 필드를 구조적으로 제한 |
| **요청마다 별도의 RequestDTO를 만든다** | 불필요한 파라미터의 유입을 구조적으로 차단 |
| **변환은 정적 팩토리 메서드 `from`으로 통일한다** | new 1회, 의도 명확, 오버로딩으로 일관성 확보 |
| **Swagger `@Schema`를 성실히 작성한다** | 협업의 기본 — API는 혼자 쓰지 않는다 |
| **`@Valid`로 입력값 검증을 선언적으로 처리한다** | Service를 비즈니스 로직에만 집중시킨다 |

처음엔 DTO를 이렇게까지 분리하는 게 번거롭게 느껴질 수 있다. 하지만 실무에서 보안 사고와 디버깅 비용을 경험하고 나면, 이 구조가 얼마나 중요한지 체감하게 된다. **처음부터 습관을 잘 들이는 것이 최선이다.**
