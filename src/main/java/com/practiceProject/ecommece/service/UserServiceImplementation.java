package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.config.JwtProvider;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImplementation implements UserService{

    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findUserById(Long Id) throws UserException {

        // Retrieve user by ID from the repository
        Optional<User> user = userRepository.findById(Id);
        if(user.isPresent()){ // If user exists, return the user object
            return user.get();
        }

        // Throw an exception if the user is not found
        throw new UserException("User Not Found with Given ID: " + Id);
    }

    @Override
    public User findUserProfileByJwt(String Jwt) throws UserException {

        // Extract the user's email from the provided JWT token
        String email = jwtProvider.getEmailFromToken(Jwt);

        // Find the user by email
        User user = userRepository.findByEmail(email);

        // If the email is null, throw an exception
        if(email == null){
            throw new UserException("User not Found with Email: " + email);
        }

        return user; // Return the found user
    }

}
