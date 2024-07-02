package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetAirPortDto {
    private long airPortId;
    private String airPortName;
    private String airPortCode;

    public GetAirPortDto(long airPortId,
                         String airPortName,
                         String airPortCode) {
        this.airPortId = airPortId;
        this.airPortName = airPortName;
        this.airPortCode = airPortCode;
    }
}
