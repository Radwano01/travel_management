package com.hackathon.backend.dto.bookingDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderedRoomsDto {

    private Long id;
    private String hotelName;
    private int floor;
    private int doorNumber;
    private int roomsNumber;
    private int bathroomsNumber;
    private int bedsNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public OrderedRoomsDto(Long id, String hotelName,
                           int floor, int doorNumber,
                           int roomsNumber, int bathroomsNumber,
                           int bedsNumber, LocalDateTime startTime,
                           LocalDateTime endTime) {
        this.id = id;
        this.hotelName = hotelName;
        this.floor = floor;
        this.doorNumber = doorNumber;
        this.roomsNumber = roomsNumber;
        this.bathroomsNumber = bathroomsNumber;
        this.bedsNumber = bedsNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
