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
    private String username;
    private String userImage;

    public HotelEvaluationDto(long id, String comment,
                              int rate,String username,
                              String image) {
        this.id = id;
        this.comment = comment;
        this.rate = rate;
        this.username = username;
        this.userImage = image;
    }
}
