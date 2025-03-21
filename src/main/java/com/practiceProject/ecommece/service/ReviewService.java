package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.entity.Review;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest request, User user) throws ProductException;

    public List<Review> getAllProductReview(Long productId);

}
