package com.example.ilovecoffee.domain.entity.order;

import com.example.ilovecoffee.domain.enums.OrderStatus;
import com.example.ilovecoffee.domain.enums.ShipmentStatus;
import jakarta.persistence.*;
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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String postNumber;
    private String address;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private long totalPrice;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    private LocalDateTime orderAt = null;
    private LocalDateTime dispatchAt = null;
    private LocalDateTime deliveredAt = null;

    public void confirm() {
        this.orderStatus = OrderStatus.CONFIRMED;
        this.orderAt = LocalDateTime.now();
    }

    public void prepare() {
        this.orderStatus = OrderStatus.PREPARING;
        this.shipmentStatus = ShipmentStatus.PENDING;
    }

    public void dispatch() {
        this.orderStatus = OrderStatus.SHIPPED;
        this.shipmentStatus = ShipmentStatus.SHIPPING;
        this.dispatchAt = LocalDateTime.now();
    }

    public void delivered() {
        this.orderStatus = OrderStatus.COMPLETED;
        this.shipmentStatus = ShipmentStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

}
