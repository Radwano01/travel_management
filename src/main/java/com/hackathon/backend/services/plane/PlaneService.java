package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.CreatePlaneDto;
import com.hackathon.backend.dto.planeDto.EditPlaneDto;
import com.hackathon.backend.dto.planeDto.GetPlaneDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PlaneService {

    ResponseEntity<String> createPlane(@NonNull CreatePlaneDto createPlaneDto);

    ResponseEntity<List<GetPlaneDto>> getPlanes();

    ResponseEntity<String> editPlane(long planeId, EditPlaneDto editPlaneDto);

    ResponseEntity<String> deletePlane(long planeId);
}
