package com.hackathon.backend.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RoomPaymentDto {
    private int price;
    private String paymentIntent;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
