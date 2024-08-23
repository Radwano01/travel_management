package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EditFlightDto {
    private String planeCompanyName;
    private Integer price;

    private Long departureAirPortId;
    private String departureAirPortCode;

    private Long destinationAirPortId;
    private String destinationAirPortCode;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
