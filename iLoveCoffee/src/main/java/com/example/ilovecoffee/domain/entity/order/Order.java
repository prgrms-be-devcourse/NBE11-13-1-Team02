package com.example.ilovecoffee.domain.entity.order;

import com.example.ilovecoffee.domain.enums.OrderStatus;
import com.example.ilovecoffee.domain.enums.ShipmentStatus;
import com.example.ilovecoffee.exception.AlreadyCanceledOrderException;
import com.example.ilovecoffee.exception.OrderDeletionNotAllowedException;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String postNumber;
    private String address;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ShipmentStatus shipmentStatus = ShipmentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;

    private long totalPrice;
    @Builder.Default
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();
    public void addItem(OrderItem newItem) {
        OrderItem existingItem = items.stream()
                .filter(item -> item.isSameMenu(newItem.getMenuId()))
                .findFirst()
                .orElse(null);
        if (existingItem != null) {
            existingItem.increaseQuantity(newItem.getQuantity());
        } else {
            items.add(newItem);
            newItem.assignOrder(this);
        }
        totalPrice += newItem.calculateSubtotal();
    }

    private LocalDateTime orderAt = null;
    private LocalDateTime dispatchAt = null;
    private LocalDateTime deliveredAt = null;

    public void suspend() {
        this.orderStatus = OrderStatus.PENDING;
        this.shipmentStatus = ShipmentStatus.PENDING;
        this.orderAt = LocalDateTime.now();
    }

    public void confirm() {
        this.orderStatus = OrderStatus.CONFIRMED;
        this.shipmentStatus = ShipmentStatus.PENDING;
    }

    public void prepare() {
        this.orderStatus = OrderStatus.PREPARING;
        this.shipmentStatus = ShipmentStatus.PENDING;
    }

    public void dispatch() {
        this.orderStatus = OrderStatus.DISPATCHED;
        this.shipmentStatus = ShipmentStatus.SHIPPING;
        this.dispatchAt = LocalDateTime.now();
    }

    public void deliver() {
        this.orderStatus = OrderStatus.COMPLETED;
        this.shipmentStatus = ShipmentStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void cancel() {
        validateCancelable();
        this.orderStatus = OrderStatus.CANCELED;
    }

    private void validateCancelable() {
        if(this.orderStatus == OrderStatus.CANCELED)
            throw new AlreadyCanceledOrderException();
        if(!this.shipmentStatus.canCancel())
            throw new OrderDeletionNotAllowedException();
    }

    public void validateDeletable() {
        if(!orderStatus.canDelete())
            throw new OrderDeletionNotAllowedException();
    }

    public static Order create(
            String email,
            String postNumber,
            String address
    ) {
        return Order.builder()
                .email(email)
                .postNumber(postNumber)
                .address(address)
                .orderStatus(OrderStatus.PENDING)
                .shipmentStatus(ShipmentStatus.PENDING)
                .orderAt(LocalDateTime.now())
                .totalPrice(0L)
                .build();
    }

}
