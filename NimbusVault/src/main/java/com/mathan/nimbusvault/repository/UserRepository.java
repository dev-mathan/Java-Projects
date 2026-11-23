package com.mathan.nimbusvault.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mathan.nimbusvault.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{
	
	Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    boolean existsByMobileNumber(String mobileNumber);
    
    Optional<User> findByEmailAndMobileNumber(String email, String mobileNumber);
    
    

}
