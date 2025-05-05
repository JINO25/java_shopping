package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRole(String role);
}
