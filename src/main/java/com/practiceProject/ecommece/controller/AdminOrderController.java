package com.practiceProject.ecommece.controller;

import com.practiceProject.ecommece.entity.Order;
import com.practiceProject.ecommece.exception.OrderException;
import com.practiceProject.ecommece.response.ApiResponse;
import com.practiceProject.ecommece.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private OrderService orderService;

    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrdersHandler() {

        List<Order> order = orderService.getAllOrder(); // Retrieve the list of all orders
        return new ResponseEntity<List<Order>>(order, HttpStatus.ACCEPTED); // Return the list of orders with HTTP status 202 (ACCEPTED) - (should be OK (200) instead)

    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> confirmedOrderHandler(@PathVariable("orderId") Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException {

        Order order = orderService.confirmedOrder(orderId); // Mark the order as confirmed
        return new ResponseEntity<>(order, HttpStatus.OK); // Return the updated order with HTTP status 200 (OK)

    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shippedOrderHandler(@PathVariable("orderId") Long orderId,
                                                     @RequestHeader("Authorization") String jwt) throws OrderException {

        Order order = orderService.shippedOrder(orderId); // Mark the order as shipped
        return new ResponseEntity<>(order, HttpStatus.OK); // Return the updated order with HTTP status 200 (OK)

    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliveredOrderHandler(@PathVariable("orderId") Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException {

        Order order = orderService.deliveredOrder(orderId); // Mark the order as delivered
        return new ResponseEntity<>(order, HttpStatus.OK); // Return the updated order with HTTP status 200 (OK)

    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelledOrderHandler(@PathVariable("orderId") Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException {

        Order order = orderService.canceledOrder(orderId); // Mark the order as canceled
        return new ResponseEntity<>(order, HttpStatus.OK); // Return the updated order with HTTP status 200 (OK)

    }

    @DeleteMapping("/{orderId}") // Missing @DeleteMapping annotation
    public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable("orderId") Long orderId,
                                                          @RequestHeader("Authorization") String jwt) throws OrderException {

        orderService.deletedOrder(orderId); // Delete the order

        ApiResponse response = new ApiResponse(); // Create a response object
        response.setMessage("Order Deleted Successfully"); // Set success message
        response.setStatus(true); // Set status to true

        return new ResponseEntity<>(response, HttpStatus.OK); // Return response with HTTP status 200 (OK)
    }
}
