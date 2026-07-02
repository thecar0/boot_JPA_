package com.example.boot.entity;

import jakarta.persistence.*;
import lombok.*;

// @Table(name="") : 테이블 생성시 테이블 이름 변경 가능.
// 일반적으로 클래스명이 테이블 명으로 적용
// Entity : DB 의 테이블 맵핑 클래스 (DB용도)
// DTO : 객체를 생성하는 클래스 (화면용도)
// JPA Auditing : reg_date, mod_date 같은 모든 테이블에 동일하게 적용되는 칼럼으로 별도 관리 => base class (super class) 로 관리
// @id => primary key
// 기본키를 생성하는 전략 : GeneratedValue
// auto_increments => GenerationType.Identity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "board")
public class Board extends TimeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 100, nullable = false)
    private String writer;
    @Column(length = 2000, nullable = false)
    private String content;
    @Column(name = "read_count", columnDefinition = "int default 0")
    private int readCount;
    @Column(name = "cmt_qty", columnDefinition = "int default 0")
    private int cmtQty;
    @Column(name="fileS_qty", columnDefinition = "int default 0")
    private int fileQty;
}
