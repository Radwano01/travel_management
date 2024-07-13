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

    private int price;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
