package com.hackathon.backend.dto.hotelDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetRoomsDto {
    private long id;
    private boolean status = false;

    public GetRoomsDto(long id, boolean status) {
        this.id = id;
        this.status = status;
    }
}
