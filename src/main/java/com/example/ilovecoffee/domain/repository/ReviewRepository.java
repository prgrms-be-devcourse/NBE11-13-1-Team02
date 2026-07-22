package com.example.ilovecoffee.domain.repository;

import com.example.ilovecoffee.domain.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByMenuIdAndEmail(long id, String email);
    List<Review> findAllByOrderByCreatedAtDesc();
    List<Review> findAllByMenuIdOrderByCreatedAtDesc(long id);
}
