package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.EditPlaneSeatDto;
import com.hackathon.backend.dto.planeDto.PlaneDto;
import com.hackathon.backend.dto.planeDto.PlaneSeatDto;
import com.hackathon.backend.dto.planeDto.ValidSeatDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.services.plane.PlaneSeatsService;
import com.hackathon.backend.services.plane.PlaneService;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import com.hackathon.backend.utilities.plane.PlaneSeatsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaneSeatsServiceTest {

    @Mock
    private PlaneUtils planeUtils;

    @Mock
    private PlaneSeatsUtils planeSeatsUtils;

    @Mock
    private PlaneFlightsUtils planeFlightsUtils;

    @InjectMocks
    private PlaneSeatsService planeSeatsService;

    @Test
    void addSeat() {
        //given
        long planeId = 1;


        PlaneEntity plane = new PlaneEntity();
        plane.setId(planeId);
        plane.setNumSeats(50);

        PlaneSeatDto planeSeatDto = new PlaneSeatDto();
        planeSeatDto.setSeatClass('A');
        planeSeatDto.setSeatNumber(5);

        //behavior
        when(planeUtils.findPlaneById(planeId)).thenReturn(plane);

        //when
        ResponseEntity<?> response = planeSeatsService.addSeat(planeId, planeSeatDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(planeUtils).findPlaneById(planeId);
        verify(planeSeatsUtils).save(plane.getPlaneSeats().get(0));
        verify(planeUtils).save(plane);
    }

    @Test
    void getValidSeats() {
        //given
        long planeId = 1;
        PlaneEntity plane = new PlaneEntity();
        plane.setId(planeId);

        PlaneSeatsEntity planeSeat = new PlaneSeatsEntity();
        plane.getPlaneSeats().add(planeSeat);
        planeSeat.setStatus(true);

        //behavior
        when(planeUtils.findById(planeId)).thenReturn(plane);

        //when
        ResponseEntity<?> response = planeSeatsService.getValidSeats(planeId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ValidSeatDto> validSeats = (List<ValidSeatDto>) response.getBody();
        assertNotNull(validSeats);
        assertEquals(1, validSeats.size());
        assertEquals(planeSeat.getSeatNumber(), validSeats.get(0).getSeatNumber());
        verify(planeUtils).findById(planeId);
    }

    @Test
    void editSeat() {
        //given
        int seatId = 1;
        PlaneEntity plane = new PlaneEntity();
        plane.setId(1);
        plane.setNumSeats(100);


        PlaneSeatsEntity planeSeat = new PlaneSeatsEntity();
        planeSeat.setId(seatId);
        planeSeat.setPlane(plane);

        plane.getPlaneSeats().add(planeSeat);

        EditPlaneSeatDto editPlaneSeatDto = new EditPlaneSeatDto();
        editPlaneSeatDto.setSeatNumber(25);
        editPlaneSeatDto.setSeatClass('B');

        //behavior
        when(planeSeatsUtils.findById(seatId)).thenReturn(planeSeat);

        //when
        ResponseEntity<?> response = planeSeatsService.editSeat(seatId, editPlaneSeatDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(planeSeatsUtils).findById(seatId);
        verify(planeSeatsUtils).save(planeSeat);
    }

    @Test
    void deleteSeat() {
        //given
        long planeId = 1;
        long seatId = 1;

        PlaneEntity plane = new PlaneEntity();
        plane.setId(planeId);

        PlaneSeatsEntity planeSeat = new PlaneSeatsEntity();
        planeSeat.setId(seatId);
        planeSeat.setSeatNumber(1);
        planeSeat.setSeatClass('A');
        planeSeat.setPlane(plane);

        plane.getPlaneSeats().add(planeSeat);

        //behavior
        when(planeUtils.findPlaneById(1L)).thenReturn(plane);

        //when
        ResponseEntity<?> response = planeSeatsService.deleteSeat(planeId, seatId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(planeUtils).findPlaneById(planeId);
        verify(planeSeatsUtils).deleteById(seatId);
        verify(planeUtils).save(plane);
    }
}