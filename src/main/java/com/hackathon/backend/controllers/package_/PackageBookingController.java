package com.hackathon.backend.controllers.package_;

import com.hackathon.backend.dto.payment.PackagePaymentDto;
import com.hackathon.backend.services.package_.impl.PackageBookingServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PackageBookingController {

    private final PackageBookingServiceImpl packageBookingServiceImpl;

    @Autowired
    public PackageBookingController(PackageBookingServiceImpl packageBookingServiceImpl){
        this.packageBookingServiceImpl = packageBookingServiceImpl;
    }

    @PostMapping("${PACKAGE_PAYMENT_PATH}")
    public CompletableFuture<ResponseEntity<String>> payment(@PathVariable("userId") long userId,
                                                             @PathVariable("packageId") int packageId,
                                                             @RequestBody PackagePaymentDto packagePaymentDto){
        try {
            return CompletableFuture.completedFuture
                    (packageBookingServiceImpl.payment(userId, packageId, packagePaymentDto));
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }
}