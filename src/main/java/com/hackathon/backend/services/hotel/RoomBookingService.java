package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.payment.RoomPaymentDto;
import com.hackathon.backend.entities.hotel.RoomBookingEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.RoomBookingRepository;
import com.hackathon.backend.utilities.UserUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.*;


@Service
public class RoomBookingService {

    private final RoomBookingRepository roomBookingRepository;
    private final RoomUtils roomUtils;
    private final UserUtils userUtils;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Autowired
    public RoomBookingService(RoomBookingRepository roomBookingRepository,
                              RoomUtils roomUtils,
                              UserUtils userUtils) {
        this.roomBookingRepository = roomBookingRepository;
        this.roomUtils = roomUtils;
        this.userUtils = userUtils;
    }


    @Transactional
    public ResponseEntity<?> payment(long userId,
                                     long planeId,
                                     RoomPaymentDto roomPaymentDto){
        try{
            UserEntity user = userUtils.findById(userId);
            boolean userVerification = user.isVerificationStatus();
            if(!userVerification) {
                return badRequestException("User is Not Verified yet!");
            }
            RoomEntity room = roomUtils.findById(planeId);
            if (!room.isStatus()) {
                return badRequestException("Room Not Valid!");
            }
            try {
                PaymentIntent paymentIntent = createPayment(roomPaymentDto);
                if (paymentIntent.getStatus().equals("succeeded")) {
                    room.setStatus(false);
                    RoomBookingEntity roomBookingEntity = new RoomBookingEntity(
                            user,
                            room,
                            roomPaymentDto.getStartTime(),
                            roomPaymentDto.getEndTime()
                    );
                    roomBookingRepository.save(roomBookingEntity);
                    return ResponseEntity.ok("Room booked successfully");
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


    private PaymentIntent createPayment(RoomPaymentDto roomPaymentDto) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        PaymentIntentCreateParams.Builder paramsBuilder = new PaymentIntentCreateParams.Builder()
                .setCurrency("USD")
                .setAmount(1000L)
                .addPaymentMethodType("card")
                .setPaymentMethod(roomPaymentDto.getPaymentIntent())
                .setConfirm(true)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .setErrorOnRequiresAction(true);
        return PaymentIntent.create(paramsBuilder.build());
    }

    @Scheduled(fixedRate = 60000)
    public void removeExpiredBookings(){
        LocalDateTime nowTime = LocalDateTime.now();
        List<RoomBookingEntity> expiredBookings = roomBookingRepository.findAllByEndTime(nowTime);
        roomBookingRepository.deleteAll(expiredBookings);
    }
}
