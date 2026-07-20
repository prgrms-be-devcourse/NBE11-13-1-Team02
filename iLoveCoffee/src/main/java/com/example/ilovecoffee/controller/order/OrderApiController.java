package com.example.ilovecoffee.controller.order;

import com.example.ilovecoffee.constant.PathConstant;
import com.example.ilovecoffee.dto.order.request.OrderRequest;
import com.example.ilovecoffee.dto.order.response.OrderResponse;
import com.example.ilovecoffee.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstant.API_HOME)
public class OrderApiController {
    private final OrderService orderService;

    @PostMapping(PathConstant.CREATE)
    public ResponseEntity<OrderResponse> create(
            @RequestBody
            OrderRequest request
    ) {
        var response = orderService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(PathConstant.FIND_ALL)
    public ResponseEntity<List<OrderResponse>> findAll() {
        var responses = orderService.findAll();

        return ResponseEntity.ok(responses);
    }


}
