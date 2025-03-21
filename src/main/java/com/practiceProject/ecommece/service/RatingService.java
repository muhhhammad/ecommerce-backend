package com.practiceProject.ecommece.service;


import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.entity.Rating;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.request.RatingRequest;

import java.util.List;

public interface RatingService {

    public Rating createRating(RatingRequest requests, User user) throws ProductException;

    public List<Rating> getProductRating(Long productId);

}
