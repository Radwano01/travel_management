package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.CreatePlaneDto;
import com.hackathon.backend.dto.planeDto.EditPlaneDto;
import com.hackathon.backend.dto.planeDto.GetPlaneDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import com.hackathon.backend.services.plane.impl.PlaneServiceImpl;
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
class PlaneServiceImplTest {

    @Mock
    private PlaneRepository planeRepository;

    @InjectMocks
    private PlaneServiceImpl planeServiceImpl;

    @Test
    void createPlane_ShouldReturnPlaneDetails() {
        // given
        CreatePlaneDto createPlaneDto = new CreatePlaneDto();
        createPlaneDto.setPlaneCompanyName("Airways");
        createPlaneDto.setNumSeats(150);
        PlaneEntity planeEntity = new PlaneEntity("Airways", 150);

        // behavior
        when(planeRepository.save(any(PlaneEntity.class))).thenReturn(planeEntity);

        // when
        ResponseEntity<String> response = planeServiceImpl.createPlane(createPlaneDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(planeEntity.toString(), response.getBody());
    }

    @Test
    void getPlanes_ShouldReturnListOfPlanes() {
        // given
        List<GetPlaneDto> planeDtos = new ArrayList<>();
        GetPlaneDto planeDto = new GetPlaneDto();
        planeDtos.add(planeDto);

        // behavior
        when(planeRepository.findAllPlanes()).thenReturn(planeDtos);

        // when
        ResponseEntity<List<GetPlaneDto>> response = planeServiceImpl.getPlanes();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(planeDtos, response.getBody());
    }

    @Test
    void editPlane_ShouldReturnUpdatedPlaneDetails() {
        // given
        long planeId = 1;
        EditPlaneDto editPlaneDto = new EditPlaneDto();
        editPlaneDto.setPlaneCompanyName("New Airways");
        editPlaneDto.setStatus(true);
        editPlaneDto.setNumSeats(200);
        PlaneEntity planeEntity = new PlaneEntity("Old Airways", 100);
        planeEntity.setId(planeId);

        // behavior
        when(planeRepository.findById(planeId)).thenReturn(Optional.of(planeEntity));
        when(planeRepository.save(any(PlaneEntity.class))).thenReturn(planeEntity);

        // when
        ResponseEntity<String> response = planeServiceImpl.editPlane(planeId, editPlaneDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(planeEntity.toString(), response.getBody());
        assertEquals("New Airways", planeEntity.getPlaneCompanyName());
        assertTrue(planeEntity.isStatus());
        assertEquals(200, planeEntity.getNumSeats());
    }

    @Test
    void editPlane_ShouldReturnBadRequest_WhenNoDataIsSent() {
        // given
        long planeId = 1;
        EditPlaneDto editPlaneDto = new EditPlaneDto();

        // when
        ResponseEntity<String> response = planeServiceImpl.editPlane(planeId, editPlaneDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("you sent an empty data to change", response.getBody());
    }

    @Test
    void deletePlane_ShouldReturnSuccess_WhenPlaneIsDeleted() {
        // given
        long planeId = 1;
        PlaneEntity planeEntity = new PlaneEntity("Airways", 150);
        planeEntity.setId(planeId);

        // behavior
        when(planeRepository.findById(planeId)).thenReturn(Optional.of(planeEntity));
        when(planeRepository.findById(planeId)).thenReturn(Optional.of(planeEntity));
        when(planeRepository.findById(planeId)).thenReturn(Optional.of(planeEntity));

        // when
        ResponseEntity<String> response = planeServiceImpl.deletePlane(planeId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Plane deleted successfully", response.getBody());
    }

    @Test
    void deletePlane_ShouldReturnAlreadyValidException_WhenPlaneHasFlight() {
        // given
        long planeId = 1;
        PlaneEntity planeEntity = new PlaneEntity("Airways", 150);
        planeEntity.setId(planeId);
        planeEntity.setFlight(new PlaneFlightsEntity()); // Simulating that the plane has a flight

        // behavior
        when(planeRepository.findById(planeId)).thenReturn(Optional.of(planeEntity));

        // when
        ResponseEntity<String> response = planeServiceImpl.deletePlane(planeId);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("You can't delete a plane that has flight" + planeEntity.getFlight(), response.getBody());
    }
}
