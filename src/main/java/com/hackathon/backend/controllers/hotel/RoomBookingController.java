package com.hackathon.backend.controllers.hotel;


import com.hackathon.backend.dto.payment.RoomPaymentDto;
import com.hackathon.backend.services.hotel.impl.RoomBookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomBookingController {

    private final RoomBookingServiceImpl roomBookingServiceImpl;
    @Autowired
    public RoomBookingController(RoomBookingServiceImpl roomBookingServiceImpl){
        this.roomBookingServiceImpl = roomBookingServiceImpl;
    }

    @PostMapping(path = "${HOTEL_ROOM_PAYMENT_PATH}")
    public CompletableFuture<ResponseEntity<String>> payment(@PathVariable("userId") long userId,
                                                             @PathVariable("hotelId") long hotelId,
                                                             @RequestBody RoomPaymentDto roomPaymentDto){
        return roomBookingServiceImpl.payment(userId, hotelId, roomPaymentDto);
    }
}
