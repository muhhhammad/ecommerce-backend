package com.practiceProject.ecommece.repository;

import com.practiceProject.ecommece.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //this method will find user by there email....
    public User findByEmail(String email);


}
