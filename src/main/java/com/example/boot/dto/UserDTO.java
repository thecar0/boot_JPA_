package com.example.boot.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String email;
    private String pwd;
    private String nickName;
    private LocalDateTime lastLogin;
    private LocalDateTime regDate, modDate;
    private List<AuthUserDTO> authList;

    public String getLastLogin() {
        return  this.lastLogin != null ?
                lastLogin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")):
                null;
    }

    public String getRegDate() {
        return  this.regDate != null ?
                regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")):
                null;
    }

    public String getModDate() {
        return  this.modDate != null ?
                modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")):
                null;
    }
}
