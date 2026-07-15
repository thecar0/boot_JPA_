package com.example.boot.security;

import com.example.boot.entity.User;
import com.example.boot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // security content 객체가 username 을 주고 해당 객체의
        // 실제 값을 DB 에서 가져와 userDetails 객체로 리턴
        // UserDetails => username, password, 권한
        User user = userRepository.findByEmailWithAuth(username)
                .orElseThrow(()-> new EntityNotFoundException("user not found :"+ username));
        log.info(">>>> login User >>{}", user);
        log.info(">>>> login User >>{}", user.getAuthList());
        return new CustomAuthUser(user);
    }
}