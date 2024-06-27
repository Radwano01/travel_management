package com.hackathon.backend.dto.packageDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditPackageEvaluationDto {
    private String comment;
    private Float rate;

    public EditPackageEvaluationDto(String comment,
                                Float rate) {
        this.comment = comment;
        this.rate = rate;
    }
}
