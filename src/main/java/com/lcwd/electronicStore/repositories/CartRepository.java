package com.lcwd.electronicStore.repositories;

import com.lcwd.electronicStore.entities.Cart;
import com.lcwd.electronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}
