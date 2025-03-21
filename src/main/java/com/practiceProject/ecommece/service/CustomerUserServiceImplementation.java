package com.practiceProject.ecommece.service;


import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerUserServiceImplementation implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomerUserServiceImplementation(UserRepository userRepository){

        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username); // Fetch user by email
        if(username == null){ // Check if the username is null

            throw new UsernameNotFoundException("Invalid Email: " + username); // Throw exception if email is invalid
        }

        List<GrantedAuthority> auth = new ArrayList<>(); // Initialize empty authorities list

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), auth); // Return user details with empty authorities
    }

}
