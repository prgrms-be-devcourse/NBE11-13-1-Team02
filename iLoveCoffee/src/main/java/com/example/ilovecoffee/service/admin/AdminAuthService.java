package com.example.ilovecoffee.service.admin;

import com.example.ilovecoffee.dto.admin.request.AdminLoginRequest;
import com.example.ilovecoffee.exception.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminAuthService {

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    public void login(AdminLoginRequest request, HttpSession session) {
        if (!adminUsername.equals(request.username()) || !adminPassword.equals(request.password())) {
            log.warn("관리자 로그인 실패: username={}", request.username());
            throw new UnauthorizedException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        session.setAttribute("isAdmin", true);
        log.info("관리자 로그인 성공: username={}", request.username());
    }

    public boolean isAdmin(HttpSession session) {
        return session != null && Boolean.TRUE.equals(session.getAttribute("isAdmin"));
    }
}
