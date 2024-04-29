package com.hackathon.backend.dto.bookingDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderedVisasDto {
    private Long id;
    private String planeName;
    private Integer placeNumber;
    private Integer price;
    private LocalDateTime airportLaunch;
    private LocalDateTime airportLand;
    private LocalDateTime launchTime;
    private LocalDateTime landTime;

    public OrderedVisasDto(Long id, String planeName,
                           Integer placeNumber, Integer price,
                           LocalDateTime airportLaunch, LocalDateTime airportLand,
                           LocalDateTime launchTime, LocalDateTime landTime) {
        this.id = id;
        this.planeName = planeName;
        this.placeNumber = placeNumber;
        this.price = price;
        this.airportLaunch = airportLaunch;
        this.airportLand = airportLand;
        this.launchTime = launchTime;
        this.landTime = landTime;
    }
}
