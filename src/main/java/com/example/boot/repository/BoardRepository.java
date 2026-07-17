package com.example.boot.repository;

import com.example.boot.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JPARepository 상속 받아서 사용
// jpaRepository<테이블병, id Type> id Type 클래스 타입
public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository{
    // 기본 CRUD repository 알아서 사용 가능. 특수 문법이 필요할 경우 사용

    List<Board> findTop5ByOrderByReadCountDesc();

}
