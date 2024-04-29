package com.hackathon.backend.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private long id;
    private String username;
    private String image;
    private boolean verificationStatus;

    public UserDto(long id,
                   String username,
                   String image) {
        this.id = id;
        this.username = username;
        this.image = image;
    }
}
