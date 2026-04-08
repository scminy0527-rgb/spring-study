# Spring MVC Controller 완전 정복

## 1. 컨트롤러는 엔드포인트다

기존 JSP 방식에서는 `FrontController`를 직접 만들고, 각 기능을 담당하는 클래스를 별도로 구현해야 했습니다.

```java
// 기존 JSP 방식 (서블릿 직접 구현)
@WebServlet("/member/*")
public class MemberFrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getPathInfo();
        if (path.equals("/join")) { ... }
        else if (path.equals("/login")) { ... }
        // 분기 처리를 직접 해야 함
    }
}
```

Spring MVC에서는 `@Controller`와 매핑 어노테이션만 붙이면 끝입니다.

```java
@Controller
@RequestMapping("/members/*")
public class MemberController {

    @GetMapping("join")
    public void goToJoin(MemberVO memberVO) {;}

    @PostMapping("join")
    public RedirectView join(MemberVO memberVO) {
        memberMapper.insert(memberVO);
        return new RedirectView("/members/login");
    }
}
```

DispatcherServlet이 FrontController 역할을 대신하고, 각 메서드가 개별 기능을 담당합니다.

---

## 2. 반환값 = 이동할 페이지

컨트롤러 메서드의 반환값은 **이동할 뷰(페이지) 경로**입니다.

```java
// templates/ex01.html 을 반환
@GetMapping("/ex01")
public String ex01(String name, int age) {
    return "ex01";
}

// templates/members/login.html 을 반환
@GetMapping("login")
public String goToLogin() {
    return "members/login";
}
```

### 반환 생략 (void)

**GET 매핑 + 메서드 매핑 경로 == HTML 파일 경로** 조건이 맞으면 반환문을 생략할 수 있습니다.

```java
// @GetMapping("join") → templates/members/join.html 자동 매핑
@GetMapping("join")
public void goToJoin(MemberVO memberVO) {;}
```

> 단, `@RequestMapping("/members/*")`가 클래스에 붙어 있어서 `templates/members/join.html`과 경로가 일치할 때만 가능합니다.

---

## 3. 파라미터 수신 방법

### 방법 1: 변수명 그대로 받기

쿼리스트링이나 form 데이터의 `name` 속성과 매개변수명이 일치하면 자동 바인딩됩니다.

```java
// /ex/ex01?name=홍길동&age=23
@GetMapping("/ex01")
public String ex01(String name, int age) {
    log.info("이름: {}", name);       // 홍길동
    log.info("나이: {}세", age + ""); // 23세
    return "ex01";
}
```

### 방법 2: VO 객체로 받기

필드명이 일치하면 Spring이 VO 객체에 자동으로 값을 채워줍니다.

```java
@PostMapping("ex06")
public String ex06(MemberVO memberVO) {
    log.info("MemberVO: {}", memberVO); // MemberVO{memberName=홍길동, ...}
    return "redirect:/ex/ex06-complete";
}
```

### 방법 3: @ModelAttribute

쿼리스트링 방식에서만 사용 가능하며, Model에 자동으로 값을 담아줍니다.

```java
// /ex/ex04?name=홍길동 → 화면에서 ${name} 으로 바로 참조 가능
@GetMapping("ex04")
public String ex04(@ModelAttribute("name") String name) {
    return "ex04";
}

@GetMapping("ex05")
public String ex05(@ModelAttribute("name") String name,
                   @ModelAttribute("hobby") String hobby) {
    return "ex05";
}
```

### 방법 4: HttpServletRequest (전통적 방식)

JSP 방식처럼 `HttpServletRequest`로 직접 받는 것도 가능합니다.

```java
@PostMapping("/ex01")
public void ex01Post(HttpServletRequest req) {
    String name = req.getParameter("name");
    int age = Integer.parseInt(req.getParameter("age"));
}
```

---

## 4. Model vs setAttribute

| 방식 | JSP | Spring MVC |
|------|-----|------------|
| 데이터 전달 | `req.setAttribute("key", value)` | `model.addAttribute("key", value)` |
| 화면 참조 | `${key}` | `${key}` (동일) |

```java
@GetMapping("ex02")
public String ex02(String name, Model model) {
    model.addAttribute("name", name); // 화면에서 ${name} 으로 참조
    return "ex02";
}

@GetMapping("ex03")
public String ex03(Model model) {
    List<String> names = List.of("홍길동", "이순신", "장보고");
    model.addAttribute("names", names);
    return "ex03";
}
```

---

## 5. GET vs POST 리다이렉트

POST 요청 후에는 결과 페이지로 이동해야 합니다. 두 가지 방법이 있습니다.

```java
// 방법 1: 문자열 반환
return "redirect:/ex/ex06-complete";

// 방법 2: RedirectView 객체 반환
return new RedirectView("/members/login");
```

### PRG 패턴 (Post-Redirect-Get)

POST 후 바로 뷰를 렌더링하면 새로고침 시 폼이 재제출되는 문제가 생깁니다.
이를 방지하기 위해 **POST → Redirect → GET** 패턴을 사용합니다.

```
사용자 폼 제출 (POST)
    → 서버 처리
    → Redirect 응답
    → 브라우저 GET 요청
    → 결과 페이지 표시
```

---

## 6. RedirectAttributes - Flash 데이터

리다이렉트 시 데이터를 전달하는 방법입니다. 일반 세션에 저장하면 명시적으로 지우지 않는 한 계속 남아 세션 과부하가 생길 수 있지만, Flash 속성은 **다음 요청 1회만 유지**되고 자동으로 사라집니다.

```java
@PostMapping("login")
public RedirectView login(MemberVO memberVO, RedirectAttributes redirectAttributes) {

    // 이메일이 존재하지 않으면
    if (memberMapper.existByMemberEmail(memberEmail) == 0) {
        // Flash 영역에 저장 → 다음 요청(GET /members/login) 후 자동 소멸
        redirectAttributes.addFlashAttribute("isLogin", false);
        return new RedirectView("/members/login");
    }

    // 로그인 성공 시 → 세션에 저장, Flash 불필요
    session.setAttribute("member", member);
    return new RedirectView("/members/my-page");
}
```

| 메서드 | 특징 |
|--------|------|
| `addFlashAttribute` | 리다이렉트 후 1회만 유지, 컨트롤러/뷰에서 사용 가능 |
| `addAttribute` | URL 쿼리스트링으로 붙음, 뷰에서 사용 불가 |

---

## 7. 의존성 주입 (DI) - 핵심 개념

컨트롤러에서 DB 작업을 하려면 Mapper가 필요합니다. 이때 의존성 주입이 등장합니다.

### 왜 필드 주입이 아닌 생성자 주입인가?

```java
// 지양: 필드 주입
@Autowired
private MemberMapper memberMapper; // 테스트 외 환경에서 권장되지 않음

// 권장: 생성자 주입
@Controller
@RequiredArgsConstructor // Lombok이 생성자 자동 생성
public class MemberController {
    private final MemberMapper memberMapper; // final → 불변, 반드시 초기화 필요
    private final HttpSession session;
}
```

`final`로 선언하면:
1. 한 번 주입된 Mapper가 **중간에 바뀌지 않음**을 보장합니다.
2. 컴파일 시점에 주입 누락을 잡을 수 있습니다.
3. 특정 컨트롤러는 **관련 Mapper만** 사용한다는 명확한 설계 의도를 표현합니다.

`@RequiredArgsConstructor`는 `final` 필드를 모두 포함하는 생성자를 자동으로 만들어줍니다.

---

## 8. 세션 관리

```java
// 로그인 성공 시 세션 저장
session.setAttribute("member", member);

// 로그아웃 시 세션 전체 초기화 (invalidate)
@PostMapping("logout")
public RedirectView logout() {
    session.invalidate(); // 세션의 모든 값 초기화 → 브라우저 부하 감소
    return new RedirectView("/members/login");
}

// 회원 탈퇴 시 세션 정보로 처리 후 초기화
@PostMapping("delete")
public RedirectView delete() {
    MemberVO member = (MemberVO) session.getAttribute("member");
    memberMapper.delete(member.getId());
    session.invalidate();
    return new RedirectView("/members/login");
}
```

> `session.removeAttribute("key")` 는 특정 값만 제거, `session.invalidate()` 는 세션 전체 초기화입니다.

---

## 9. MyBatis 동적 쿼리 - `<if>` 구문

비밀번호 수정처럼 **선택적으로 업데이트**해야 하는 경우 MyBatis의 `<if>` 구문을 활용합니다.

```xml
<update id="update" parameterType="MemberVO">
    UPDATE TBL_MEMBER
    SET MEMBER_EMAIL = #{memberEmail},
        MEMBER_NAME = #{memberName}
        <if test="memberPassword != null and memberPassword != ''">
        , MEMBER_PASSWORD = #{memberPassword}
        </if>
    WHERE ID = #{id}
</update>
```

비밀번호 input이 비어있으면 `memberPassword`가 빈 문자열로 넘어오고, `<if>` 조건을 통과하지 못해 기존 비밀번호를 그대로 유지합니다.

---

## 10. Thymeleaf - form 바인딩

### `th:object` vs `name` 속성

| | `name` 속성 | `th:field` |
|---|---|---|
| 오타 발견 | null로 조용히 실패 | 명확한 예외 발생 |
| 기존 값 채우기 | `th:value` 별도 작성 필요 | 자동으로 value 세팅 |
| id/name 생성 | 직접 작성 | 자동 생성 |

```html
<!-- th:object + th:field 방식 (수정 페이지) -->
<form th:action="@{/members/update}" method="post" th:object="${memberVO}">
    <input type="text" th:field="*{memberName}">    <!-- id, name, value 자동 생성 -->
    <input type="email" th:field="*{memberEmail}">
    <input type="password" th:field="*{memberPassword}">
</form>
```

### `th:object`의 제약

`th:object`는 form 전용이며 Spring 데이터 바인딩을 요구합니다. 따라서 세션 객체를 직접 사용할 수 없습니다.

```java
// 컨트롤러에서 세션 값을 꺼내 Model에 담아야 함
@GetMapping("update")
public void goToUpdate(Model model) {
    MemberVO member = (MemberVO) session.getAttribute("member");
    model.addAttribute("memberVO", member); // Model을 통해 전달
}
```

```html
<!-- 단순 출력 → session 직접 참조 가능 -->
<p th:text="${session.member.memberName}">이름</p>

<!-- 폼 바인딩 → Model 객체만 가능 -->
<form th:object="${memberVO}"> ... </form>
```

---

## 11. fetch와 리다이렉트

`fetch`로 요청을 보낼 때는 서버의 리다이렉트 응답이 **브라우저 페이지 이동으로 연결되지 않습니다.**
`form submit`이나 `location.href`는 브라우저가 자동으로 이동하지만, `fetch`는 JS가 응답을 받을 뿐입니다.

```javascript
// 회원 탈퇴 - fetch 사용 시 직접 페이지 이동 처리
const button = document.querySelector("#delete-btn");
button.addEventListener("click", () => {
    fetch("/members/delete", { method: "POST" })
        .then(() => location.href = "/members/login"); // 직접 이동
});
```

---

## 정리

| 개념 | JSP 방식 | Spring MVC |
|------|----------|------------|
| 라우팅 | FrontController 직접 구현 | `@GetMapping`, `@PostMapping` |
| 데이터 전달 | `req.setAttribute` | `Model.addAttribute` |
| 파라미터 수신 | `req.getParameter` | 매개변수 자동 바인딩 |
| 리다이렉트 | `resp.sendRedirect` | `redirect:` 문자열 or `RedirectView` |
| 세션 | `req.getSession` | `HttpSession` DI 주입 |
