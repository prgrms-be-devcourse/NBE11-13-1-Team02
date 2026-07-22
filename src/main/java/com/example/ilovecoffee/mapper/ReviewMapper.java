package com.example.ilovecoffee.mapper;

import com.example.ilovecoffee.domain.entity.review.Review;
import com.example.ilovecoffee.dto.review.response.AdminReviewResponse;
import com.example.ilovecoffee.dto.review.response.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .menuId(review.getMenuId())
                .menuVersion(review.getMenuVersion())
                .email(maskEmail(review.getEmail()))
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public AdminReviewResponse toAdminReviewResponse(Review review) {
        return AdminReviewResponse.builder()
                .id(review.getId())
                .menuId(review.getMenuId())
                .menuVersion(review.getMenuVersion())
                .email(review.getEmail())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');

        if(atIndex < 0) return email;

        String local = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if(local.length() <= 2) {
            return local + "***" + domain;
        }
        return local.substring(0, 2) + "***" + domain;
    }
}
