package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.payment.PackagePaymentDto;
import com.hackathon.backend.entities.package_.PackageBookingEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.package_.PackageBookingRepository;
import com.hackathon.backend.utilities.user.UserUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PackageBookingService {

    private final UserUtils userUtils;

    private final PackageUtils packageUtils;

    private final PackageBookingRepository packageBookingRepository;

    @Autowired
    public PackageBookingService(UserUtils userUtils, PackageUtils packageUtils,
                                 PackageBookingRepository packageBookingRepository) {
        this.userUtils = userUtils;
        this.packageUtils = packageUtils;
        this.packageBookingRepository = packageBookingRepository;
    }

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;


    @Async("bookingTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<String>> payment(long userId,
                                                             int packageId,
                                                             PackagePaymentDto packagePaymentDto){
        try{
            UserEntity user = userUtils.findById(userId);
            boolean userVerification = user.isVerificationStatus();
            if(!userVerification) {
                return CompletableFuture.completedFuture
                        ((ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is Not Verified yet!")));
            }
            PackageEntity packageEntity = packageUtils.findById(packageId);
            try {
                PaymentIntent paymentIntent = createPayment(packagePaymentDto.getPaymentIntent(), packageEntity.getPrice());
                LocalDateTime bookedDate = LocalDateTime.now();
                if (paymentIntent.getStatus().equals("succeeded")) {
                    PackageBookingEntity packageBookingEntity = new PackageBookingEntity(
                            user,
                            packageEntity,
                            packagePaymentDto.getReservationName(),
                            bookedDate
                    );
                    packageBookingRepository.save(packageBookingEntity);
                    sendEmail(user.getEmail(), packageEntity.getPackageName(),
                            packageEntity.getCountry().getCountry(), packagePaymentDto.getReservationName(),
                            bookedDate);
                    return CompletableFuture.completedFuture
                            ((ResponseEntity.ok("package booked successfully")));
                } else {
                    return CompletableFuture.completedFuture((ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                            .body("Payment failed. Please check your payment details and try again.")));
                }
            } catch (Exception e) {
                return CompletableFuture.completedFuture((serverErrorException(e)));
            }
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @Async("bookingTaskExecutor")
    private void sendEmail(String email,
                           String packageName,
                           String country,
                           String reservationName,
                           LocalDateTime bookedDate) {
        String subject = "Package Booking Confirmation";
        String message = String.format("""
        Dear %s,

        Your package booking has been confirmed. Here are the details of your booking:

        Package Name: %s
        Destination Country: %s
        Booked Date: %s

        Thank you for choosing our service. We look forward to providing you with an excellent experience.

        A guide will contact you for further arrangements and any special requests you may have.

        Best regards,
        The Travel Agency Team
        """, reservationName, packageName, country, bookedDate);

        userUtils.sendMessageToEmail(userUtils.prepareTheMessageEmail(email, subject, message));
    }


    private PaymentIntent createPayment(String paymentIntentCode, int price) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        PaymentIntentCreateParams.Builder paramsBuilder = new PaymentIntentCreateParams.Builder()
                .setCurrency("USD")
                .setAmount((long) price * 100)
                .addPaymentMethodType("card")
                .setPaymentMethod(paymentIntentCode)
                .setConfirm(true)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .setErrorOnRequiresAction(true);
        return PaymentIntent.create(paramsBuilder.build());
    }
}
