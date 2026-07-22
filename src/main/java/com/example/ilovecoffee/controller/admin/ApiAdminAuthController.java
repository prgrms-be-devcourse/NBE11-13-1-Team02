package com.example.ilovecoffee.controller.admin;

import com.example.ilovecoffee.dto.admin.request.AdminLoginRequest;
import com.example.ilovecoffee.service.admin.AdminAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/auth")
public class ApiAdminAuthController {
    private final AdminAuthService adminAuthService;
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid
            @RequestBody
            AdminLoginRequest request,
            HttpServletRequest httpRequest
    ) {
        HttpSession session = httpRequest.getSession(true);
        adminAuthService.login(request, session);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest httpRequest
    ) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> status(
            HttpServletRequest httpRequest
    ) {
        HttpSession session = httpRequest.getSession(false);

        return ResponseEntity.ok(adminAuthService.isAdmin(session));
    }
}