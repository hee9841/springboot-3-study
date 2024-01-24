# 스프링 3 백엔드 개발자 되기 정리
## 쿠키, 토큰, 세션??

### 1. 쿠키, 세션
> HTTP 는 stateless(무상태)라는 특증으로 클라이언트가 이전에 요청한 상태등를 기억하지 못함.
> 그래서 만약 사용자/비사용자가 이용할 수 있는 서비스 구분, 사용자의 역할에 따른 서비스 구분을 하기 힘듬.
> 따라서 이런 클라이언트의 상태 정보를 유지하고 저장하기 `쿠키`가 등장

### 🍪 쿠키


---
## 토큰 기반 인증의 특징
### 무상태성
- 토큰은 클라이언트에 저장 됨 -> 서버의 자원을 소비할 필요X
- 서버입장에서는 클라이언트 인증 정보를 저장하거나 유지하지 않아도 상태를 관리할 수 있음

### 확정성
- 서버를 확장할때도 상태 관리를 신경 쓸 필요가 없어서 서버 확장에도 용의
- ex) 물건 파는 서비스 : 결제를 위한 서비스, 주문은을 위한 서비스 각각 다른 서버에 분리되어있다고 하면
-> 토큰으로 결제 서버와 주문 서버에 요청을 보낼 수 있음
- 다른 소셜로그인, 회원가입으로 확장할 수 있고 -> 다른 서비스에 권한을 공유할 수 있음

### 무결성
- 토큰은 HMAC(hash-based message authentication) 기법을 사용
- 발급 이후에 토큰 정보를 변경할 수 없음 -> 무결성이 보장

---

## JWT



