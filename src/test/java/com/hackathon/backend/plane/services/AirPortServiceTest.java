package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.AirPortDto;
import com.hackathon.backend.dto.planeDto.GetAirPortDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.services.plane.AirPortService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirPortServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private AirPortService airPortService;

    @Test
    void createAirPort_ShouldReturnSuccess_WhenAirPortIsCreated() {
        // given
        int placeId = 1;
        AirPortDto airPortDto = new AirPortDto();
        airPortDto.setAirPortName("JFK");
        airPortDto.setAirPortCode("JFK001");
        PlaceEntity place = new PlaceEntity();
        place.setAirPorts(new ArrayList<>());

        // behavior
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));
        when(placeRepository.save(any(PlaceEntity.class))).thenReturn(place);

        // when
        ResponseEntity<String> response = airPortService.createAirPort(placeId, airPortDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AirPort created successfully", response.getBody());
        assertEquals(1, place.getAirPorts().size());
        assertEquals("JFK", place.getAirPorts().get(0).getAirPortName());
    }

    @Test
    void createAirPort_ShouldReturnBadRequest_WhenAirPortNameAlreadyExists() {
        // given
        int placeId = 1;
        AirPortDto airPortDto = new AirPortDto();
        airPortDto.setAirPortName("JFK");
        airPortDto.setAirPortCode("JFK002");
        PlaceEntity place = new PlaceEntity();
        AirPortEntity existingAirPort = new AirPortEntity("JFK", "JFK002", place);
        place.setAirPorts(List.of(existingAirPort));

        // behavior
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        // when
        ResponseEntity<String> response = airPortService.createAirPort(placeId, airPortDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("airport name already exist"));
    }

    @Test
    void getAirPortsByPlaceId_ShouldReturnListOfAirPorts() {
        // given
        int placeId = 1;
        List<GetAirPortDto> airPortDtos = new ArrayList<>();
        GetAirPortDto airPortDto = new GetAirPortDto();
        airPortDtos.add(airPortDto);

        // behavior
        when(placeRepository.findAllAirportsByPlaceId(placeId)).thenReturn(airPortDtos);

        // when
        ResponseEntity<List<GetAirPortDto>> response = airPortService.getAirPortsByPlaceId(placeId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(airPortDtos, response.getBody());
    }

    @Test
    void editAirPort_ShouldReturnUpdatedAirPortDetails() {
        // given
        int placeId = 1;
        long airPortId = 1L;
        AirPortDto airPortDto = new AirPortDto();
        airPortDto.setAirPortName("New JFK");
        airPortDto.setAirPortCode("JFK003");
        PlaceEntity place = new PlaceEntity();
        AirPortEntity airPortEntity = new AirPortEntity("JFK", "JFK001", place);
        airPortEntity.setId(airPortId);
        place.setAirPorts(List.of(airPortEntity));

        // behavior
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));
        when(placeRepository.save(any(PlaceEntity.class))).thenReturn(place);

        // when
        ResponseEntity<String> response = airPortService.editAirPort(placeId, airPortId, airPortDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("New JFK"));
        assertEquals("New JFK", airPortEntity.getAirPortName());
        assertEquals("JFK003", airPortEntity.getAirPortCode());
    }

    @Test
    void editAirPort_ShouldReturnBadRequest_WhenNoDataIsSent() {
        // given
        int placeId = 1;
        long airPortId = 1L;
        AirPortDto airPortDto = new AirPortDto();
        PlaceEntity place = new PlaceEntity();
        AirPortEntity airPortEntity = new AirPortEntity("JFK", "JFK001", place);
        airPortEntity.setId(airPortId);
        place.setAirPorts(List.of(airPortEntity));

        // when
        ResponseEntity<String> response = airPortService.editAirPort(placeId, airPortId, airPortDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("you sent an empty data to change"));
    }

    @Test
    void deleteAirPort_ShouldReturnSuccess_WhenAirPortIsDeleted() {
        // given
        int placeId = 1;
        long airPortId = 1L;
        PlaceEntity place = new PlaceEntity();
        AirPortEntity airPortEntity = new AirPortEntity("JFK", "JFK001", place);
        airPortEntity.setId(airPortId);

        // Use ArrayList instead of List.of
        List<AirPortEntity> airPorts = new ArrayList<>();
        airPorts.add(airPortEntity);
        place.setAirPorts(airPorts);

        // behavior
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        // when
        ResponseEntity<String> response = airPortService.deleteAirPort(placeId, airPortId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AirPort deleted successfully", response.getBody());
        assertTrue(place.getAirPorts().isEmpty()); // Verify that the airport has been removed
    }
}
