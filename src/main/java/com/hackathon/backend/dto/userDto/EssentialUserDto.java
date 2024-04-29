package com.hackathon.backend.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EssentialUserDto {

    private long id;
    private String username;
    private String image;

    public EssentialUserDto(long id,
                            String username,
                            String image) {
        this.id = id;
        this.username = username;
        this.image = image;
    }
}
