package com.hackathon.backend.dto.packageDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PackageEvaluationDto {
    private long id;
    private String comment;
    private int rate;
    private long userId;
    private String username;
    private String userImage;

    public PackageEvaluationDto(long id, String comment,
                                int rate,long userId,
                                String username, String image) {
        this.id = id;
        this.comment = comment;
        this.rate = rate;
        this.username = username;
        this.userImage = image;
        this.userId = userId;
    }
}
