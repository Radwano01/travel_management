package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetAirPortDto {
    private String airPortName;
    private String airPortCode;

    public GetAirPortDto(String airPortName, String airPortCode) {
        this.airPortName = airPortName;
        this.airPortCode = airPortCode;
    }
}
