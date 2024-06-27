package com.hackathon.backend.dto.hotelDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditHotelEvaluationDto {

    private String comment;
    private Float rate;

    public EditHotelEvaluationDto(String comment,
                                  Float rate) {
        this.comment = comment;
        this.rate = rate;
    }
}
