package com.hackathon.backend.dto.planeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetPlaneDto {

    private long id;
    private String planeCompanyName;
    private boolean status;

    public GetPlaneDto(long id,
                       String planeCompanyName,
                       boolean status) {
        this.id = id;
        this.planeCompanyName = planeCompanyName;
        this.status = status;
    }
}
