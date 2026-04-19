# RESTful API 응답 설계 — ResponseEntity, 제네릭 공통 응답, 전역 예외처리

## 들어가며

지난 글에서는 DTO ↔ VO 변환 전략을 다뤘다.
이번에는 **"요청이 오면 반드시 응답을 줘야 한다"** 는 HTTP 표준에서 출발해서, 공통 응답 형태를 설계하고 예외도 일관된 방식으로 처리하는 방법을 살펴본다.

---

## 1. HTTP 표준 — 요청에는 반드시 응답이 있어야 한다

HTTP 프로토콜은 클라이언트가 요청(Request)을 보내면, 서버는 **반드시 응답(Response)을 반환해야 한다** 는 것을 표준으로 정하고 있다.

그런데 기존 방식에서는 이렇게 처리하는 경우가 많았다.

```java
// 반환형이 void → 응답이 없다
@PostMapping("/join")
public void join(@RequestBody MemberJoinRequestDTO dto) {
    memberService.join(dto);
}

@DeleteMapping("/{id}")
public void delete(@PathVariable Long id) {
    memberService.withdrawMember(id);
}
```

조회(GET) 처럼 반환할 데이터가 있으면 자연스럽게 응답이 갔지만,
등록/수정/삭제처럼 **반환할 데이터가 없는 경우에는 아무 응답도 없었다.**

이는 HTTP 표준에 어긋난다. 최소한 **"성공했다" 또는 "실패했다"** 는 결과는 반환해야 한다.

---

## 2. ResponseEntity — 응답을 감싸는 큰 그릇

Spring이 제공하는 `ResponseEntity<T>` 는 HTTP 응답 전체를 표현하는 객체다.

```
ResponseEntity
├── 상태 코드 (HttpStatus) → 200, 201, 400, 401, 404, 409...
├── 헤더 (Header)
└── 바디 (Body) → 실제 응답 데이터
```

사용 예시:

```java
// 조회 성공
return ResponseEntity
        .status(HttpStatus.OK)           // 200
        .body(someData);

// 생성 성공
return ResponseEntity
        .status(HttpStatus.CREATED)      // 201
        .body(someData);

// 삭제 성공 (반환 데이터 없음)
return ResponseEntity
        .status(HttpStatus.NO_CONTENT)   // 204
        .build();
```

이제 **상태 코드를 응답에 포함** 할 수 있다. 프론트엔드는 상태 코드만 봐도 성공/실패를 즉시 알 수 있다.

### 주요 HTTP 상태 코드 정리

| 코드 | 의미 | 주로 사용하는 경우 |
|------|------|-------------------|
| 200 OK | 성공 | GET, PUT (수정), POST (로그인) |
| 201 Created | 생성됨 | POST (데이터 생성) |
| 204 No Content | 성공, 반환할 내용 없음 | DELETE |
| 400 Bad Request | 잘못된 요청 | 파라미터 오류 |
| 401 Unauthorized | 인증 실패 | 로그인 실패, 토큰 없음 |
| 403 Forbidden | 권한 없음 | 권한 부족 |
| 404 Not Found | 리소스 없음 | 존재하지 않는 데이터 조회 |
| 409 Conflict | 충돌 | 이메일 중복 가입 |
| 500 Internal Server Error | 서버 오류 | 처리되지 않은 예외 |

---

## 3. 문제 — API마다 응답 형태가 제각각이다

`ResponseEntity`로 응답을 감쌌지만 아직 문제가 있다.

```java
// 조회: List<MemberResponseDTO> 반환
ResponseEntity<List<MemberResponseDTO>>

// 단건 조회: MemberResponseDTO 반환
ResponseEntity<MemberResponseDTO>

// 등록: 반환 데이터 없음
ResponseEntity<Void>
```

API마다 body의 타입이 다르다. 프론트엔드 개발자 입장에서는 **API별로 각각 다른 응답 구조를 파악하고 처리 코드를 작성**해야 한다.

프론트엔드 코드가 이렇게 된다.

```javascript
// GET /api/members    → data가 배열
// GET /api/members/1  → data가 객체
// POST /api/members   → data가 없음
// 매번 다른 형태로 처리해야 한다...
```

**응답의 envelope(봉투)를 통일**해야 한다.

---

## 4. ApiResponseDTO<T> — 제네릭으로 만드는 공통 응답 래퍼

모든 API 응답을 하나의 공통된 형태로 감싼다.

```java
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "응답 객체 DTO")
public class ApiResponseDTO<T> {

    @Schema(description = "응답 메세지", example = "회원 조회 성공", required = true)
    private String message;

    @Schema(description = "응답 데이터")
    private T data;  // ← 제네릭: 어떤 타입도 담을 수 있다

    // 데이터가 없을 때 (등록, 수정, 삭제 결과)
    public static <T> ApiResponseDTO<T> of(String message) {
        return new ApiResponseDTO<>(message, null);
    }

    // 데이터가 있을 때 (조회 결과)
    public static <T> ApiResponseDTO<T> of(String message, T data) {
        return new ApiResponseDTO<>(message, data);
    }
}
```

### 제네릭(Generic)이 핵심이다

`T`라는 타입 파라미터를 사용하면, 클래스를 **한 번만 정의**해도 다양한 타입에 대응할 수 있다.

제네릭 없이 만든다면?

```java
// 타입별로 클래스를 각각 만들어야 한다 → 유지보수 지옥
public class MemberListApiResponse {
    private String message;
    private List<MemberResponseDTO> data;
}
public class MemberApiResponse {
    private String message;
    private MemberResponseDTO data;
}
public class PostListApiResponse {
    private String message;
    private List<PostDTO> data;
}
// ... 계속 늘어난다
```

제네릭을 쓰면 `ApiResponseDTO<T>` 하나로 모든 경우를 처리한다.

```java
ApiResponseDTO<List<MemberResponseDTO>>  // 회원 목록
ApiResponseDTO<MemberResponseDTO>        // 회원 단건
ApiResponseDTO<List<PostDTO>>            // 게시글 목록
ApiResponseDTO<?>                        // 데이터 없음 (null)
```

### 오버로딩으로 사용성 높이기

`of` 메서드를 두 가지로 오버로딩했다.

```java
// 등록, 수정, 삭제 → 결과 메세지만
ApiResponseDTO.of("회원가입 성공하였습니다.")

// 조회 → 메세지 + 데이터
ApiResponseDTO.of("멤버 정보 불러오기 성공", members)
```

호출부에서 필요에 따라 골라 쓸 수 있어서 편리하다.
메서드 이름이 `of`인 이유는 "여러 값을 모아 객체를 생성한다" 는 정적 팩토리 컨벤션을 따른 것이다.

---

## 5. 컨트롤러에서의 실제 사용

이제 컨트롤러의 반환형은 모두 `ResponseEntity<ApiResponseDTO>` 로 통일된다.

```java
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberAPI {

    // 목록 조회 — 200 OK + 데이터 포함
    @GetMapping("")
    public ResponseEntity<ApiResponseDTO> getAllMembers() {
        List<MemberResponseDTO> members = memberService.getMembers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("멤버 정보 불러오기 성공", members));
    }

    // 회원가입 — 201 Created + 메세지만
    @PostMapping("/join")
    public ResponseEntity<ApiResponseDTO> join(@RequestBody MemberJoinRequestDTO dto) {
        memberService.join(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.of("회원가입 성공하였습니다."));
    }

    // 수정 — 200 OK + 메세지만
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> modifyMemberInfo(
            @PathVariable Long id,
            @RequestBody MemberUpdateRequestDTO dto) {
        dto.setId(id);
        memberService.modifyMemberInfo(dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.of("회원 정보 수정 성공"));
    }

    // 탈퇴 — 204 No Content + 메세지만
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteMember(@PathVariable Long id) {
        memberService.withdrawMember(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponseDTO.of("회원 탈퇴 성공하였습니다."));
    }
}
```

프론트엔드는 이제 어떤 API를 호출해도 동일한 구조로 응답을 받는다.

```json
// 조회 응답
{
  "message": "멤버 정보 불러오기 성공",
  "data": [ { "id": 1, "memberEmail": "...", "memberName": "..." } ]
}

// 등록/수정/삭제 응답
{
  "message": "회원가입 성공하였습니다.",
  "data": null
}
```

`message`로 결과를 읽고, `data`가 있으면 데이터를 처리하는 **하나의 패턴**으로 통일된다.

---

## 6. 예외가 발생하면? — 500이 그대로 가면 안 된다

아직 해결하지 못한 문제가 있다. 서비스에서 예외가 발생했을 때 처리되지 않으면 Spring이 500 에러를 그대로 클라이언트에 반환한다.

```json
// 처리되지 않은 예외 시 Spring 기본 응답 — 이게 화면단으로 가면 안 된다
{
  "timestamp": "...",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/members/join"
}
```

프론트엔드 입장에서는 이 응답으로 무슨 에러인지 알 수 없다. **서버 개발자가 의도한 메세지와 상태 코드를 클라이언트로 전달**해야 한다.

---

## 7. 커스텀 예외 클래스 설계

먼저, 도메인별로 의미 있는 예외 클래스를 만든다.

### 베이스 예외 — HttpStatus를 품은 RuntimeException

```java
@Data
public class MemberException extends RuntimeException {

    // 예외의 성격(종류)을 상태 코드로 표현
    private HttpStatus status;

    public MemberException() { ; }

    public MemberException(String message, HttpStatus status) {
        super(message);      // RuntimeException에 메세지 전달
        this.status = status;
    }
}
```

`RuntimeException`을 상속하는 이유는 **Unchecked Exception** 으로 만들기 위해서다.
Checked Exception(`Exception` 상속)은 `throws` 선언이나 `try-catch`를 강제해서 코드가 복잡해진다.
서비스 레이어에서 자유롭게 던질 수 있도록 `RuntimeException`을 상속한다.

### 하위 예외 — 구체적인 상황에 맞는 예외

```java
// 로그인 실패는 항상 400 BAD_REQUEST → 상태 코드를 고정
public class MemberLoginException extends MemberException {
    public MemberLoginException() { ; }
    public MemberLoginException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
```

```java
public class PostNotFoundException extends PostException {
    public PostNotFoundException() { ; }
    public PostNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
```

이처럼 **예외 계층 구조**를 만들면:

```
RuntimeException
├── MemberException          (message + HttpStatus 포함)
│   ├── MemberLoginException (HttpStatus.BAD_REQUEST 고정)
│   └── DuplicateEmailException
└── PostException
    └── PostNotFoundException
```

- 공통 속성(`HttpStatus`, `message`)은 부모에 정의
- 각 상황에 맞는 구체적인 예외는 자식 클래스로 분리
- 이것이 **객체지향의 상속** 을 예외 설계에 적용한 것이다

---

## 8. Service에서 예외 던지기

예외는 비즈니스 로직을 처리하는 **Service 레이어** 에서 던진다.

```java
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    // 이메일 중복 검사
    @Override
    public void checkEmailDuplicate(String email) {
        if (memberDAO.existByEmail(email) != 0) {
            throw new MemberException("이메일 중복입니다.", HttpStatus.CONFLICT); // 409
        }
    }

    // 로그인
    @Override
    public MemberResponseDTO login(MemberLoginRequestDTO memberLoginRequestDTO) {
        return memberDAO
                .findByEmailAndPassword(MemberVO.from(memberLoginRequestDTO))
                .map(MemberResponseDTO::from)
                .orElseThrow(() -> {
                    throw new MemberLoginException("아이디 또는 비밀번호를 확인하세요"); // 400
                });
    }

    // 회원 단건 조회
    @Override
    public MemberResponseDTO getMemberInfo(Long id) {
        return memberDAO.findById(id)
                .map(MemberResponseDTO::from)
                .orElseThrow(() -> {
                    throw new MemberException("해당 회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
                });
    }
}
```

각 상황에 맞는 예외를 던지면, **컨트롤러는 비즈니스 로직을 알 필요가 없다.** 오류 분기를 서비스에 위임하고, 컨트롤러는 성공 응답만 담당한다.

---

## 9. 전역 예외 핸들러 — @RestControllerAdvice

던져진 예외를 잡아서 클라이언트에 보기 좋게 반환하는 것이 전역 예외 핸들러다.

```java
@RestControllerAdvice  // ← 모든 컨트롤러에 걸치는 AOP 기반 어드바이저
public class CustomExceptionHandler {

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ApiResponseDTO> handleException(PostException e) {
        return ResponseEntity
                .status(e.getStatus())           // 예외가 가진 HttpStatus 사용
                .body(ApiResponseDTO.of(e.getMessage())); // 예외 메세지를 body에
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponseDTO> handleException(MemberException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponseDTO.of(e.getMessage()));
    }
}
```

`@RestControllerAdvice`는 모든 `@RestController`에 AOP 방식으로 적용된다.
`@ExceptionHandler`에 지정한 예외 타입이 발생하면 해당 메서드가 실행된다.

이제 서비스에서 예외를 던지면 500 대신 이런 응답이 간다.

```json
// MemberException("이메일 중복입니다.", HttpStatus.CONFLICT) 발생 시
HTTP Status: 409 Conflict

{
  "message": "이메일 중복입니다.",
  "data": null
}
```

프론트엔드는 **상태 코드로 분기**하고 **message로 사용자에게 안내**할 수 있다.

---

## 10. 전체 흐름 정리

```
[Client 요청]
      │
      ▼
  Controller (MemberAPI)
      │ 성공 시 → ResponseEntity(상태코드, ApiResponseDTO.of(msg, data))
      │ 실패 시 → Service 에서 예외 throw
      ▼
  Service (MemberServiceImpl)
      │ 비즈니스 조건 실패 → throw MemberException / MemberLoginException
      ▼
  CustomExceptionHandler (@RestControllerAdvice)
      │ @ExceptionHandler 로 잡아서
      └─→ ResponseEntity(e.getStatus(), ApiResponseDTO.of(e.getMessage()))

[Client 응답] — 성공이든 실패든 항상 ApiResponseDTO 형태로 반환
```

---

## 11. 놓치기 쉬운 개념들

### 11-1. @ExceptionHandler의 우선순위

자식 예외를 처리하는 핸들러가 있으면 부모보다 먼저 실행된다.

```java
@ExceptionHandler(MemberLoginException.class) // 더 구체적 → 먼저 실행
public ResponseEntity<ApiResponseDTO> handleLoginException(MemberLoginException e) { ... }

@ExceptionHandler(MemberException.class) // 덜 구체적 → 위에서 안 잡히면 실행
public ResponseEntity<ApiResponseDTO> handleException(MemberException e) { ... }
```

현재 코드는 `MemberException` 핸들러 하나만 있고, `MemberLoginException`은 `MemberException`을 상속하므로 함께 잡힌다. 나중에 로그인 실패에 대한 별도 처리(ex. 로그인 시도 횟수 기록)가 필요해지면, `MemberLoginException` 전용 핸들러를 추가하면 된다.

### 11-2. Checked vs Unchecked Exception

| 구분 | 상속 | 특징 |
|------|------|------|
| Checked Exception | `Exception` | throws 선언 또는 try-catch 강제 |
| Unchecked Exception | `RuntimeException` | 강제 아님, 전파 가능 |

서비스에서 `throw new MemberException(...)` 을 선언 없이 자유롭게 던질 수 있는 이유가 바로 `RuntimeException`을 상속했기 때문이다.

### 11-3. Optional + orElseThrow 패턴

현재 서비스 코드에서 이 패턴이 사용된다.

```java
return memberDAO.findById(id)
        .map(MemberResponseDTO::from)         // VO → DTO 변환
        .orElseThrow(() -> {
            throw new MemberException("해당 회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        });
```

`Optional`은 null일 수 있는 값을 감싸는 컨테이너다.
- `map()`: 값이 있으면 변환한다 (`MemberVO → MemberResponseDTO`)
- `orElseThrow()`: 값이 없으면(null이면) 예외를 던진다

이 패턴은 null 체크를 명시적이지 않게 하면서도, 예외 처리까지 체이닝으로 연결할 수 있어서 가독성이 좋다.

### 11-4. 제네릭 타입 파라미터 명시 (심화)

현재 코드는 이렇게 되어있다.

```java
// 타입 파라미터를 명시하지 않은 형태 (Raw type)
public ResponseEntity<ApiResponseDTO> getAllMembers() { ... }
```

엄밀하게는 아래처럼 타입 파라미터를 명시하는 것이 더 안전하다.

```java
// 타입을 명확히 지정 → 컴파일 타임에 타입 안전성 보장
public ResponseEntity<ApiResponseDTO<List<MemberResponseDTO>>> getAllMembers() { ... }
```

실무에서는 팀 컨벤션에 따라 다르지만, 타입을 명시하면 IDE의 자동완성과 오류 감지가 정확해진다.

### 11-5. @Operation, @ApiResponses — Swagger 응답 문서화

컨트롤러에 Swagger 어노테이션으로 API의 가능한 응답 코드를 문서화한다.

```java
@Operation(summary = "회원가입 서비스", description = "회원 정보를 받아서 회원가입을 시켜주는 서비스")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "회원가입 성공"),
    @ApiResponse(responseCode = "409", description = "이메일 중복")
})
@PostMapping("/join")
public ResponseEntity<ApiResponseDTO> join(...) { ... }
```

이 어노테이션은 런타임 동작에 영향이 없지만, **Swagger UI에서 가능한 응답 코드와 설명을 보여준다.** 프론트엔드 개발자가 어떤 상태 코드를 처리해야 하는지 API 문서만 보고 알 수 있다.

---

## 마치며

이번에 적용한 패턴들을 정리하면 다음과 같다.

| 역할 | 방법 | 이점 |
|------|------|------|
| 항상 응답 반환 | `ResponseEntity<T>` | HTTP 표준 준수, 상태 코드 제어 |
| 응답 형태 통일 | `ApiResponseDTO<T>` (제네릭) | 프론트엔드 처리 코드 단순화 |
| 반환 유형 분기 | `of()` 오버로딩 | 데이터 유무에 따라 깔끔하게 분기 |
| 의미 있는 예외 | 커스텀 예외 클래스 + HttpStatus | 에러 원인과 상태를 함께 전달 |
| 예외 계층 구조 | 상속 (MemberException → MemberLoginException) | 공통 속성 재사용, 세분화 가능 |
| 전역 예외 처리 | `@RestControllerAdvice` + `@ExceptionHandler` | 컨트롤러가 성공만 담당, 예외는 한 곳에서 처리 |

이 구조가 완성되면 컨트롤러는 성공 경로만 담당하고, 실패 경로는 예외 핸들러가 일관되게 처리한다.
**화면에는 절대 500이 그대로 가지 않고, 개발자가 의도한 메세지와 상태 코드가 항상 반환된다.**
