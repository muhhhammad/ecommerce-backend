package com.practiceProject.ecommece.repository;

import com.practiceProject.ecommece.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
