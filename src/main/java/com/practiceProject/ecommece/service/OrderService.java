package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.exception.OrderException;
import com.practiceProject.ecommece.entity.Address;
import com.practiceProject.ecommece.entity.Order;
import com.practiceProject.ecommece.entity.User;

import java.util.List;

public interface OrderService {

    public Order createOrder(User user, Address shippingAddress);

    public Order findOrderById(Long orderId) throws OrderException;

    public List<Order> usersOrderHistory (Long userId);

    public Order placedOrder(Long orderId) throws OrderException;

    public Order confirmedOrder(Long orderId) throws OrderException;

    public Order shippedOrder(Long orderId) throws OrderException;

    public Order deliveredOrder(Long orderId) throws OrderException;

    public Order canceledOrder(Long orderId) throws OrderException;

    public List<Order> getAllOrder();

    public void deletedOrder(Long orderId) throws OrderException;
}
