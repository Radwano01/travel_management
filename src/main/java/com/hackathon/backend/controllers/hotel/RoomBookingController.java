package com.hackathon.backend.controllers.hotel;


import com.hackathon.backend.dto.payment.RoomPaymentDto;
import com.hackathon.backend.services.hotel.RoomBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomBookingController {

    private final RoomBookingService roomBookingService;
    @Autowired
    public RoomBookingController(RoomBookingService roomBookingService){
        this.roomBookingService = roomBookingService;
    }

    @PostMapping("{HOTEL_ROOM_PAYMENT_PATH}")
    public CompletableFuture<ResponseEntity<String>> payment(@PathVariable("userId") long userId,
                                                             @PathVariable("hotelId") long hotelId,
                                                             @PathVariable("paymentIntent") String paymentIntent,
                                                             @RequestBody RoomPaymentDto roomPaymentDto){
        return roomBookingService.payment(userId, hotelId, paymentIntent, roomPaymentDto);
    }
}
