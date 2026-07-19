# ☕ I Love Coffee Data Specification

> **Draft v1.0**  
> 본 문서는 Entity, DTO, Enum 기준의 데이터 구조 명세입니다.  
> HTTP Method, URI, Status Code는 팀 회의 후 별도 API 명세에서 정의합니다.

---

## 목차

- [공통 규칙](#공통-규칙)
- [Menu](#menu)
- [Order](#order)
- [Enums](#enums)
- [Domain Rules](#domain-rules)

---

# 공통 규칙

## 날짜 형식

`@JsonFormat`이 적용된 모든 날짜 및 시간 필드는 아래 형식으로 응답합니다.

```text
yyyy-MM-dd HH:mm:ss
```

## 문서 범위

이 문서에서는 다음 항목만 정의합니다.

- Entity 필드 구조
- Request DTO 구조 및 검증 규칙
- Response DTO 구조
- Enum 값
- 주요 도메인 규칙

다음 항목은 팀 회의 후 별도 정의합니다.

- HTTP Method
- URI
- HTTP Status Code
- API별 요청 및 응답 흐름

---

# Menu

<details>
<summary><strong>Menu Entity</strong></summary>

## 설명

메뉴 정보와 판매 상태를 관리하는 엔티티입니다.

| Field | Type | Description |
|---|---|---|
| id | Long | 메뉴 고유 ID |
| name | String | 메뉴명 |
| description | String | 메뉴 설명 |
| price | long | 메뉴 가격 |
| stock | int | 현재 재고 수량 |
| status | MenuStatus | 메뉴 상태 |
| createdAt | LocalDateTime | 메뉴 생성 일시 |
| updatedAt | LocalDateTime | 메뉴 수정 일시 |
| deletedAt | LocalDateTime | 메뉴 삭제 일시 |

## 주요 동작

| Method | Description |
|---|---|
| delete() | 상태를 `DELETED`로 변경하고 삭제 일시를 기록 |
| update(...) | 메뉴 정보와 상태를 수정하고 수정 일시를 기록 |
| activate() | 상태를 `ACTIVE`로 변경하고 삭제 일시를 초기화 |
| deactivate() | `ACTIVE` 상태인 메뉴를 `INACTIVE`로 변경 |

</details>

<details>
<summary><strong>AdminMenuCreateRequest DTO</strong></summary>

## 설명

관리자가 메뉴를 생성할 때 사용하는 요청 DTO입니다.

| Field | Type | Required | Description |
|---|---|:---:|---|
| name | String | O | 메뉴명 |
| description | String | O | 메뉴 설명 |
| price | long | O | 메뉴 가격 |
| stock | int | O | 초기 재고 수량 |

### JSON 예시

```json
{
  "name": "Americano",
  "description": "진한 에스프레소와 물로 만든 커피",
  "price": 4500,
  "stock": 100
}
```

</details>

<details>
<summary><strong>AdminMenuUpdateRequest DTO</strong></summary>

## 설명

관리자가 기존 메뉴 정보를 수정할 때 사용하는 요청 DTO입니다.

| Field | Type | Required | Description |
|---|---|:---:|---|
| name | String | O | 메뉴명 |
| description | String | O | 메뉴 설명 |
| price | long | O | 메뉴 가격 |
| stock | int | O | 재고 수량 |

### JSON 예시

```json
{
  "name": "Cafe Latte",
  "description": "에스프레소와 스팀 밀크로 만든 커피",
  "price": 5000,
  "stock": 80
}
```

</details>

<details>
<summary><strong>MenuResponse DTO</strong></summary>

## 설명

고객이 메뉴를 조회할 때 사용하는 응답 DTO입니다.

| Field | Type | Description |
|---|---|---|
| id | Long | 메뉴 고유 ID |
| name | String | 메뉴명 |
| description | String | 메뉴 설명 |
| price | long | 메뉴 가격 |
| stock | int | 현재 재고 수량 |
| soldOut | boolean | 품절 여부 |

### JSON 예시

```json
{
  "id": 1,
  "name": "Americano",
  "description": "진한 에스프레소와 물로 만든 커피",
  "price": 4500,
  "stock": 100,
  "soldOut": false
}
```

### 비고

- 내부 관리 상태인 `MenuStatus`는 고객 응답에 직접 노출하지 않습니다.
- 고객에게 필요한 품절 여부만 `soldOut`으로 제공합니다.

</details>

<details>
<summary><strong>AdminMenuResponse DTO</strong></summary>

## 설명

관리자가 메뉴 정보를 조회할 때 사용하는 응답 DTO입니다.

| Field | Type | Description |
|---|---|---|
| id | Long | 메뉴 고유 ID |
| name | String | 메뉴명 |
| description | String | 메뉴 설명 |
| price | long | 메뉴 가격 |
| stock | int | 현재 재고 수량 |
| status | MenuStatus | 메뉴 상태 |
| createdAt | LocalDateTime | 메뉴 생성 일시 |
| updatedAt | LocalDateTime | 메뉴 수정 일시 |
| deletedAt | LocalDateTime | 메뉴 삭제 일시 |

### JSON 예시

```json
{
  "id": 1,
  "name": "Americano",
  "description": "진한 에스프레소와 물로 만든 커피",
  "price": 4500,
  "stock": 100,
  "status": "ACTIVE",
  "createdAt": "2026-07-19 10:30:15",
  "updatedAt": "2026-07-19 12:00:00",
  "deletedAt": null
}
```

</details>

---

# Order

<details>
<summary><strong>Order Entity</strong></summary>

## 설명

주문 정보, 주문 상태, 배송 상태 및 주문 상품 목록을 관리하는 엔티티입니다.

| Field | Type | Description |
|---|---|---|
| id | Long | 주문 고유 ID |
| email | String | 주문자 이메일 |
| postNumber | String | 배송지 우편번호 |
| address | String | 배송 주소 |
| shipmentStatus | ShipmentStatus | 배송 상태 |
| orderStatus | OrderStatus | 주문 상태 |
| totalPrice | long | 총 주문 금액 |
| items | List&lt;OrderItem&gt; | 주문 상품 목록 |
| orderAt | LocalDateTime | 주문 확정 일시 |
| dispatchAt | LocalDateTime | 출고 일시 |
| deliveredAt | LocalDateTime | 배송 완료 일시 |

## 연관관계

- `Order` 1 : N `OrderItem`
- `CascadeType.ALL`
- `orphanRemoval = true`
- `order_id` 외래 키를 사용하여 주문 상품을 연결

## 주요 동작

| Method | OrderStatus | ShipmentStatus | Timestamp |
|---|---|---|---|
| confirm() | CONFIRMED | 변경 없음 | orderAt 기록 |
| prepare() | PREPARING | PENDING | 변경 없음 |
| dispatch() | SHIPPED | SHIPPING | dispatchAt 기록 |
| delivered() | COMPLETED | DELIVERED | deliveredAt 기록 |

</details>

<details>
<summary><strong>OrderItem Entity</strong></summary>

## 설명

주문에 포함된 개별 상품 정보를 주문 시점 기준으로 저장하는 엔티티입니다.

| Field | Type | Description |
|---|---|---|
| id | Long | 주문 상품 고유 ID |
| menuId | Long | 주문 당시 메뉴 ID |
| name | String | 주문 당시 메뉴명 |
| price | long | 주문 당시 메뉴 가격 |
| quantity | int | 주문 수량 |

### 비고

- `Menu` 엔티티를 직접 참조하지 않습니다.
- 주문 당시의 메뉴 정보를 스냅샷으로 저장합니다.
- 이후 메뉴명이나 가격이 변경되어도 기존 주문 내역은 유지됩니다.

</details>

<details>
<summary><strong>OrderRequest DTO</strong></summary>

## 설명

주문을 생성할 때 사용하는 요청 DTO입니다.

| Field | Type | Required | Validation | Description |
|---|---|:---:|---|---|
| email | String | O | NotBlank, Email | 주문자 이메일 |
| postNumber | String | O | NotBlank | 배송지 우편번호 |
| address | String | O | NotBlank | 배송 주소 |
| items | List&lt;OrderItemRequest&gt; | O | NotEmpty, Valid | 주문 상품 목록 |

### JSON 예시

```json
{
  "email": "user@example.com",
  "postNumber": "12345",
  "address": "서울특별시 강남구 테헤란로 123",
  "items": [
    {
      "menuId": 1,
      "quantity": 2
    },
    {
      "menuId": 2,
      "quantity": 1
    }
  ]
}
```

### 검증 규칙

- 이메일은 비어 있을 수 없으며 이메일 형식이어야 합니다.
- 우편번호와 주소는 비어 있을 수 없습니다.
- 주문 상품 목록은 최소 1개 이상이어야 합니다.
- `@Valid`에 의해 각 `OrderItemRequest`도 중첩 검증합니다.

</details>

<details>
<summary><strong>OrderItemRequest DTO</strong></summary>

## 설명

주문에 포함할 개별 상품과 수량을 전달하는 요청 DTO입니다.

| Field | Type | Required | Validation | Description |
|---|---|:---:|---|---|
| menuId | Long | O | NotNull | 주문할 메뉴 ID |
| quantity | int | O | Min(1) | 주문 수량 |

### JSON 예시

```json
{
  "menuId": 1,
  "quantity": 2
}
```

### 검증 규칙

- 메뉴 ID는 `null`일 수 없습니다.
- 주문 수량은 1개 이상이어야 합니다.

</details>

<details>
<summary><strong>OrderResponse DTO</strong></summary>

## 설명

주문 정보를 반환할 때 사용하는 최상위 응답 DTO입니다.

| Field | Type | Description |
|---|---|---|
| id | Long | 주문 고유 ID |
| email | String | 주문자 이메일 |
| address | String | 배송 주소 |
| postNumber | String | 배송지 우편번호 |
| items | List&lt;OrderItemResponse&gt; | 주문 상품 목록 |
| totalPrice | long | 주문 전체 금액 |
| orderStatus | OrderStatus | 주문 상태 |
| shipmentStatus | ShipmentStatus | 배송 상태 |
| orderAt | LocalDateTime | 주문 확정 일시 |
| dispatchAt | LocalDateTime | 출고 일시 |
| deliveredAt | LocalDateTime | 배송 완료 일시 |

### JSON 예시

```json
{
  "id": 1,
  "email": "user@example.com",
  "address": "서울특별시 강남구 테헤란로 123",
  "postNumber": "12345",
  "items": [
    {
      "menuId": 1,
      "name": "Americano",
      "price": 4500,
      "quantity": 2,
      "subtotal": 9000
    },
    {
      "menuId": 2,
      "name": "Cafe Latte",
      "price": 5000,
      "quantity": 1,
      "subtotal": 5000
    }
  ],
  "totalPrice": 14000,
  "orderStatus": "PREPARING",
  "shipmentStatus": "PENDING",
  "orderAt": "2026-07-19 14:30:00",
  "dispatchAt": null,
  "deliveredAt": null
}
```

</details>

<details>
<summary><strong>OrderItemResponse DTO</strong></summary>

## 설명

주문에 포함된 개별 상품 정보를 반환하는 응답 DTO입니다.

| Field | Type | Description |
|---|---|---|
| menuId | Long | 주문 당시 메뉴 ID |
| name | String | 주문 당시 메뉴명 |
| price | long | 주문 당시 단가 |
| quantity | int | 주문 수량 |
| subtotal | long | 해당 상품의 합계 금액 |

### JSON 예시

```json
{
  "menuId": 1,
  "name": "Americano",
  "price": 4500,
  "quantity": 2,
  "subtotal": 9000
}
```

### 계산식

```text
subtotal = price × quantity
```

</details>

---

# Enums

<details>
<summary><strong>MenuStatus</strong></summary>

| Value | Description |
|---|---|
| ACTIVE | 판매 중 |
| INACTIVE | 판매 중지 |
| DELETED | 삭제 |

</details>

<details>
<summary><strong>OrderStatus</strong></summary>

| Value | Description |
|---|---|
| CONFIRMED | 주문 확정 |
| PAID | 결제 완료 |
| PREPARING | 상품 준비 중 |
| COMPLETED | 상품 배송 완료 |
| SHIPPED | 출고 완료 |
| CANCELED | 주문 취소 |
| REFUNDED | 환불 완료 |

</details>

<details>
<summary><strong>ShipmentStatus</strong></summary>

| Value | Description |
|---|---|
| PENDING | 대기 |
| SHIPPING | 배송 중 |
| DELIVERED | 배송 완료 |
| FAILED | 배송 실패 |
| RESHIPPING | 재배송 |
| RETURNING | 반품 중 |
| RETURNED | 반품 완료 |

</details>

---

# Domain Rules

## Menu Soft Delete

메뉴 삭제 시 데이터베이스에서 즉시 제거하지 않고 논리 삭제합니다.

```text
status = DELETED
deletedAt = 현재 시간
```

삭제된 메뉴를 다시 활성화하면 다음과 같이 변경합니다.

```text
status = ACTIVE
deletedAt = null
```

## OrderItem Snapshot Policy

주문 생성 시 메뉴의 현재 정보를 `OrderItem`에 복사하여 저장합니다.

```text
menuId
name
price
quantity
```

따라서 메뉴명이나 가격이 변경되어도 기존 주문 내역은 변경되지 않습니다.

## Calculated Fields

다음 값은 Entity에 별도 저장하지 않고 응답 DTO 변환 시 계산합니다.

| Field | Formula |
|---|---|
| subtotal | price × quantity |


## 상태 변경 규칙

주문 및 메뉴 상태를 변경할 때 외부 계층에서 Enum 값을 직접 지정하거나 필드를 직접 변경하지 않습니다.

상태 변경은 각 Entity가 제공하는 도메인 메서드를 통해 수행합니다.

### Menu

```java
menu.activate();
menu.deactivate();
menu.delete();
```

### Order

```java
order.confirm();
order.prepare();
order.dispatch();
order.delivered();
```

이를 통해 상태 변경과 함께 필요한 시간 값 및 연관 상태가 일관되게 변경되도록 합니다.

예를 들어 주문 출고 시 `OrderStatus.SHIPPED`와 `ShipmentStatus.SHIPPING`을 각각 직접 지정하는 대신 다음 메서드를 호출합니다.

```java
order.dispatch();
```

`dispatch()`는 다음 변경을 한 번에 처리합니다.

```text
orderStatus = SHIPPED
shipmentStatus = SHIPPING
dispatchAt = 현재 시간
```

> DTO는 요청과 응답 데이터를 전달하는 역할을 담당하며, 실제 상태 변경은 Entity의 도메인 메서드가 담당합니다.

## Order Relationship

```text
Order
└── List<OrderItem>
```

주문에 포함된 상품은 주문의 하위 엔티티로 관리합니다.
