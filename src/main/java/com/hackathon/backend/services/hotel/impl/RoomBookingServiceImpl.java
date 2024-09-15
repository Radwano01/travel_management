package com.hackathon.backend.services.hotel.impl;

import com.hackathon.backend.dto.payment.RoomPaymentDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomBookingEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.hotel.RoomBookingRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.services.hotel.RoomBookingService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.hackathon.backend.utilities.ErrorUtils.*;


@Service
public class RoomBookingServiceImpl implements RoomBookingService {

    private final RoomBookingRepository roomBookingRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Autowired
    public RoomBookingServiceImpl(RoomBookingRepository roomBookingRepository,
                                  HotelRepository hotelRepository, UserRepository userRepository,
                                  JavaMailSender javaMailSender) {
        this.roomBookingRepository = roomBookingRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Async("bookingTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<String>> payment(long userId,
                                                             long hotelId,
                                                             RoomPaymentDto roomPaymentDto) {
        try {
            UserEntity user = getUserById(userId);
            boolean userVerification = user.isVerificationStatus();
            if (!userVerification) {
                return CompletableFuture.completedFuture(badRequestException("User is Not Verified yet!"));
            }

            HotelEntity hotel = findHotelById(hotelId);
            if(hotel.getHotelRoomsCount() == 0){
                return CompletableFuture.completedFuture(badRequestException("There is no empty room to book"));
            }

            try {
                PaymentIntent paymentIntent = createPayment(roomPaymentDto.getPaymentIntent(), roomPaymentDto.getPrice());
                if (paymentIntent.getStatus().equals("succeeded")) {
                    hotel.setHotelRoomsCount(hotel.getHotelRoomsCount() - 1);
                    hotel.setPaidRoomsCount(hotel.getHotelRoomsCount() + 1);

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

        } catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    private UserEntity getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User id not found"));
    }

    private HotelEntity findHotelById(long hotelId){
        return hotelRepository.findById(hotelId)
                .orElseThrow(()-> new EntityNotFoundException("Hotel id not found"));
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

        sendMessageToEmail(prepareTheMessageEmail(email, subject, message));
    }

    private MimeMessage prepareTheMessageEmail(String email, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(message, true);
        return mimeMessage;
    }

    private void sendMessageToEmail(MimeMessage mimeMessage){
        javaMailSender.send(mimeMessage);
    }

    @Scheduled(fixedRate = 1800000)
    public void removeExpiredBookings(){
        LocalDateTime nowTime = LocalDateTime.now();
        List<RoomBookingEntity> expiredBookings = roomBookingRepository.findAllByEndTime(nowTime);
        for(RoomBookingEntity roomBookingEntity : expiredBookings){
            HotelEntity hotel = roomBookingEntity.getHotel();
            hotel.setHotelRoomsCount(hotel.getHotelRoomsCount() + 1);
            hotel.setPaidRoomsCount(hotel.getPaidRoomsCount() - 1);
            hotelRepository.save(hotel);
        }
    }
}
