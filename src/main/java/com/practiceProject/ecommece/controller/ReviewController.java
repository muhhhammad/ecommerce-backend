package com.practiceProject.ecommece.controller;

import com.practiceProject.ecommece.entity.Review;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.request.ReviewRequest;
import com.practiceProject.ecommece.service.ReviewService;
import com.practiceProject.ecommece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;
    private UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }


    @PostMapping("/create")
    public ResponseEntity<Review> createRating(@RequestBody ReviewRequest request,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {

        User user = userService.findUserProfileByJwt(jwt); // Retrieve the user profile using the JWT token
        Review review = reviewService.createReview(request, user); // Create a new review associated with the user

        return new ResponseEntity<Review>(review, HttpStatus.CREATED); // Return the created review with HTTP status 201 (CREATED)
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductsReview(@PathVariable Long productId) throws UserException, ProductException {

        List<Review> reviewList = reviewService.getAllProductReview(productId); // Fetch all reviews for the specified product
        return new ResponseEntity<>(reviewList, HttpStatus.ACCEPTED); // Return the list of reviews with HTTP status 202 (ACCEPTED) - (should be OK (200) instead)
    }


}
