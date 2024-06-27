package com.hackathon.backend.dto.planeDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FlightDto {

    private long id;
    private String planeCompanyName;
    private Integer price;
    private String departureAirPort;
    private String departureAirPortCode;

    private String destinationAirPort;
    private String destinationAirPortCode;

    private String departureTime;
    private String arrivalTime;

    public FlightDto(long id,
                     String planeCompanyName,
                     int price,
                     String departureAirPort,
                     String departureAirPortCode,
                     String destinationAirPort,
                     String destinationAirPortCode,
                     String departureTime,
                     String arrivalTime) {
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
