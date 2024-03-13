package com.filesharing.filesharingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.filesharing.filesharingapi.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String email);
}
