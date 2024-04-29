//package com.hackathon.backend.plane.services;
//
//import com.hackathon.backend.dto.planeDto.PlaneSeatDto;
//import com.hackathon.backend.dto.planeDto.ValidSeatDto;
//import com.hackathon.backend.entities.plane.PlaneEntity;
//import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
//import com.hackathon.backend.repositories.plane.PlaneRepository;
//import com.hackathon.backend.repositories.plane.PlaneSeatsRepository;
//import com.hackathon.backend.services.plane.PlaneSeatsService;
//import com.hackathon.backend.utilities.plane.PlaneUtils;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//class PlaneSeatsServiceTest {
//
//    @Mock
//    private PlaneRepository planeRepository;
//
//    @Mock
//    private PlaneSeatsRepository planeSeatsRepository;
//
//    @Mock
//    private PlaneUtils planeUtils;
//
//    private PlaneSeatsService planeSeatsService;
//
//    @BeforeEach
//    void setUp() {
//        planeSeatsService = new PlaneSeatsService(planeRepository,
//                planeSeatsRepository,planeUtils);
//    }
//
//    @AfterEach
//    void tearDown() {
//        planeRepository.deleteAll();
//        planeSeatsRepository.deleteAll();
//    }
//
//    @Test
//    void addSeat() {
//        //given
//        long planeId = 1L;
//        PlaneEntity plane = new PlaneEntity();
//        plane.setId(planeId);
//
//        PlaneSeatDto planeSeatDto = new PlaneSeatDto();
//        planeSeatDto.setSeatClass('A');
//        planeSeatDto.setSeatNumber(1);
//
//        when(planeUtils.findPlaneById(planeId)).thenReturn(plane);
//        //when
//        ResponseEntity<?> response = planeSeatsService.addSeat(planeId,planeSeatDto);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void getValidSeats() {
//        //given
//        long planeId = 1L;
//        PlaneSeatsEntity planeSeats = new PlaneSeatsEntity();
//        planeSeats.setId(1L);
//        planeSeats.setSeatClass('A');
//        planeSeats.setSeatNumber(1);
//        planeSeats.setStatus(true);
//        planeSeatsRepository.save(planeSeats);
//
//        PlaneEntity plane = new PlaneEntity();
//        plane.setId(planeId);
//        plane.getPlaneSeats().add(planeSeats);
//
//        when(planeRepository.findById(planeId)).thenReturn(Optional.of(plane));
//        //when
//        ResponseEntity<?> response = planeSeatsService.getValidSeats(planeId);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        List<ValidSeatDto> planeSeatsBody = (List<ValidSeatDto>) response.getBody();
//
//        assertNotNull(planeSeatsBody);
//        assertEquals(planeSeats.getId(), planeSeatsBody.get(0).getId());
//        assertEquals(planeSeats.getSeatClass(), planeSeatsBody.get(0).getSeatClass());
//        assertEquals(planeSeats.getSeatNumber(), planeSeatsBody.get(0).getSeatNumber());
//    }
//
//    @Test
//    void editSeat() {
//        //given
//        long seatId = 1L;
//        PlaneEntity plane = new PlaneEntity();
//        plane.setId(1L);
//        plane.setNumSeats(100);
//
//        PlaneSeatsEntity planeSeats = new PlaneSeatsEntity();
//        planeSeats.setId(1L);
//        planeSeats.setPlane(plane);
//
//        PlaneSeatDto planeSeatDto = new PlaneSeatDto();
//        planeSeatDto.setSeatClass('B');
//        planeSeatDto.setSeatNumber(25);
//
//        when(planeSeatsRepository.findById(seatId)).thenReturn(Optional.of(planeSeats));
//        //when
//        ResponseEntity<?> response = planeSeatsService.editSeat(seatId, planeSeatDto);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void deleteVisa() {
//        //given
//        long planeId = 1L;
//        long seatId = 1L;
//
//        when(planeUtils.findPlaneById(planeId)).thenReturn(new PlaneEntity());
//        //when
//        ResponseEntity<?> response = planeSeatsService.deleteVisa(planeId,seatId);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//}