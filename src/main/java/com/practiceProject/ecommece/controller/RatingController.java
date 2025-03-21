package com.practiceProject.ecommece.controller;

import com.practiceProject.ecommece.entity.Rating;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.request.RatingRequest;
import com.practiceProject.ecommece.service.RatingService;
import com.practiceProject.ecommece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private UserService userService;
    private RatingService ratingService;

    @Autowired
    public RatingController(UserService userService, RatingService ratingService) {
        this.userService = userService;
        this.ratingService = ratingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest request,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {

        User user = userService.findUserProfileByJwt(jwt); // Retrieve the user profile using the JWT token
        Rating rating = ratingService.createRating(request, user); // Create a new rating associated with the user

        return new ResponseEntity<Rating>(rating, HttpStatus.CREATED); // Return the created rating with HTTP status 201 (CREATED)
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductsRating(@RequestBody Long productId,
                                                          @RequestHeader("Authorization") String jwt) throws UserException, ProductException {

        User user = userService.findUserProfileByJwt(jwt); // Retrieve the user profile using the JWT token
        List<Rating> rating = ratingService.getProductRating(productId); // Fetch the ratings for the specified product

        return new ResponseEntity<>(rating, HttpStatus.CREATED); // Return the list of ratings with HTTP status 201 (CREATED) - (should be OK (200) instead)
    }


}
