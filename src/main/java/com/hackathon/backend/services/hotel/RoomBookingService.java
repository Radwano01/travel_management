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
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
                                                             RoomPaymentDto roomPaymentDto) {
        try {
            UserEntity user = userUtils.findById(userId);
            boolean userVerification = user.isVerificationStatus();
            if (!userVerification) {
                return CompletableFuture.completedFuture(badRequestException("User is Not Verified yet!"));
            }

            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            for (RoomEntity room : hotel.getRooms()) {
                if (!room.isStatus()) {
                    try {
                        PaymentIntent paymentIntent = createPayment(roomPaymentDto.getPaymentIntent(), roomPaymentDto.getPrice());
                        if (paymentIntent.getStatus().equals("succeeded")) {
                            room.setStatus(true);
                            LocalDateTime bookedDate = LocalDateTime.now();
                            RoomBookingEntity roomBookingEntity = new RoomBookingEntity(
                                    user,
                                    hotel,
                                    roomPaymentDto.getStartTime(),
                                    roomPaymentDto.getEndTime(),
                                    roomPaymentDto.getReservationName(),
                                    bookedDate
                            );
                            roomBookingRepository.save(roomBookingEntity);
                            sendEmail(user.getEmail(), roomPaymentDto.getReservationName(),
                                    roomPaymentDto.getStartTime(), roomPaymentDto.getEndTime(),
                                    hotel.getHotelName(), hotel.getAddress(),
                                    bookedDate);
                            return CompletableFuture.completedFuture
                                    (ResponseEntity.ok("Room booked successfully, Now! you can check your book in your Email."));
                        } else {
                            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                                    .body("Payment failed. Please check your payment details and try again."));
                        }
                    } catch (Exception e) {
                        return CompletableFuture.completedFuture(serverErrorException(e));
                    }
                }
            }
            return CompletableFuture.completedFuture(notFoundException("Invalid rooms in this hotel"));
        } catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    private PaymentIntent createPayment(String paymentIntentCode, int amount) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        PaymentIntentCreateParams.Builder paramsBuilder = new PaymentIntentCreateParams.Builder()
                .setCurrency("USD")
                .setAmount((long) amount * 100)
                .addPaymentMethodType("card")
                .setPaymentMethod(paymentIntentCode)
                .setConfirm(true)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .setErrorOnRequiresAction(true);
        return PaymentIntent.create(paramsBuilder.build());
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Async("bookingTaskExecutor")
    private void sendEmail(String email,
                           String reservationName,
                           LocalDateTime startTime,
                           LocalDateTime endTime,
                           String hotelName,
                           String hotelAddress,
                           LocalDateTime bookedDate) throws MessagingException {
        String formattedStartTime = startTime.format(DATE_TIME_FORMATTER);
        String formattedEndTime = endTime.format(DATE_TIME_FORMATTER);
        String formattedBookedDate = bookedDate.format(DATE_TIME_FORMATTER);

        String subject = "Booking Confirmation";
        String message = String.format("""
            Dear %s,

            Your booking has been confirmed. Here are the details of your booking:

            Hotel Name: %s
            Hotel Address: %s
            Start Date: %s
            End Date: %s
            Booked Date: %s

            Thank you for choosing our service. We look forward to hosting you.

            Best regards,
            The Hotel Team""", reservationName, hotelName, hotelAddress, formattedStartTime, formattedEndTime, formattedBookedDate);

        userUtils.sendMessageToEmail(userUtils.prepareTheMessageEmail(email, subject, message));
    }

    @Scheduled(fixedRate = 60000)
    public void removeExpiredBookings(){
        LocalDateTime nowTime = LocalDateTime.now();
        List<RoomBookingEntity> expiredBookings = roomBookingRepository.findAllByEndTime(nowTime);
        roomBookingRepository.deleteAll(expiredBookings);
    }
}
