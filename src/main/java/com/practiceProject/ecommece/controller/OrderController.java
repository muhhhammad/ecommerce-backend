package com.practiceProject.ecommece.controller;


import com.practiceProject.ecommece.entity.Address;
import com.practiceProject.ecommece.entity.Order;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.exception.OrderException;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.service.OrderService;
import com.practiceProject.ecommece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;
    private UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress,
                                             @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt); // Retrieve the user based on JWT
        Order order = orderService.createOrder(user, shippingAddress); // Create a new order
        System.out.println("Order: " + order); // Debugging statement (should be removed in production)

        return new ResponseEntity<Order>(order, HttpStatus.CREATED); // Return the created order with HTTP status 201 (CREATED)

    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt); // Retrieve the user based on JWT
        List<Order> order = orderService.usersOrderHistory(user.getUserId()); // Fetch the user's order history

        return new ResponseEntity<>(order, HttpStatus.OK); // Return the order history with HTTP status 200 (OK)

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findOrderById(@PathVariable("orderId") Long orderId,
                                               @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        User user = userService.findUserProfileByJwt(jwt); // Retrieve the user based on JWT
        Order order = orderService.findOrderById(orderId); // Find the order by ID

        return new ResponseEntity<>(order, HttpStatus.OK); // Return the order with HTTP status 200 (OK) (was incorrectly using CREATED)

    }


}
