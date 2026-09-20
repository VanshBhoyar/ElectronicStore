package com.lcwd.electronicStore.repositories;

import com.lcwd.electronicStore.entities.Order;
import com.lcwd.electronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByUser(User user);

}
