package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.payment.RoomPaymentDto;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface RoomBookingService {

    CompletableFuture<ResponseEntity<String>> payment(long userId, long hotelId, RoomPaymentDto roomPaymentDto);
}
