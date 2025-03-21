package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.entity.OrderItems;
import com.practiceProject.ecommece.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImplementation implements OrderItemService{

    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItems createOrderItems(OrderItems orderItems) {

        return orderItemRepository.save(orderItems);

    }
}
