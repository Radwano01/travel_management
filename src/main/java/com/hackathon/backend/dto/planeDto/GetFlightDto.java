package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GetFlightDto {

    private long id;
    private String planeCompanyName;
    private int price;

    private String departureAirPort;
    private String departureAirPortCode;

    private String destinationAirPort;
    private String destinationAirPortCode;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public GetFlightDto(long id,
                        String planeCompanyName,
                        int price,
                        String departureAirPort,
                        String departureAirPortCode,
                        String destinationAirPort,
                        String destinationAirPortCode,
                        LocalDateTime departureTime,
                        LocalDateTime arrivalTime) {
        this.id = id;
        this.planeCompanyName = planeCompanyName;
        this.price = price;
        this.departureAirPort = departureAirPort;
        this.departureAirPortCode = departureAirPortCode;
        this.destinationAirPort = destinationAirPort;
        this.destinationAirPortCode = destinationAirPortCode;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
