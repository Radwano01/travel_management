package com.hackathon.backend.controllers.package_;

import com.hackathon.backend.dto.payment.PackagePaymentDto;
import com.hackathon.backend.services.package_.PackageBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PackageBookingController {

    private final PackageBookingService packageBookingService;

    @Autowired
    public PackageBookingController(PackageBookingService packageBookingService){
        this.packageBookingService = packageBookingService;
    }

    @PostMapping("${PACKAGE_PAYMENT_PATH}")
    public CompletableFuture<ResponseEntity<String>> payment(@PathVariable("userId") long userId,
                                                             @PathVariable("packageId") int packageId,
                                                             @RequestBody PackagePaymentDto packagePaymentDto){
        return packageBookingService.payment(userId, packageId, packagePaymentDto);
    }
}