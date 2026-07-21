package com.example.ilovecoffee.controller.order;

import com.example.ilovecoffee.dto.order.request.OrderRequest;
import com.example.ilovecoffee.dto.order.response.OrderResponse;
import com.example.ilovecoffee.service.order.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class ApiOrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request
    ) {
        OrderResponse response = orderService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrdersByEmail(
            @RequestParam
            @NotBlank
            @Email
            String email
    ) {
        return ResponseEntity.ok(orderService.findByEmail(email));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponse>> getOrdersForAdmin(
            @RequestParam(required = false) String email
    ) {
        List<OrderResponse> responses =
                email == null || email.isBlank()
                        ? orderService.findAll()
                        : orderService.findByEmail(email);

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long id
    ) {
        orderService.cancelOrder(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrderById(
            @PathVariable Long id
    ) {
        orderService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<Void> deleteOrdersByEmail(
            @RequestParam
            @NotBlank
            @Email
            String email
    ) {
        orderService.deleteAllByEmail(email);

        return ResponseEntity.noContent().build();
    }
}