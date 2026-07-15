package com.example.boot.security;

import com.example.boot.entity.User;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAuthUser implements UserDetails {
    @Getter
    private User user;

    public CustomAuthUser(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // username password auth 권한 리스트
        return user.getAuthList().stream()
                .map(authUser ->
                        new SimpleGrantedAuthority(authUser.getAuth().getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
