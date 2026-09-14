package com.lcwd.electronicStore.repositories;


import com.lcwd.electronicStore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
