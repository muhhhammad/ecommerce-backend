package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.entity.Product;
import com.practiceProject.ecommece.entity.Rating;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.repository.RatingRepository;
import com.practiceProject.ecommece.request.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RatingServiceImplementation implements RatingService{

    private RatingRepository ratingRepository;
    private ProductService productService;

    @Autowired
    public RatingServiceImplementation(RatingRepository ratingRepository, ProductService productService) {
        this.ratingRepository = ratingRepository;
        this.productService = productService;
    }

    @Override
    public Rating createRating(RatingRequest requests, User user) throws ProductException {

        Product product = productService.findProductById(requests.getProductId());

        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(requests.getRating());
        rating.setCreatedAt(LocalDate.from(LocalDateTime.now()));

        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductRating(Long productId) {

        return ratingRepository.getAllProductRating(productId);
    }
}
