package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.AirPortDto;
import com.hackathon.backend.dto.planeDto.GetAirPortDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.List;

public interface AirPortService {

    ResponseEntity<String> createAirPort(int placeId, @NonNull AirPortDto airPortDto);

    ResponseEntity<List<GetAirPortDto>> getAirPortsByPlaceId(int placeId);

    ResponseEntity<String> editAirPort(int placeId, long airPortId, AirPortDto airPortDto);

    ResponseEntity<String> deleteAirPort(int placeId, long airPortId);
}
