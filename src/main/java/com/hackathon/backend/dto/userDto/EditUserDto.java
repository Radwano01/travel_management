package com.hackathon.backend.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditUserDto {
    private String email;
    private String username;
    private String password;
    private String image;

    public EditUserDto(String email, String username,
                       String password, String image) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.image = image;
    }
}
