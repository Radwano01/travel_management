package com.hackathon.backend.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDto {

    private String email;
    private String username;
    private String password;

    public RegisterUserDto(String email,
                           String username,
                           String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
