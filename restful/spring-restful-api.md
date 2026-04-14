# Spring Boot로 RESTful API 설계하기

> Flutter에서 Firebase Cloud Functions를 HTTP로 호출하던 경험이 있다면, Spring RESTful API는 그것과 굉장히 유사하다.
> 결국 URL을 통해 데이터를 주고받는 구조는 동일하다.

---

## 목차

1. [RESTful API란?](#1-restful-api란)
2. [@RestController vs @Controller](#2-restcontroller-vs-controller)
3. [HTTP 메서드와 URL 설계 규칙](#3-http-메서드와-url-설계-규칙)
4. [프로젝트 레이어드 아키텍처](#4-프로젝트-레이어드-아키텍처)
5. [VO와 DTO의 분리 - 보안을 위한 설계](#5-vo와-dto의-분리---보안을-위한-설계)
6. [정적 팩토리 메서드 패턴](#6-정적-팩토리-메서드-패턴)
7. [Stream API로 리스트 변환하기](#7-stream-api로-리스트-변환하기)
8. [@PathVariable, @RequestParam, @RequestBody](#8-pathvariable-requestparam-requestbody)
9. [MyBatis + HikariCP 설정](#9-mybatis--hikaricp-설정)
10. [Swagger로 API 문서 자동화](#10-swagger로-api-문서-자동화)
11. [Serializable과 JSON 직렬화](#11-serializable과-json-직렬화)
12. [ResponseEntity로 HTTP 상태 코드 제어하기](#12-responseentity로-http-상태-코드-제어하기)

---

## 1. RESTful API란?

**REST(Representational State Transfer)**는 HTTP를 기반으로 클라이언트와 서버 간 데이터를 주고받는 아키텍처 스타일이다.

기존 Spring MVC 방식이 **화면(HTML 페이지)을 반환**했다면,
RESTful API는 **데이터(JSON)를 반환**한다.

```
기존 MVC 방식:
브라우저 → 요청 → 서버 → HTML 페이지 반환

RESTful API 방식:
React/Flutter 등 → 요청 → 서버 → JSON 데이터 반환
```

### Flutter Firebase Cloud Functions와 비교

Flutter에서 Firebase Cloud Functions를 사용해본 적이 있다면 감이 잡힐 것이다.

```dart
// Flutter에서 Firebase Cloud Function 호출
final response = await http.get(
  Uri.parse('https://us-central1-myapp.cloudfunctions.net/getMembers'),
);
```

Spring RESTful API도 정확히 같은 방식이다. URL로 함수(기능)를 호출하고, JSON으로 응답받는다.

```
GET http://localhost:10000/api/members        → 전체 회원 목록 반환 (JSON)
GET http://localhost:10000/api/members/1      → ID가 1인 회원 정보 반환 (JSON)
```

---

## 2. @RestController vs @Controller

| 어노테이션 | 반환값 | 용도 |
|---|---|---|
| `@Controller` | HTML 뷰 (Thymeleaf 등) | 페이지 중심 |
| `@RestController` | JSON 데이터 | 데이터 중심 |

`@RestController`는 `@Controller` + `@ResponseBody`의 합성 어노테이션이다.
즉, 메서드의 반환값을 자동으로 JSON으로 변환해서 HTTP 응답 바디에 담아준다.

```java
// 리턴값을 JSON으로 처리
// 해당 컨트롤러는 화면에 데이터를 전달하기 위한 컨트롤러
// 즉 페이지 중심 대신 데이터 중심 컨트롤러
// 데이터 중심 컨트롤러는 업무처마다 다르지만 클래스 명 끝에 API 라는 키워드 붙임
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")  // 추가적으로 api 라는 식으로 경로를 정해줘야
                                  // 기존 Controller의 RequestMapping이랑 안 겹침
public class MemberAPI {
    private final MemberServiceImpl memberService;

    // 회원 목록 조회 서비스
    // 해당 기능은 주소창에 api/members 로 들어왔을 때 수행되는 메서드
    @GetMapping("")
    public List<MemberResponseDTO> getAllMembers() {
        return memberService.getMembers();
    }

    // 회원 정보 조회 서비스
    // members 뒤에 오는 회원 id 값을 토대로 해서 해당 조건의 데이터를 반환
    // 패스 배리어블로 해서 요청을 보냈어야 함
    @GetMapping("{id}")
    public MemberResponseDTO getMemberInfo(@PathVariable Long id) {
        Optional<MemberResponseDTO> foundMember = memberService.getMemberInfo(id);
        if (foundMember.isPresent()) {
            return foundMember.get();
        }
        return new MemberResponseDTO();
    }
}
```

---

## 3. HTTP 메서드와 URL 설계 규칙

RESTful API에서는 **URL은 자원(명사)**으로, **HTTP 메서드는 행위(동사)**로 표현한다.

### HTTP 메서드 종류

| 메서드 | 역할 | Spring 어노테이션 |
|---|---|---|
| GET | 조회 | `@GetMapping` |
| POST | 생성 | `@PostMapping` |
| PUT | 전체 수정 | `@PutMapping` |
| PATCH | 부분 수정 | `@PatchMapping` |
| DELETE | 삭제 | `@DeleteMapping` |

### URL 설계 예시 (회원 API)

```
GET    /api/members          → 전체 회원 목록 조회
GET    /api/members/{id}     → 특정 회원 조회
POST   /api/members          → 회원 가입
PUT    /api/members/{id}     → 회원 정보 전체 수정
PATCH  /api/members/{id}     → 회원 정보 일부 수정 (비밀번호 등)
DELETE /api/members/{id}     → 회원 탈퇴
```

### REST URL 설계 규칙

1. **명사를 사용한다** — `/getMembers` (X) → `/members` (O)
2. **소문자를 사용한다** — `/Members` (X) → `/members` (O)
3. **복수형을 사용한다** — `/member` (X) → `/members` (O)
4. **언더스코어 대신 하이픈** — `/member_info` (X) → `/member-info` (O)
5. **계층 관계는 슬래시로** — `/members/{memberId}/posts`
6. **파일 확장자 포함하지 않는다** — `/members.json` (X)

---

## 4. 프로젝트 레이어드 아키텍처

```
[Client (React / Flutter)]
         ↕ HTTP (JSON)
[API Layer - @RestController]   ← MemberAPI.java
         ↕
[Service Layer - @Service]      ← MemberService.java / MemberServiceImpl.java
         ↕
[Repository Layer - @Repository]← MemberDAO.java
         ↕
[Mapper Layer - @Mapper]        ← MemberMapper.java
         ↕
[DB - Oracle]
```

각 레이어의 역할:

- **API (Controller)**: HTTP 요청을 받고 응답을 반환. 비즈니스 로직 없음
- **Service**: 비즈니스 로직 처리, VO → DTO 변환, 트랜잭션 관리
- **DAO (Repository)**: Mapper를 감싸는 레이어. 의미 있는 메서드명 제공
- **Mapper**: MyBatis 인터페이스. SQL과 Java 메서드를 연결

### Service 인터페이스 설계

```java
public interface MemberService {
    // 회원 가입
    public void join(MemberVO memberVO);

    // 로그인 (사실은 token을 반환하는 걸로 해야 함)
    public Optional<MemberVO> login(MemberVO memberVO);

    // 회원 정보 조회
    public Optional<MemberResponseDTO> getMemberInfo(Long id);

    // 멤버 전체 정보 조회
    public List<MemberResponseDTO> getMembers();

    // 회원 수정
    // 회원 비밀번호 변경 (마이페이지)
    // 회원 비밀번호 변경 (로그인 전)

    // 회원 탈퇴
}
```

> **포인트**: 로그인은 실제 서비스에서 `MemberVO`를 반환하는 게 아니라 **JWT Token**을 반환해야 한다.

---

## 5. VO와 DTO의 분리 - 보안을 위한 설계

### MemberVO (DB와 1:1 매핑)

```java
// json으로 직렬화하기 위한 것 필요
@Component
@Data
public class MemberVO implements Serializable {
    private Long id;
    private String memberEmail;
    private String memberPassword;  // 비밀번호 포함
    private String memberName;
}
```

### MemberResponseDTO (화면에 전달하는 응답용)

```java
// 해당 DTO는 화면으로 보내주기 위해 (응답) 사용하는 DTO
// 화면에는 줘야 하는 값과 주면 안 되는 값이 존재한다. 따라서 이를 설정해야 한다.
@Component
@Data
public class MemberResponseDTO {
    private Long id;
    private String memberEmail;
    private String memberName;
    // memberPassword 필드 없음 → API 응답에서 비밀번호 노출 차단
}
```

**왜 분리하는가?**

- `MemberVO`를 그대로 반환하면 **비밀번호가 JSON에 포함**되어 클라이언트에 노출된다
- 응답 전용 DTO를 만들어 민감 정보를 제거한 뒤 반환해야 한다
- `select` 쿼리에서 비밀번호를 제외하는 방법도 있지만, **DTO로 분리하는 것이 더 명시적이고 안전**하다

```xml
<!-- 로그인 쿼리: SELECT에서 MEMBER_PASSWORD 제외 -->
<select id="selectByMemberEmailAndMemberPassword" parameterType="MemberVO" resultType="MemberVO">
    SELECT ID, MEMBER_EMAIL, MEMBER_NAME
    FROM TBL_MEMBER
    WHERE MEMBER_EMAIL = #{memberEmail}
      AND MEMBER_PASSWORD = #{memberPassword}
</select>
```

---

## 6. 정적 팩토리 메서드 패턴

VO를 DTO로 변환할 때 **생성자 대신 정적 팩토리 메서드**를 사용한다.

```java
// 초기화 생성자 만들면 기본 생성자가 없어서 에러가 발생할 수 있다.
// 그래서 정적 팩토리 메서드 고려
// 만약 생성자를 만들면 무슨 역할인지 헷갈리지만
// from이라는 정적 메서드를 만들면 외부에서 받아서 형변환 하는 거라고 알기 쉬움
public static MemberResponseDTO from(MemberVO memberVO) {
    MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
    memberResponseDTO.setId(memberVO.getId());
    memberResponseDTO.setMemberEmail(memberVO.getMemberEmail());
    memberResponseDTO.setMemberName(memberVO.getMemberName());
    // memberPassword는 세팅하지 않음 → 보안
    return memberResponseDTO;
}
```

사용할 때는 이렇게:

```java
MemberResponseDTO dto = MemberResponseDTO.from(memberVO);
```

**정적 팩토리 메서드의 장점:**
- 메서드 이름으로 의도 전달 (`from`, `of`, `valueOf` 등)
- 기본 생성자를 건드리지 않아 Spring 빈 등록에 안전
- 변환 로직이 DTO 클래스 내부에 캡슐화됨

---

## 7. Stream API로 리스트 변환하기

전체 회원 목록 조회 시 `List<MemberVO>` → `List<MemberResponseDTO>`로 변환이 필요하다.

```java
@Override
public List<MemberResponseDTO> getMembers() {
    List<MemberVO> memberVOList = memberDAO.findAll();

    // 해당 과정을 통해서 리스트 내 원소를 MemberResponseDTO 자료형으로 바꾼
    // 새로운 리스트를 만들어서 반환 (비밀번호 제거)
    List<MemberResponseDTO> members = memberVOList.stream()
            .map(memberVO -> MemberResponseDTO.from(memberVO))
            .collect(Collectors.toList());

    return members;
}
```

메서드 참조로 더 간결하게 쓸 수도 있다:

```java
List<MemberResponseDTO> members = memberVOList.stream()
        .map(MemberResponseDTO::from)
        .collect(Collectors.toList());
```

---

## 8. @PathVariable, @RequestParam, @RequestBody

### @PathVariable — URL 경로에서 값 추출

```java
// GET /api/members/1  → id = 1
@GetMapping("{id}")
public MemberResponseDTO getMemberInfo(@PathVariable Long id) { ... }
```

### @RequestParam — 쿼리 파라미터에서 값 추출

```java
// GET /api/members?page=1&size=10
@GetMapping("")
public List<MemberResponseDTO> getMembers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) { ... }
```

### @RequestBody — HTTP 바디(JSON)에서 값 추출

POST, PUT 요청처럼 JSON 데이터를 전송할 때 사용한다.

```java
// POST /api/members
// Request Body: { "memberEmail": "test@test.com", "memberPassword": "1234", "memberName": "홍길동" }
@PostMapping("")
public void join(@RequestBody MemberVO memberVO) {
    memberService.join(memberVO);
}
```

---

## 9. MyBatis + HikariCP 설정

### application.yaml

```yaml
server:
  port: 10000

spring:
  datasource:
    hikari:
      driver-class-name: oracle.jdbc.OracleDriver
      jdbc-url: jdbc:oracle:thin:@//localhost:1521/XE
      username: hr
      password: 1234
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher  # Swagger 사용 시 필요
```

### MyBatisConfig.java

```java
@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    // applicationContext를 통해 resources의 경로를 가져온다.
    private final ApplicationContext applicationContext;

    // yml 파일의 connection 정보를 가져오기 위한 작업
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    // DataSource 객체에 따라 설정해 놓은 connection 설정을 넣어준다.
    @Bean
    public DataSource getDataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    // SqlSession을 만들기 위한 SqlSessionFactory를 제작한다.
    public SqlSessionFactory sqlSessionFactory() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        sqlSessionFactoryBean.setMapperLocations(
            applicationContext.getResources("classpath*:/mapper/*.xml")
        );
        sqlSessionFactoryBean.setConfigLocation(
            applicationContext.getResource("classpath:/config/config.xml")
        );
        try {
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
            // 스네이크 케이스 → 카멜 케이스 자동 변환
            // DB: MEMBER_EMAIL → Java: memberEmail
            sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
            return sqlSessionFactory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

> **HikariCP란?**
> 가장 빠른 Java DB 커넥션 풀 라이브러리. DB 연결을 미리 만들어두고 재사용한다.
> Spring Boot 2.x 이후 기본 커넥션 풀로 채택되었다.

### MyBatis XML Mapper

```xml
<mapper namespace="com.app.restful.mapper.MemberMapper">
    <!-- 회원 추가: Oracle 시퀀스로 ID 자동 생성 -->
    <insert id="insert" parameterType="MemberVO">
        INSERT INTO TBL_MEMBER
        VALUES (SEQ_MEMBER.nextval, #{memberEmail}, #{memberPassword}, #{memberName})
    </insert>

    <!-- 동적 SQL: 비밀번호가 있을 때만 비밀번호도 함께 업데이트 -->
    <update id="update" parameterType="MemberVO">
        UPDATE TBL_MEMBER
        SET MEMBER_EMAIL = #{memberEmail}, MEMBER_NAME = #{memberName}
        <if test="memberPassword != null and memberPassword != ''">
            , MEMBER_PASSWORD = #{memberPassword}
        </if>
        WHERE ID = #{id}
    </update>
</mapper>
```

---

## 10. Swagger로 API 문서 자동화

Swagger(OpenAPI)를 사용하면 API 명세서를 코드에서 자동으로 생성할 수 있다.
별도 문서 작성 없이 UI에서 직접 API를 테스트할 수 있다.

```java
// http://localhost:10000/swagger-ui/index.html
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restful API Documentation")
                        .description("Restful API Documentation")
                        .version("0.1"));
    }
}
```

`build.gradle` 의존성 추가:

```gradle
implementation 'org.springdoc:springdoc-openapi-ui:1.7.0'
```

서버 실행 후 `http://localhost:10000/swagger-ui/index.html` 접속하면 아래처럼 API 목록과 테스트 UI를 볼 수 있다.

```
GET  /api/members       getAllMembers
GET  /api/members/{id}  getMemberInfo
...
```

---

## 11. Serializable과 JSON 직렬화

```java
// json으로 직렬화하기 위한 것 필요
public class MemberVO implements Serializable {
    ...
}
```

**직렬화(Serialization)란?**
Java 객체를 바이트 스트림(또는 JSON 문자열)으로 변환하는 과정이다.

- Java → JSON 변환 = **직렬화 (Serialization)**
- JSON → Java 변환 = **역직렬화 (Deserialization)**

Spring은 내부적으로 `Jackson` 라이브러리를 사용해 자동으로 처리한다.
`Serializable` 인터페이스를 구현하면 캐싱, 세션 저장 등 다양한 곳에서 객체를 안전하게 직렬화할 수 있다.

```java
// @RestController에서 객체를 반환하면
// Jackson이 자동으로 JSON으로 변환해서 응답 바디에 담아준다
@GetMapping("")
public List<MemberResponseDTO> getAllMembers() {
    return memberService.getMembers();
    // → [{"id":1,"memberEmail":"test@test.com","memberName":"홍길동"}, ...]
}
```

---

## 12. ResponseEntity로 HTTP 상태 코드 제어하기

현재 코드에서는 단순히 객체를 반환하고 있지만, 실무에서는 `ResponseEntity`로 **HTTP 상태 코드**를 함께 제어하는 것이 좋다.

```java
@GetMapping("{id}")
public ResponseEntity<MemberResponseDTO> getMemberInfo(@PathVariable Long id) {
    Optional<MemberResponseDTO> foundMember = memberService.getMemberInfo(id);

    // 찾은 경우: 200 OK + 데이터
    // 없는 경우: 404 Not Found
    return foundMember
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

### 주요 HTTP 상태 코드

| 코드 | 의미 | 사용 상황 |
|---|---|---|
| 200 OK | 성공 | GET 조회 성공 |
| 201 Created | 생성 성공 | POST 성공 |
| 204 No Content | 내용 없음 | DELETE, 수정 성공 |
| 400 Bad Request | 잘못된 요청 | 입력값 검증 실패 |
| 401 Unauthorized | 인증 필요 | 로그인 필요 |
| 403 Forbidden | 권한 없음 | 접근 권한 없음 |
| 404 Not Found | 없음 | 존재하지 않는 리소스 |
| 500 Internal Server Error | 서버 오류 | 서버 내부 에러 |

---

## 전체 흐름 정리

```
[React/Flutter 클라이언트]
  GET /api/members/1
         ↓
[MemberAPI - @RestController]
  @GetMapping("{id}")
  getMemberInfo(id = 1L)
         ↓
[MemberServiceImpl - @Service]
  getMemberInfo(1L)
  → memberDAO.findById(1L) 호출
  → MemberVO를 MemberResponseDTO.from()으로 변환 (비밀번호 제거)
         ↓
[MemberDAO - @Repository]
  findById(1L)
  → memberMapper.select(1L) 호출
         ↓
[MemberMapper - @Mapper]
  select(1L)
  → XML의 SELECT 쿼리 실행
         ↓
[Oracle DB]
  SELECT ID, MEMBER_EMAIL, MEMBER_PASSWORD, MEMBER_NAME
  FROM TBL_MEMBER WHERE ID = 1
         ↑
결과를 거슬러 올라가 JSON으로 반환:
{"id":1,"memberEmail":"test@test.com","memberName":"홍길동"}
```

---

## 마치며

Spring RESTful API는 결국 **URL 주소 = 기능, HTTP 메서드 = 행위** 라는 규칙 아래 데이터를 JSON으로 주고받는 구조다.

Firebase Cloud Functions를 `https://...cloudfunctions.net/getMembers`로 호출하던 것처럼, Spring에서는 `http://localhost:10000/api/members`로 호출하면 된다. 클라이언트 입장에서 보면 그냥 URL에 HTTP 요청을 보내고 JSON을 받는 것이다.

다음 단계로는:
- **JWT 토큰 기반 인증/인가** (Spring Security)
- **CORS 설정** (React와 연동 시 필수)
- **예외 처리** (`@ExceptionHandler`, `@ControllerAdvice`)
- **입력값 검증** (`@Valid`, `@Validated`)

를 학습하면 완성도 있는 RESTful API를 만들 수 있다.
