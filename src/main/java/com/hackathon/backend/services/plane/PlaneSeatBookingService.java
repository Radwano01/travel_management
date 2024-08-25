package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.payment.FlightPaymentDto;
import org.springframework.http.ResponseEntity;

public interface PlaneSeatBookingService {

    ResponseEntity<String> payment(long userId, long flightId, FlightPaymentDto flightPaymentDto);
}
