package com.hackathon.backend.dto.hotelDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotelEvaluationDto {

    private long id;
    private String comment;
    private int rate;
    private long userId;
    private String username;
    private String userImage;

    public HotelEvaluationDto(long id, String comment,
                              int rate, long userId,
                              String username, String image) {
        this.id = id;
        this.comment = comment;
        this.rate = rate;
        this.userId = userId;
        this.username = username;
        this.userImage = image;
    }
}
