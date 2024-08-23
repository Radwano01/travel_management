package com.hackathon.backend.dto.packageDto.features;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRoadmapDto {
    private int id;
    private String roadmap;

    public GetRoadmapDto(int id, String roadmap) {
        this.id = id;
        this.roadmap = roadmap;
    }
}
