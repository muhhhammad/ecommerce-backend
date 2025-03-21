package com.practiceProject.ecommece.repository;

import com.practiceProject.ecommece.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {
}
