package com.lcwd.electronicStore.repositories;

import com.lcwd.electronicStore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,String> {
}
