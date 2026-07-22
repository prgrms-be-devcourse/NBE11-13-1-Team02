package com.example.ilovecoffee.service.component;

import com.example.ilovecoffee.exception.UnauthorizedException;
import com.example.ilovecoffee.service.admin.AdminAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final AdminAuthService adminAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession(false);

        if (!adminAuthService.isAdmin(session)) {
            throw new UnauthorizedException("관리자 로그인이 필요합니다.");
        }
        return true;
    }

}
