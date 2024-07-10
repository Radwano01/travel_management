package com.hackathon.backend.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PackagePaymentDto {
    private String paymentIntent;
}
