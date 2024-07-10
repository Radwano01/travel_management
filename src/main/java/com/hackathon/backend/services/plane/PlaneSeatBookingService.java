package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.payment.FlightPaymentDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsBookingEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import com.hackathon.backend.repositories.plane.PlaneSeatsBookingRepository;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
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

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaneSeatBookingService {

    private final UserUtils userUtils;
    private final PlaneFlightsUtils planeFlightsUtils;
    private final PlaneSeatsBookingRepository planeSeatsBookingRepository;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Autowired
    public PlaneSeatBookingService(UserUtils userUtils,
                                   PlaneFlightsUtils planeFlightsUtils,
                                   PlaneSeatsBookingRepository planeSeatsBookingRepository) {
        this.userUtils = userUtils;
        this.planeFlightsUtils = planeFlightsUtils;
        this.planeSeatsBookingRepository = planeSeatsBookingRepository;
    }

    @Async("bookingTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<String>> payment(long userId,
                                                             long flightId,
                                                             FlightPaymentDto flightPaymentDto){
        try{
            UserEntity user = userUtils.findById(userId);
            boolean userVerification = user.isVerificationStatus();
            if(!userVerification) {
                return CompletableFuture.completedFuture
                        ((ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is Not Verified yet!")));
            }
            PlaneFlightsEntity flightsEntity = planeFlightsUtils.findById(flightId);
            if(flightsEntity.getAvailableSeats() == 0){
                return CompletableFuture.completedFuture(notFoundException("there is no seats for this flights"));
            }
            try {
                PaymentIntent paymentIntent = createPayment(flightPaymentDto.getPaymentIntent(), flightsEntity.getPrice());
                if (paymentIntent.getStatus().equals("succeeded")) {
                    flightsEntity.setAvailableSeats(flightsEntity.getAvailableSeats() - 1);
                    for(PlaneSeatsEntity seat:flightsEntity.getPlane().getPlaneSeats()){
                        if(!seat.isStatus()){
                            PlaneSeatsBookingEntity planeSeatsBookingEntity = new PlaneSeatsBookingEntity(
                                    user,
                                    seat,
                                    flightsEntity
                            );
                            planeSeatsBookingRepository.save(planeSeatsBookingEntity);
                        }
                    }
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
