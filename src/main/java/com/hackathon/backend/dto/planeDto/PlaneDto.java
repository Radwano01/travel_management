package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaneDto {

    private long id;
    private String planeCompanyName;
    private int numSeats;
}
