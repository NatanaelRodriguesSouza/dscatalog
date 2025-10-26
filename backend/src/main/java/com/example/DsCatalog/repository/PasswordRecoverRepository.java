package com.example.DsCatalog.repository;

import com.example.DsCatalog.entities.PasswordRecover;
import com.example.DsCatalog.entities.User;
import com.example.DsCatalog.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover,Long> {
	@Query("SELECT obj FROM PasswordRecover obj WHERE obj.token = :token AND obj.expiration > :now")
	List<PasswordRecover> searchValidTokens(String token, Instant now);
}
