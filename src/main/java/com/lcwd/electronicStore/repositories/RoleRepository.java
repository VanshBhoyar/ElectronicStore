package com.lcwd.electronicStore.repositories;

import com.lcwd.electronicStore.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
}
