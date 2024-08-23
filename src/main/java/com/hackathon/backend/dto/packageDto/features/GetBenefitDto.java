package com.hackathon.backend.dto.packageDto.features;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBenefitDto {
    private int id;
    private String benefit;

    public GetBenefitDto(int id, String benefit) {
        this.id = id;
        this.benefit = benefit;
    }
}
