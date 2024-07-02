package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.EditPlaneDto;
import com.hackathon.backend.dto.planeDto.AirPortDto;
import com.hackathon.backend.dto.planeDto.GetAirPortDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;

import com.hackathon.backend.services.plane.AirPortService;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.plane.AirPortsUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AirPortServiceTest {

    @Mock
    AirPortsUtils airPortsUtils;

    @Mock
    PlaceUtils placeUtils;

    @InjectMocks
    AirPortService airPortService;

    AirPortDto airPortDto;
    AirPortEntity airPortEntity;

    @BeforeEach
    void setUp() {
        airPortDto = new AirPortDto();
        airPortDto.setAirPortName("Test Airport");
        airPortDto.setAirPortCode("TST");

        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.setId(1);
        placeEntity.setPlace("Test Place");
        placeUtils.save(placeEntity);

        airPortEntity = new AirPortEntity("Test Airport", "TST", placeEntity);
    }

    @AfterEach
    void tearDown(){
        airPortsUtils.deleteAll();
        placeUtils.deleteAll();
    }

    @Test
    void createAirPort() {
        //given
        PlaceEntity place = new PlaceEntity();
        place.setId(1);

        //behavior
        when(placeUtils.findById(1)).thenReturn(place);
        when(airPortsUtils.existsAirPortByAirPort(anyString())).thenReturn(false);

        //when
        ResponseEntity<?> response = airPortService.createAirPort(1, airPortDto);

        //then
        assertEquals(ResponseEntity.ok("AirPort created successfully"), response);
    }

    @Test
    void getAirPortsByPlaceId(){
        List<GetAirPortDto> mockAirPortDtos = Arrays.asList(
                new GetAirPortDto(1, "Airport 1", "AP1"),
                new GetAirPortDto(1, "Airport 2", "AP2")
        );

        when(airPortsUtils.findByPlaceId(anyInt())).thenReturn(mockAirPortDtos);

        ResponseEntity<?> response = airPortService.getAirPortsByPlaceId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAirPortDtos, response.getBody());
    }

    @Test
    void editAirPort() {
        //behavior
        when(airPortsUtils.findById(anyLong())).thenReturn(airPortEntity);
        when(airPortsUtils.checkHelper(any(AirPortDto.class))).thenReturn(true);

        //when
        ResponseEntity<?> response = airPortService.editAirPort(1L, airPortDto);

        //then
        assertEquals(ResponseEntity.ok("AirPort edited successfully"), response);
    }

    @Test
    void deleteAirPort() {
        //given
        doNothing().when(airPortsUtils).deleteById(anyLong());

        //when
        ResponseEntity<?> response = airPortService.deleteAirPort(1L);

        //then
        assertEquals(ResponseEntity.ok("AirPort deleted successfully"), response);
        verify(airPortsUtils, times(1)).deleteById(anyLong());
    }
}
