# Redis를 이용한 JWT 토큰 관리 전략

## 왜 Redis인가

### 인메모리 저장 방식

Redis는 데이터를 디스크가 아닌 **메모리(RAM)에 저장**한다.

덕분에 일반 RDB(MySQL 등)와 비교했을 때 읽기/쓰기 속도가 압도적으로 빠르다.
토큰 검증은 모든 API 요청마다 발생하는 작업이기 때문에 속도가 중요하다.

### 휘발성 — 오히려 장점

Redis는 전원이 꺼지거나 서버가 재시작되면 데이터가 유실된다.

토큰 관리에서는 이게 **단점이 아니라 장점**이다.

- 토큰 데이터는 영구 보존이 필요 없다
- 서버 재시작으로 데이터가 유실되더라도 사용자가 다시 로그인하면 그만이다
- 굳이 영속성 있는 RDB에 토큰을 저장할 이유가 없다

### TTL 자동 삭제

일반 RDB에는 TTL 자동 삭제 기능이 없다. 만료된 토큰을 직접 배치 작업으로 삭제해야 한다.

Redis는 TTL(Time To Live)을 설정하면 **만료 시 자동으로 삭제**된다.

```
SET refresh_token "value" EX 604800  // 7일 후 자동 삭제
```

토큰처럼 일정 기간 후 자동으로 사라져야 하는 데이터에 최적화된 구조다.

---

## Refresh Token 생명 주기

### 왜 AccessToken과 RefreshToken으로 나누는가

AccessToken만 사용하면 두 가지 문제가 생긴다.

- 만료 시간을 길게 → 토큰 탈취 시 오랫동안 악용 가능
- 만료 시간을 짧게 → 매번 재로그인 필요, 사용 불편

이를 해결하기 위해 두 가지 토큰을 함께 사용한다.

| | AccessToken | RefreshToken |
|--|-------------|--------------|
| 만료 시간 | 짧게 (15분 ~ 1시간) | 길게 (7일 ~ 30일) |
| 용도 | API 요청마다 인증 | AccessToken 재발급 |
| 저장 위치 | 쿠키 | 쿠키 + Redis |

### 생명 주기

```
[로그인 성공]
    ↓
AccessToken 발급 → 쿠키에 저장
RefreshToken 발급 → 쿠키 저장 + Redis에 TTL과 함께 저장
    ↓
[AccessToken 만료]
    ↓
클라이언트가 RefreshToken으로 재발급 요청
    ↓
서버: 쿠키의 RefreshToken == Redis의 RefreshToken ?
    ├── 일치 → 새 AccessToken 발급 반환
    └── 불일치 → 401 반환 → 재로그인
    ↓
[RefreshToken TTL 만료]
    ↓
Redis에서 자동 삭제 → 재로그인 필요
```

---

## 로그아웃과 블랙리스트

### 왜 블랙리스트가 필요한가

JWT는 서버가 발급한 이후 **서버가 무효화할 수단이 없다**.

로그아웃을 해도 토큰 자체는 만료 시간까지 유효하기 때문에,
탈취된 토큰이 있다면 로그아웃 이후에도 악용될 수 있다.

이를 막기 위해 로그아웃 시 해당 토큰을 블랙리스트로 Redis에 등록한다.

### 블랙리스트 TTL 설정 시 주의점

블랙리스트의 TTL은 **RefreshToken의 TTL과 같거나 더 길어야 한다.**

```
RefreshToken TTL: 7일
BlackList TTL: 7일 이상 ← 반드시 지켜야 함
```

만약 블랙리스트 TTL이 더 짧으면:

```
블랙리스트 만료 → Redis에서 사라짐
RefreshToken은 아직 유효
→ 탈취된 토큰으로 AccessToken 재발급 가능
```

블랙리스트의 목적은 **"RefreshToken이 살아있는 동안 재발급을 막는 것"** 이다.
RefreshToken 유효 기간 내에 블랙리스트가 먼저 사라지면 의미가 없다.

### 로그아웃 흐름

```
[로그아웃 요청]
    ↓
RefreshToken을 블랙리스트로 Redis에 등록 (TTL = RefreshToken TTL 이상)
쿠키에서 AccessToken, RefreshToken 삭제
    ↓
이후 해당 RefreshToken으로 재발급 요청 시
    ↓
블랙리스트 확인 → 등록된 토큰 → 401 반환
```

---

## 정리

| 항목 | 내용 |
|------|------|
| Redis 선택 이유 | 인메모리 속도, TTL 자동 삭제, 휘발성이 오히려 적합 |
| AccessToken | 짧은 수명, API 인증용 |
| RefreshToken | 긴 수명, Redis 저장, AccessToken 재발급용 |
| 블랙리스트 TTL | RefreshToken TTL 이상으로 설정 필수 |
