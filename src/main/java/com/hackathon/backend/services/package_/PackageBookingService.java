package com.hackathon.backend.services.package_;

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
                                                             String paymentIntentCode){
        try{
            UserEntity user = userUtils.findById(userId);
            boolean userVerification = user.isVerificationStatus();
            if(!userVerification) {
                return CompletableFuture.completedFuture
                        ((ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is Not Verified yet!")));
            }
            PackageEntity packageEntity = packageUtils.findById(packageId);
            try {
                PaymentIntent paymentIntent = createPayment(paymentIntentCode);
                if (paymentIntent.getStatus().equals("succeeded")) {
                    PackageBookingEntity packageBookingEntity = new PackageBookingEntity(
                            user,
                            packageEntity
                    );
                    packageBookingRepository.save(packageBookingEntity);
                    return CompletableFuture.completedFuture
                            ((ResponseEntity.ok("Visa booked successfully")));
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

    private PaymentIntent createPayment(String paymentIntentCode) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        PaymentIntentCreateParams.Builder paramsBuilder = new PaymentIntentCreateParams.Builder()
                .setCurrency("USD")
                .setAmount(1000L)
                .addPaymentMethodType("card")
                .setPaymentMethod(paymentIntentCode)
                .setConfirm(true)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .setErrorOnRequiresAction(true);
        return PaymentIntent.create(paramsBuilder.build());
    }
}
