package com.sp.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
