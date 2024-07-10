package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditFlightDto {
    private long id;
    private String planeCompanyName;
    private Integer price;
    private Integer availableSeats;
    private String departureAirPort;
    private String departureAirPortCode;

    private String destinationAirPort;
    private String destinationAirPortCode;

    private String departureTime;
    private String arrivalTime;
}
