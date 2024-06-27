package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditPlaneDto {
    private String planeCompanyName;
    private Integer numSeats;
}
