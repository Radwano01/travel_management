package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
