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
    private boolean verificationStatus;

    public EssentialUserDto(long id,
                            String username,
                            String image,
                            boolean verificationStatus) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.verificationStatus = verificationStatus;
    }
}
