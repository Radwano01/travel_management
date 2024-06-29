package com.hackathon.backend.controllers.plane;

import com.hackathon.backend.dto.planeDto.EditPlaneSeatDto;
import com.hackathon.backend.dto.planeDto.PlaneSeatDto;
import com.hackathon.backend.services.plane.PlaneSeatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PlaneSeatsController {

    private final PlaneSeatsService planeSeatsService;

    @Autowired
    public PlaneSeatsController(PlaneSeatsService planeSeatsService) {
        this.planeSeatsService = planeSeatsService;
    }

    @PostMapping(path = "${ADD_PLANE_SEAT_PATH}")
    public ResponseEntity<String> addSeat(@PathVariable("planeId") long planeId,
                                        @RequestBody PlaneSeatDto planeSeatDto){
        return planeSeatsService.addSeat(planeId,planeSeatDto);
    }

    @GetMapping(path = "${GET_VALID_SEATS_PATH}")
    public ResponseEntity<?> getValidPlaneSeats(@PathVariable("planeId") long planeId){
        return planeSeatsService.getValidSeats(planeId);
    }

    @PutMapping(path = "${EDIT_PLANE_SEAT_PATH}")
    public ResponseEntity<String> editPlaneSeat(@PathVariable("seatId") long seatId,
                                      @RequestBody EditPlaneSeatDto editPlaneSeatDto){
        return planeSeatsService.editSeat(seatId, editPlaneSeatDto);
    }

    @DeleteMapping(path = "${DELETE_PLANE_SEAT_PATH}")
    public ResponseEntity<String> deletePlaneSeat(@PathVariable("planeId") long planeId,
                                        @PathVariable("seatId") long seatId){
        return planeSeatsService.deleteSeat(planeId,seatId);
    }
}
