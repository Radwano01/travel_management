package com.hackathon.backend.dto.hotelDto.evaluationDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditHotelEvaluationDto {

    private String comment;
    private Integer rate;
}
