package com.example.boot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = "authList")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends TimeBase{
    @Id
    private String email;
    @Column(nullable = false)
    private String pwd;
    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // MapperBy : 연관관계의 테이블 쪽의 연결된 필드명 auth_user 의 user 와 연결
    // JPA 에서 주테이블이 아닌 쪽 에서는 조회만 가능하도록 관리
    // Cascode.ALL : User 를 수정/삭제 될때 auth_user 도 같이 수정/삭제 되도록 설계
    // orphanRemoval = true : 삭제시 같이 제거
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthUser> authList = new ArrayList<>();

    // 편의 메서드
    public void addAuth(AuthRole role){
        if(this.authList == null){
            this.authList = new ArrayList<>();
        }
        this.authList.add(AuthUser.builder()
                .user(this)
                .auth(role)
                .build());
    }
}
