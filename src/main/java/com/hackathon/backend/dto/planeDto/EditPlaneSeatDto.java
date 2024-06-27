package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditPlaneSeatDto {
    private Character seatClass;
    private Integer seatNumber;
}
