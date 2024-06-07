package com.hackathon.backend.dto.hotelDto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomDto {

    private long id;
    private boolean status;
    private GetRoomDetailsDto getRoomDetailsDto;

    public RoomDto(Long id, boolean status) {
        this.id = id;
        this.status = status;
    }
}
