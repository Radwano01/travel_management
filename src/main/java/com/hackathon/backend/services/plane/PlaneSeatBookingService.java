package com.hackathon.backend.services.plane;

import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsBookingEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.plane.PlaneSeatsBookingRepository;
import com.hackathon.backend.utilities.user.UserUtils;
import com.hackathon.backend.utilities.plane.PlaneSeatsUtils;
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
public class PlaneSeatBookingService {

    private final UserUtils userUtils;
    private final PlaneSeatsUtils planeSeatsUtils;
    private final PlaneSeatsBookingRepository planeSeatsBookingRepository;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Autowired
    public PlaneSeatBookingService(UserUtils userUtils,
                                   PlaneSeatsUtils planeSeatsUtils,
                              PlaneSeatsBookingRepository planeSeatsBookingRepository) {
        this.userUtils = userUtils;
        this.planeSeatsUtils = planeSeatsUtils;
        this.planeSeatsBookingRepository = planeSeatsBookingRepository;
    }

    @Async("bookingTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<String>> payment(long userId,
                                                             long planeId,
                                                             String paymentIntentCode){
        try{
            UserEntity user = userUtils.findById(userId);
            boolean userVerification = user.isVerificationStatus();
            if(!userVerification) {
                return CompletableFuture.completedFuture
                        ((ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is Not Verified yet!")));
            }
            PlaneSeatsEntity seat = planeSeatsUtils.findById(planeId);
            if (!seat.isStatus()) {
                return CompletableFuture.completedFuture
                        ((ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Visa Not Valid!")));
            }
            try {
                PaymentIntent paymentIntent = createPayment(paymentIntentCode);
                if (paymentIntent.getStatus().equals("succeeded")) {
                    seat.setStatus(false);
                    PlaneSeatsBookingEntity planeSeatsBookingEntity = new PlaneSeatsBookingEntity(
                            user,
                            seat
                    );
                    planeSeatsBookingRepository.save(planeSeatsBookingEntity);
                    return CompletableFuture.completedFuture((ResponseEntity.ok("Visa booked successfully")));
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
