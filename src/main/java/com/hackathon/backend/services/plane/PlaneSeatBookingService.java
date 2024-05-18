package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.payment.PlaneSeatPaymentDto;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsBookingEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.plane.PlaneSeatsBookingRepository;
import com.hackathon.backend.repositories.plane.PlaneSeatsRepository;
import com.hackathon.backend.utilities.UserUtils;
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
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PlaneSeatBookingService {

    private final UserUtils userUtils;
    private final PlaneSeatsRepository planeSeatsRepository;
    private final PlaneSeatsBookingRepository planeSeatsBookingRepository;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Autowired
    public PlaneSeatBookingService(UserUtils userUtils,
                              PlaneSeatsRepository planeSeatsRepository,
                              PlaneSeatsBookingRepository planeSeatsBookingRepository) {
        this.userUtils = userUtils;
        this.planeSeatsRepository = planeSeatsRepository;
        this.planeSeatsBookingRepository = planeSeatsBookingRepository;
    }

    @Transactional
    public ResponseEntity<?> payment(long id,
                                     long planeId,
                                     PlaneSeatPaymentDto planeSeatPaymentDto){
        try{
            UserEntity user = userUtils.findById(id);
            boolean userVerification = user.isVerificationStatus();
            if(!userVerification) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is Not Verified yet!");
            }
            PlaneSeatsEntity seat = planeSeatsRepository.findById(planeId)
                    .orElseThrow(() -> new EntityNotFoundException("Visa id is not found"));
            if (!seat.isStatus()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Visa Not Valid!");
            }
            try {
                PaymentIntent paymentIntent = createPayment(planeSeatPaymentDto);
                if (paymentIntent.getStatus().equals("succeeded")) {
                    seat.setStatus(false);
                    PlaneSeatsBookingEntity planeSeatsBookingEntity = new PlaneSeatsBookingEntity(
                            user,
                            seat
                    );
                    planeSeatsBookingRepository.save(planeSeatsBookingEntity);


                    return ResponseEntity.ok("Visa booked successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                            .body("Payment failed. Please check your payment details and try again.");
                }
            } catch (Exception e) {
                return serverErrorException(e);
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    private PaymentIntent createPayment(PlaneSeatPaymentDto planeSeatPaymentDto) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        PaymentIntentCreateParams.Builder paramsBuilder = new PaymentIntentCreateParams.Builder()
                .setCurrency("USD")
                .setAmount(1000L)
                .addPaymentMethodType("card")
                .setPaymentMethod(planeSeatPaymentDto.getPaymentIntent())
                .setConfirm(true)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .setErrorOnRequiresAction(true);
        return PaymentIntent.create(paramsBuilder.build());
    }
}
