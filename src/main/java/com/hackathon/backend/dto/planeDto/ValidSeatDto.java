package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidSeatDto {

    private long id;
    private char seatClass;
    private long seatNumber;

    public ValidSeatDto(long id, char seatClass,
                        Integer seatNumber) {
        this.id = id;
        this.seatClass = seatClass;
        this.seatNumber = seatNumber;
    }
}
