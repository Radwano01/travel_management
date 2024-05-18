package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.PlaneDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import com.hackathon.backend.repositories.plane.PlaneSeatsRepository;
import com.hackathon.backend.services.plane.PlaneService;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import com.hackathon.backend.utilities.plane.PlaneSeatsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaneServiceTest {


    @Mock
    private PlaneSeatsUtils planeSeatsUtils;

    @Mock
    private PlaneUtils planeUtils;

    @Mock
    private PlaneFlightsUtils planeFlightsUtils;

    private PlaneService planeService;

    @BeforeEach
    void setUp() {
        planeService = new PlaneService(
                planeSeatsUtils,
                planeUtils,
                planeFlightsUtils
        );
    }

    @AfterEach
    void tearDown() {
        planeUtils.deleteAll();
        planeSeatsUtils.deleteAll();
    }

    @Test
    void createPlane() {
        //given
        PlaneDto planeDto = new PlaneDto();
        planeDto.setPlaneCompanyName("test company name");
        planeDto.setNumSeats(100);

        //when
        ResponseEntity<?> response = planeService.createPlane(planeDto);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editPlane() {
        //given
        long planeId = 1L;

        PlaneDto planeDto = new PlaneDto();
        planeDto.setPlaneCompanyName("test company name");
        planeDto.setNumSeats(100);
        when(planeUtils.findPlaneById(planeId)).thenReturn(new PlaneEntity());
        //when
        ResponseEntity<?> response = planeService.editPlane(planeId,planeDto);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deletePlane() {
        //given
        long planeId = 1L;

        //when
        ResponseEntity<?> response = planeService.deletePlane(planeId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}