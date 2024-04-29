package com.hackathon.backend.dto.planeDto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaneSeatDto {

    private long id;
    private char seatClass;
    private int seatNumber;
    private boolean status;

}
