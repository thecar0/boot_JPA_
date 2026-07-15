package com.example.boot.service;

import com.example.boot.dto.UserDTO;
import com.example.boot.entity.AuthRole;
import com.example.boot.entity.User;
import com.example.boot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String insert(UserDTO userDTO) {
        // pwd 암호화 해서 DB 에 넣기
        userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
        User user = convertDtoToEntity(userDTO);
        user.addAuth(AuthRole.USER);
        // cascade.ALL 설정으로 인해 User 테이블이 변경되면 AuthUser 도 자동 저장
        return userRepository.save(user).getEmail();
    }

    @Transactional
    @Override
    public void lastloginUpdate(String name) {
        // findById 값을 가져와서 현재 시간으로 lastlogin set => update
        User user = userRepository.findById(name)
                .orElseThrow(()-> new EntityNotFoundException("해당 사용자가 없습니다."));
        // transactional dirty checking 발생 => update
        user.setLastLogin(LocalDateTime.now());
    }
}