package com.hackathon.backend.controllers.hotel;


import com.hackathon.backend.dto.payment.RoomPaymentDto;
import com.hackathon.backend.services.hotel.RoomBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomBookingController {

    private final RoomBookingService roomBookingService;
    @Autowired
    public RoomBookingController(RoomBookingService roomBookingService){
        this.roomBookingService = roomBookingService;
    }

    @PostMapping("{HOTEL_SEAT_PAYMENT}")
    public ResponseEntity<?> payment(@PathVariable("userId") long userId,
                                     @PathVariable("planeId") long planeId,
                                     @RequestBody RoomPaymentDto roomPaymentDto){
        return roomBookingService.payment(userId, planeId, roomPaymentDto);
    }
}
