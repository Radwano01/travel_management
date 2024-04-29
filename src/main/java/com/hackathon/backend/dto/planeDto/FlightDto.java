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
    private int price;
    private String departureCountry;
    private String destinationCountry;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm a")
    private LocalDateTime departureTime;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm a")
    private LocalDateTime arrivalTime;

    public FlightDto(long id, int price,
                     String planeCompanyName,
                     String departureCountry, String destinationCountry,
                     LocalDateTime departureTime,
                     LocalDateTime arrivalTime) {
        this.id = id;
        this.price = price;
        this.planeCompanyName = planeCompanyName;
        this.departureCountry = departureCountry;
        this.destinationCountry = destinationCountry;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
