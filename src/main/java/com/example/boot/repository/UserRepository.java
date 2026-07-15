package com.example.boot.repository;

import com.example.boot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    // email 을 주고 User 객체와 AuthUser List 를 같이 리턴
    // selelct * from user u
    // left join auth_user a
    // on u.email = a.email
    // where u.email = email
    @Query("select u from User u left join fetch u.authList where u.email = :email")
    Optional<User> findByEmailWithAuth(@Param("email")String email);
}