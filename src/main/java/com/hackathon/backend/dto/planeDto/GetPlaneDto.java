package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetPlaneDto {

    private long id;
    private String planeCompanyName;
    private int numSeats;
    private int paidSeats;
    private boolean status;

    public GetPlaneDto(long id, String planeCompanyName,
                       int numSeats, int paidSeats, boolean status) {
        this.id = id;
        this.planeCompanyName = planeCompanyName;
        this.numSeats = numSeats;
        this.paidSeats = paidSeats;
        this.status = status;
    }
}
