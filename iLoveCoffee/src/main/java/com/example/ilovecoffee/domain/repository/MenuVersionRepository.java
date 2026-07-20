package com.example.ilovecoffee.domain.repository;

import com.example.ilovecoffee.domain.entity.menu.MenuVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuVersionRepository extends JpaRepository<MenuVersion, Long> {

}