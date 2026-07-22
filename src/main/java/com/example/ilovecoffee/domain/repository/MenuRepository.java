package com.example.ilovecoffee.domain.repository;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.domain.enums.MenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStatusNot(MenuStatus status);   // 고객용 목록 조회 (DELETED 제외)
    Optional<Menu> findByIdAndStatusNot(Long id, MenuStatus status);    // 고객용 상세 조회
    List<Menu> findAllByStatus(MenuStatus status);// 관리자 휴지통 조회
    List<Menu> findAllByStockAndStatusNot(int stock, MenuStatus status);
}
