package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.payment.RoomPaymentDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomBookingEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.RoomBookingRepository;
import com.hackathon.backend.utilities.user.UserUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.hackathon.backend.utilities.ErrorUtils.*;


@Service
public class RoomBookingService {

    private final RoomBookingRepository roomBookingRepository;
    private final HotelUtils hotelUtils;
    private final UserUtils userUtils;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Autowired
    public RoomBookingService(RoomBookingRepository roomBookingRepository,
                              HotelUtils hotelUtils,
                              UserUtils userUtils) {
        this.roomBookingRepository = roomBookingRepository;
        this.hotelUtils = hotelUtils;
        this.userUtils = userUtils;
    }

    @Async("bookingTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<String>> payment(long userId,
                                                             long hotelId,
                                                             String paymentIntentCode,
                                                             RoomPaymentDto roomPaymentDto){
        try{
            UserEntity user = userUtils.findById(userId);
            boolean userVerification = user.isVerificationStatus();
            if(!userVerification) {
                return CompletableFuture.completedFuture(badRequestException("User is Not Verified yet!"));
            }
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            for (RoomEntity room:hotel.getRooms()){
                if(room.isStatus()){
                    try {
                        PaymentIntent paymentIntent = createPayment(paymentIntentCode);
                        if (paymentIntent.getStatus().equals("succeeded")) {
                            RoomBookingEntity roomBookingEntity = new RoomBookingEntity(
                                    user,
                                    hotel,
                                    roomPaymentDto.getStartTime(),
                                    roomPaymentDto.getEndTime()
                            );
                            roomBookingRepository.save(roomBookingEntity);
                            return CompletableFuture.completedFuture
                                    ((ResponseEntity.ok("Room booked successfully")));
                        } else {
                            return CompletableFuture.completedFuture((ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                                    .body("Payment failed. Please check your payment details and try again.")));
                        }
                    } catch (Exception e) {
                        return CompletableFuture.completedFuture((serverErrorException(e)));
                    }
                }
            }
            return CompletableFuture.completedFuture((notFoundException("Invalid rooms in this hotel")));
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

    @Scheduled(fixedRate = 60000)
    public void removeExpiredBookings(){
        LocalDateTime nowTime = LocalDateTime.now();
        List<RoomBookingEntity> expiredBookings = roomBookingRepository.findAllByEndTime(nowTime);
        roomBookingRepository.deleteAll(expiredBookings);
    }
}
