package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.payment.PackagePaymentDto;
import org.springframework.http.ResponseEntity;

public interface PackageBookingService {

    ResponseEntity<String> payment(long userId, int packageId, PackagePaymentDto packagePaymentDto);
}
