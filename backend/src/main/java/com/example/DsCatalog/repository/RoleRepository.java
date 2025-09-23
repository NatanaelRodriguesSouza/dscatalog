package com.example.DsCatalog.repository;

import com.example.DsCatalog.entities.Role;
import com.example.DsCatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
