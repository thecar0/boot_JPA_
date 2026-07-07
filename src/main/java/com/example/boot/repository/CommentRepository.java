package com.example.boot.repository;

import com.example.boot.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    /*
        findBy** => **테이블 안에 있는 모든 칼럼
        SELECT * FROM comment WHERE 칼럼명 = ?
        기본키 (ID) 가 아닌 일반 칼럼은 들록을 해야 사용 가능
        list<Comment> findByBno(Long bno);
     */
    List<Comment> findByBno(Long bno);

    Page<Comment> findByBno(Long bno, Pageable pageable);
}
