package com.hackathon.backend.services.plane.impl;

import com.hackathon.backend.dto.payment.FlightPaymentDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsBookingEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import com.hackathon.backend.repositories.plane.PlaneSeatsBookingRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.services.plane.PlaneSeatBookingService;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaneSeatBookingServiceImpl implements PlaneSeatBookingService {

    private final PlaneFlightsRepository planeFlightsRepository;
    private final PlaneSeatsBookingRepository planeSeatsBookingRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Autowired
    public PlaneSeatBookingServiceImpl(PlaneFlightsRepository planeFlightsRepository,
                                       PlaneSeatsBookingRepository planeSeatsBookingRepository,
                                       UserRepository userRepository,
                                       JavaMailSender javaMailSender) {
        this.planeFlightsRepository = planeFlightsRepository;
        this.planeSeatsBookingRepository = planeSeatsBookingRepository;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Async("bookingTaskExecutor")
    @Transactional
    @Override
    public ResponseEntity<String> payment(long userId,
                                          long flightId,
                                          FlightPaymentDto flightPaymentDto){
        UserEntity user = getUserById(userId);

        if(!user.isVerificationStatus()) {
            return badRequestException("User is Not Verified yet!");
        }

        PlaneFlightsEntity flightsEntity = getFlightById(flightId);
        PlaneEntity plane = flightsEntity.getPlane();
        try {
            PaymentIntent paymentIntent = createPayment(flightPaymentDto.getPaymentIntent(), flightsEntity.getPrice());
            if (paymentIntent.getStatus().equals("succeeded")) {
                plane.setNumSeats(plane.getNumSeats() - 1);
                plane.setPaidSeats(plane.getPaidSeats() + 1);

                LocalDateTime bookedDate = LocalDateTime.now();

                PlaneSeatsBookingEntity planeSeatsBookingEntity = new PlaneSeatsBookingEntity(
                        user,
                        flightsEntity,
                        flightPaymentDto.getReservationName(),
                        bookedDate
                );
                planeSeatsBookingRepository.save(planeSeatsBookingEntity);
                sendEmail(
                        user.getEmail(),
                        flightsEntity.getId(),
                        flightsEntity.getDepartureTime(),
                        flightsEntity.getArrivalTime(),
                        flightsEntity.getDepartureAirPort().getAirPortName(),
                        flightsEntity.getDestinationAirPort().getAirPortName(),
                        flightsEntity.getDepartureAirPort().getAirPortCode(),
                        flightsEntity.getDestinationAirPort().getAirPortCode(),
                        flightPaymentDto.getReservationName(),
                        bookedDate
                );

                return ResponseEntity.ok("Visa booked successfully");
            } else {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                        .body("Payment failed. Please check your payment details and try again.");
            }
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    private PlaneFlightsEntity getFlightById(long flightId) {
        return planeFlightsRepository.findById(flightId)
                .orElseThrow(()-> new EntityNotFoundException("No such flight has this id"));
    }

    private UserEntity getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User id not found"));
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Async("bookingTaskExecutor")
    private void sendEmail(String email,
                           long flightNumber,
                           LocalDateTime departureTime,
                           LocalDateTime destinationTime,
                           String departureAirport,
                           String departureAirportCode,
                           String arrivalAirport,
                           String arrivalAirportCode,
                           String reservationName,
                           LocalDateTime bookedDate) throws MessagingException{
        String formattedDepartureTime = departureTime.format(DATE_TIME_FORMATTER);
        String formattedDestinationTime = destinationTime.format(DATE_TIME_FORMATTER);
        String formattedBookedDate = bookedDate.format(DATE_TIME_FORMATTER);

        String subject = "Flight Booking Confirmation";
        String message = String.format("""
            Dear %s,
    
            Your flight booking has been confirmed. Here are the details of your booking:
    
            Flight Number: %s
            Departure Time: %s
            Destination Time: %s
            Departure Airport: %s (%s)
            Arrival Airport: %s (%s)
            Booked Date: %s
    
            Thank you for choosing our service. We look forward to flying with you.
    
            Best regards,
            The Airline Team
            """,reservationName, flightNumber, formattedDepartureTime,
                formattedDestinationTime, departureAirport, departureAirportCode,
                arrivalAirport, arrivalAirportCode, formattedBookedDate);

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
