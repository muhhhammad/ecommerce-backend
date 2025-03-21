package com.practiceProject.ecommece.controller;

import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        //Whenever we log into our ecommerce
        //It will Save JSON Web Token into our local Storage
        //And we will find the user with the token saved in our local storage
        //And then we will show in the profile that this user logged in
        //We will use this a lot
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);

    }

}
