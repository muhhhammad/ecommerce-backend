package com.practiceProject.ecommece.service;


import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.entity.User;

public interface UserService {

    //by implementing this method we can find the user by ID
    public User findUserById(Long Id) throws UserException;

    //by implementing this method we can find th user by JWT(Json Web Token)
    public User findUserProfileByJwt(String Jwt) throws UserException;



}
