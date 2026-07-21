package com.example.ilovecoffee.controller.order;

import com.example.ilovecoffee.dto.order.request.OrderRequest;
import com.example.ilovecoffee.dto.order.response.OrderResponse;
import com.example.ilovecoffee.service.order.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class ApiOrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody
            @Valid
            OrderRequest request
    ) {
        var response = orderService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrdersByEmail(
            @RequestParam
            @Valid
            @NotBlank
            String email
    ) {
        var responses = orderService.findByEmail(email);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponse>> getOrdersForAdmin(
            @RequestParam(required = false)
            String email
    ) {
        var responses = (email == null || email.isBlank())
                ? orderService.findAll()
                : orderService.findByEmail(email);

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable
            Long id
    ) {
        orderService.cancelOrder(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrderById(
            @PathVariable
            Long id
    ) {
        orderService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<Void> deleteOrderByEmail(
            @RequestParam
            @Valid
            @NotBlank
            String email
    ) {
        orderService.deleteAllByEmail(email);

        return ResponseEntity.noContent().build();
    }
}
