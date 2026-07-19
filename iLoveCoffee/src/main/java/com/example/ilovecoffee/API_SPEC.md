# ☕ I Love Coffee API Specification

> **Draft v0.1**\
> 본 문서는 Entity, DTO, Enum 기준의 데이터 명세입니다.\
> HTTP Method, URI, Status Code는 별도 API 명세에서 정의합니다.

------------------------------------------------------------------------

# Table of Contents

1.  Common Rules
2.  Menu
    -   Entity
    -   AdminMenuCreateRequest
    -   AdminMenuUpdateRequest
    -   MenuResponse
    -   AdminMenuResponse
3.  Order
    -   Entity
    -   OrderItem Entity
    -   OrderRequest
    -   OrderItemRequest
    -   OrderResponse
    -   OrderItemResponse
4.  Enums
    -   MenuStatus
    -   OrderStatus
    -   ShipmentStatus
5.  Domain Rules

------------------------------------------------------------------------

# Common Rules

## Date Format

모든 날짜/시간은 아래 형식으로 직렬화합니다.

``` text
yyyy-MM-dd HH:mm:ss
```

------------------------------------------------------------------------

# Menu

```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}Menu Entity`</strong>`{=html}
```{=html}
</summary>
```
  Field         Type            Description
  ------------- --------------- --------------
  id            Long            메뉴 고유 ID
  name          String          메뉴명
  description   String          메뉴 설명
  price         long            메뉴 가격
  stock         int             재고
  status        MenuStatus      메뉴 상태
  createdAt     LocalDateTime   생성 일시
  updatedAt     LocalDateTime   수정 일시
  deletedAt     LocalDateTime   삭제 일시

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}AdminMenuCreateRequest`</strong>`{=html}
```{=html}
</summary>
```
  Field         Type      Required  Description
  ------------- -------- ---------- -------------
  name          String       ✅     메뉴명
  description   String       ✅     메뉴 설명
  price         long         ✅     메뉴 가격
  stock         int          ✅     초기 재고

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}AdminMenuUpdateRequest`</strong>`{=html}
```{=html}
</summary>
```
  Field         Type      Required  Description
  ------------- -------- ---------- -------------
  name          String       ✅     메뉴명
  description   String       ✅     메뉴 설명
  price         long         ✅     메뉴 가격
  stock         int          ✅     재고

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}MenuResponse`</strong>`{=html}
```{=html}
</summary>
```
  Field         Type      Description
  ------------- --------- -------------
  id            Long      메뉴 ID
  name          String    메뉴명
  description   String    메뉴 설명
  price         long      가격
  stock         int       재고
  soldOut       boolean   품절 여부

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}AdminMenuResponse`</strong>`{=html}
```{=html}
</summary>
```
  Field         Type            Description
  ------------- --------------- -------------
  id            Long            메뉴 ID
  name          String          메뉴명
  description   String          메뉴 설명
  price         long            가격
  stock         int             재고
  status        MenuStatus      메뉴 상태
  createdAt     LocalDateTime   생성 일시
  updatedAt     LocalDateTime   수정 일시
  deletedAt     LocalDateTime   삭제 일시

```{=html}
</details>
```

------------------------------------------------------------------------

# Order

```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}Order Entity`</strong>`{=html}
```{=html}
</summary>
```
  Field            Type                Description
  ---------------- ------------------- ----------------
  id               Long                주문 ID
  email            String              주문자 이메일
  postNumber       String              우편번호
  address          String              배송 주소
  shipmentStatus   ShipmentStatus      배송 상태
  orderStatus      OrderStatus         주문 상태
  totalPrice       long                총 주문 금액
  items            List\<OrderItem\>   주문 상품 목록
  orderAt          LocalDateTime       주문 일시
  dispatchAt       LocalDateTime       출고 일시
  deliveredAt      LocalDateTime       배송 완료 일시

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}OrderItem Entity`</strong>`{=html}
```{=html}
</summary>
```
  Field      Type     Description
  ---------- -------- ------------------
  id         Long     주문 상품 ID
  menuId     Long     메뉴 ID
  name       String   주문 당시 메뉴명
  price      long     주문 당시 가격
  quantity   int      주문 수량

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}OrderRequest`</strong>`{=html}
```{=html}
</summary>
```
  Field        Type                        Required  Description
  ------------ -------------------------- ---------- ----------------
  email        String                         ✅     주문자 이메일
  postNumber   String                         ✅     우편번호
  address      String                         ✅     배송 주소
  items        List\<OrderItemRequest\>       ✅     주문 상품 목록

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}OrderItemRequest`</strong>`{=html}
```{=html}
</summary>
```
  Field      Type   Validation   Description
  ---------- ------ ------------ -------------
  menuId     Long   NotNull      메뉴 ID
  quantity   int    Min(1)       주문 수량

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}OrderResponse`</strong>`{=html}
```{=html}
</summary>
```
  Field            Type                        Description
  ---------------- --------------------------- ----------------
  id               Long                        주문 ID
  email            String                      주문자 이메일
  address          String                      배송 주소
  postNumber       String                      우편번호
  items            List\<OrderItemResponse\>   주문 상품 목록
  totalPrice       long                        총 주문 금액
  orderStatus      OrderStatus                 주문 상태
  shipmentStatus   ShipmentStatus              배송 상태
  orderAt          LocalDateTime               주문 일시
  dispatchAt       LocalDateTime               출고 일시
  deliveredAt      LocalDateTime               배송 완료 일시

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}OrderItemResponse`</strong>`{=html}
```{=html}
</summary>
```
  Field      Type     Description
  ---------- -------- ------------------
  menuId     Long     메뉴 ID
  name       String   메뉴명
  price      long     단가
  quantity   int      수량
  subtotal   long     price × quantity

```{=html}
</details>
```

------------------------------------------------------------------------

# Enums

```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}MenuStatus`</strong>`{=html}
```{=html}
</summary>
```
  Value      Description
  ---------- -------------
  ACTIVE     판매 중
  INACTIVE   판매 중지
  DELETED    삭제

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}OrderStatus`</strong>`{=html}
```{=html}
</summary>
```
  Value       Description
  ----------- --------------
  CONFIRMED   주문 확정
  PAID        결제 완료
  PREPARING   상품 준비 중
  SHIPPED     출고 완료
  COMPLETED   배송 완료
  CANCELED    주문 취소
  REFUNDED    환불 완료

```{=html}
</details>
```
```{=html}
<details>
```
```{=html}
<summary>
```
`<strong>`{=html}ShipmentStatus`</strong>`{=html}
```{=html}
</summary>
```
  Value        Description
  ------------ -------------
  PENDING      대기
  SHIPPING     배송 중
  DELIVERED    배송 완료
  FAILED       배송 실패
  RESHIPPING   재배송
  RETURNING    반품 중
  RETURNED     반품 완료

```{=html}
</details>
```

------------------------------------------------------------------------

# Domain Rules

## Soft Delete

-   Menu는 Soft Delete를 사용합니다.
-   삭제 시 status = DELETED
-   deletedAt 기록

## Snapshot Policy

OrderItem은 주문 시점의 메뉴 정보를 저장합니다.

-   menuId
-   name
-   price

메뉴가 수정되어도 기존 주문 내역은 변경되지 않습니다.

## Calculated Fields

  Field      Formula
  ---------- ------------------
  subtotal   price × quantity
