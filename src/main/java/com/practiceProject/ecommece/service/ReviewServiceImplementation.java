package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.entity.Product;
import com.practiceProject.ecommece.entity.Review;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.repository.ProductRepository;
import com.practiceProject.ecommece.repository.ReviewRepository;
import com.practiceProject.ecommece.request.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewServiceImplementation implements ReviewService{

    private ReviewRepository reviewRepository;
    private ProductService productService;
    private ProductRepository productRepository;

    @Autowired
    public ReviewServiceImplementation(ReviewRepository reviewRepository, ProductService productService, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Override
    public Review createReview(ReviewRequest request, User user) throws ProductException {

        Product product = productService.findProductById(request.getProductId());

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setReview(request.getReview());
        review.getCreatedAt(LocalDate.from(LocalDateTime.now()));

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllProductReview(Long productId) {

        return reviewRepository.getAllProductReview(productId);

    }
}
