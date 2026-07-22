package com.example.ilovecoffee.service.review;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.domain.entity.review.Review;
import com.example.ilovecoffee.domain.repository.MenuRepository;
import com.example.ilovecoffee.domain.repository.ReviewRepository;
import com.example.ilovecoffee.dto.review.request.ReviewCreateRequest;
import com.example.ilovecoffee.dto.review.response.AdminReviewResponse;
import com.example.ilovecoffee.dto.review.response.ReviewResponse;
import com.example.ilovecoffee.exception.DuplicateReviewException;
import com.example.ilovecoffee.exception.MenuNotFoundException;
import com.example.ilovecoffee.exception.ReviewNotFound;
import com.example.ilovecoffee.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse create(
            Long menuId,
            ReviewCreateRequest request
    ) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        if(reviewRepository.existsByMenuIdAndEmail(menuId, request.email())) {
            throw new DuplicateReviewException();
        }

        var review = Review.create(
                menu.getId(),
                menu.getName(),
                menu.getVersion(),
                request.email(),
                request.rating(),
                normalizeContent(request.content())
        );
        var saved = reviewRepository.save(review);

        return reviewMapper.toReviewResponse(saved);
    }

    public List<ReviewResponse> findAllByMenuId(Long menuId) {
        if(!menuRepository.existsById(menuId)) {
            throw new MenuNotFoundException();
        }

        return reviewRepository.findAllByMenuIdOrderByCreatedAtDesc(menuId)
                .stream()
                .map(reviewMapper::toReviewResponse)
                .toList();
    }

    public List<AdminReviewResponse> findAllForAdmin() {
        return reviewRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(reviewMapper::toAdminReviewResponse)
                .toList();
    }

    @Transactional
    public void deleteByAdmin(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFound::new);
        reviewRepository.delete(review);
    }

    private String normalizeContent(String content) {
        if (content == null || content.isBlank()) {
            return null;
        }
        return content.trim();
    }
}
