# ☕ I Love Coffee

Spring Boot + React 기반의 커피 주문 관리 서비스입니다.

사용자는 메뉴를 조회하고 주문할 수 있으며,
관리자는 메뉴와 주문을 관리할 수 있습니다.

---

# 📌 프로젝트 소개

**I Love Coffee**는 온라인 커피 주문 서비스를 가정하여 제작한 프로젝트입니다.

주문 생성부터 주문 조회, 주문 취소, 메뉴 관리, 이미지 업로드까지
실제 서비스에서 자주 사용하는 기능들을 구현하였습니다.

또한 고객 화면과 관리자 화면을 분리하여
REST API 기반으로 Spring Boot와 React를 연동하였습니다.

---

# ✨ 주요 기능

## 👤 고객

- 메뉴 목록 조회
- 메뉴 상세 조회
- 메뉴 이미지 제공
- 주문 생성
- 주문 조회 (이메일)
- 주문 취소
- 수량 직접 입력
- 주문 가능 수량 자동 제한
- 품절 메뉴 자동 비활성화
- 주소 검색 (Daum 우편번호 서비스)

---

## 👨‍💼 관리자

### 메뉴 관리

- 메뉴 등록
- 메뉴 수정
- 메뉴 삭제
- 메뉴 활성 / 비활성
- 이미지 업로드
- 기본 이미지 지원

### 주문 관리

- 전체 주문 조회
- 이메일별 주문 조회
- 주문 상세 조회
- 주문 취소
- 주문 삭제

---

# 🛠 Tech Stack

## Backend

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Validation
- Lombok

## Frontend

- React
- React Router
- Fetch API
- CSS

## Tools

- Git
- GitHub
- IntelliJ IDEA
- Swagger

---

# 📂 프로젝트 구조

```
Backend
├── controller
├── service
├── repository
├── mapper
├── dto
├── entity
├── exception

Frontend
├── api
├── assets
├── components
├── pages
├── styles
```

---

# 📡 API

## 주문

| Method | URL |
|---------|-----|
| POST | /api/orders/create |
| GET | /api/orders |
| POST | /api/orders/cancel/{id} |

## 관리자 주문

| Method | URL |
|---------|-----|
| GET | /api/orders/admin |
| GET | /api/orders/admin/{id} |
| DELETE | /api/orders/delete/{id} |

## 메뉴

| Method | URL |
|---------|-----|
| GET | /api/menus |
| GET | /api/menus/{id} |

## 관리자 메뉴

| Method | URL |
|---------|-----|
| POST | /api/admin/menus |
| PUT | /api/admin/menus/{id} |
| PATCH | /api/admin/menus/{id}/activate |
| PATCH | /api/admin/menus/{id}/deactivate |
| DELETE | /api/admin/menus/{id} |

---



# 📸 화면

## 고객

- 메뉴 목록
- 메뉴 상세
- 주문하기
- 주문 조회

## 관리자

- 메뉴 관리
- 메뉴 등록
- 주문 관리
- 주문 상세

---

# 🔥 구현 포인트

### 📷 메뉴 이미지 업로드

- MultipartFile 업로드
- UUID 파일명 저장
- 기본 이미지 지원
- 이미지 미리보기

---

### 📦 주문 병합

동일한

- 이메일
- 우편번호
- 주소

그리고

- **PENDING 상태**

인 주문은 하나의 주문으로 병합됩니다.

동일 메뉴는 수량만 증가하며,
다른 메뉴는 새로운 주문 항목으로 추가됩니다.

---

### 📋 Validation

- Bean Validation
- GlobalExceptionHandler
- 공통 ErrorResponse
- Field Error 지원

---

### ☕ 메뉴 관리

- Soft Delete
- 최근 삭제된 메뉴에서 Hard Delete 가능
- 활성 / 비활성 관리
- 이미지 변경 지원

---

### 🛒 주문 관리

- 이메일 조회
- 관리자 전체 조회
- 주문 취소
- 주문 삭제

---

# 📌 향후 개선 사항

- 동시성 제어
- JWT 로그인
- 관리자 권한 관리

---

# 👨‍💻 Team

Programmers Backend DevCourse Team Project
