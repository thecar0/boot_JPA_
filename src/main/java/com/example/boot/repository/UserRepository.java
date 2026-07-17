package com.example.boot.repository;

import com.example.boot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    // email 을 주고 User 객체와 AuthUser List 를 같이 리턴
    // selelct * from user u
    // left join auth_user a
    // on u.email = a.email
    // where u.email = email
    @Query("select u from User u left join fetch u.authList where u.email = :email")
    Optional<User> findByEmailWithAuth(@Param("email")String email);

    // 전체 유저와 각 유저의 authList 같이 리턴
    // 중복데이터 발견시 1개만 가져오기 distinct 로 제거
    // user 1 : auth N 중복데이터 발생 => distinct
    // select distinct u from user u left join auth_user on u.email = auth_user.email
    @Query("select distinct u from User u left join fetch u.authList")
    List<User> findByAllwithAuthList();

    // id 중복 확인
    boolean existsByEmail(String email);
}