package com.hackathon.backend.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EditUserDto {
    private String password;
    private MultipartFile image;

    public EditUserDto(String password, MultipartFile image) {
        this.password = password;
        this.image = image;
    }
}
