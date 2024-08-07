package com.hackathon.backend.controllers.plane;

import com.hackathon.backend.dto.payment.FlightPaymentDto;
import com.hackathon.backend.services.plane.PlaneSeatBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PlaneSeatBookingController {

    private final PlaneSeatBookingService planeSeatBookingService;

    @Autowired
    public PlaneSeatBookingController(PlaneSeatBookingService planeSeatBookingService) {
        this.planeSeatBookingService = planeSeatBookingService;
    }

    @PostMapping(path = "${PLANE_SEAT_PAYMENT_PATH}")
    public CompletableFuture<ResponseEntity<String>> payment(@PathVariable("userId") long userId,
                                                             @PathVariable("flightId") long flightId,
                                                             @RequestBody FlightPaymentDto flightPaymentDto){
        return planeSeatBookingService.payment(userId, flightId, flightPaymentDto);
    }
}
