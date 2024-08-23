package com.hackathon.backend.dto.hotelDto.evaluationDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateHotelEvaluationDto {
    private String comment;
    private int rate;

    public CreateHotelEvaluationDto(String comment,
                                    int rate) {
        this.comment = comment;
        this.rate = rate;
    }
}
