package com.example.DsCatalog.repository;

import com.example.DsCatalog.entities.Product;
import com.example.DsCatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
