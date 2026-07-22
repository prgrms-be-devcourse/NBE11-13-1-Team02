package com.example.ilovecoffee.domain.entity.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_review_menu_email",
                        columnNames = {"menu_id", "email"}
                )
        }
)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    private int rating;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;
    @Column(name = "menu_version", nullable = false)
    private Long menuVersion;
    @Column(name = "email", nullable = false)
    private String email;

    @Column(length = 1000)
    private String content;
    private LocalDateTime createdAt;

    public static Review create(
            long menuId,
            long version,
            String email,
            int rating,
            String content
    ) {
        return Review.builder()
                .menuId(menuId)
                .menuVersion(version)
                .email(email)
                .rating(rating)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
