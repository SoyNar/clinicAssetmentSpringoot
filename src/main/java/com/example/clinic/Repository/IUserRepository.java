package com.example.clinic.Repository;

import com.example.clinic.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
}
