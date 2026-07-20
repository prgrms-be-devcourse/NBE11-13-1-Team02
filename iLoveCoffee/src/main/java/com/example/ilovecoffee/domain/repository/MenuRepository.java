package com.example.ilovecoffee.domain.repository;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
