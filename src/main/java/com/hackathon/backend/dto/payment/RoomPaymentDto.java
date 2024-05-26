package com.hackathon.backend.dto.payment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RoomPaymentDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
