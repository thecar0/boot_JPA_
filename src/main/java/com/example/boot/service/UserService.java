package com.example.boot.service;

import com.example.boot.dto.AuthUserDTO;
import com.example.boot.dto.UserDTO;
import com.example.boot.entity.AuthUser;
import com.example.boot.entity.User;

public interface UserService {
    // convt
    // DTO -> Entity
    default User convertDtoToEntity(UserDTO userDTO){
        return User.builder()
                .email(userDTO.getEmail())
                .pwd(userDTO.getPwd())
                .nickName(userDTO.getNickname())
                .lastLogin(userDTO.getLastLogin())
                .build();
    }

    // Entity -> DTO
    default UserDTO convertEntitiyToDTO(User user){
        return UserDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickName())
                .lastLogin(user.getLastLogin())
                .regDate(user.getRegDate())
                .modDate(user.getModDate())
                .authList(user.getAuthList() == null ? null :
                        user.getAuthList().stream()
                        .map(this :: convertQuthEntityToAuthDto)
                        .toList())
                .build();
    }

    // auth authDTO 변환
    default AuthUserDTO convertQuthEntityToAuthDto(AuthUser authUser){
        return AuthUserDTO.builder()
                .id(authUser.getId())
                .email(authUser.getUser().getEmail())
                .role(authUser.getAuth().getRoleName())
                .build();
    }

    String insert(UserDTO userDTO);

    void lastloginUpdate(String name);
}