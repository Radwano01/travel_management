package com.hackathon.backend.dto.hotelDto.evaluationDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetHotelEvaluationDto {

    private long id;
    private String comment;
    private int rate;
    private long userId;
    private String username;
    private String userImage;

    public GetHotelEvaluationDto(long id, String comment,
                                 int rate, long userId,
                                 String username, String userImage) {
        this.id = id;
        this.comment = comment;
        this.rate = rate;
        this.userId = userId;
        this.username = username;
        this.userImage = userImage;
    }
}
