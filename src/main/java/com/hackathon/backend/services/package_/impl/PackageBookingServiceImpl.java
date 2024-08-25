package com.hackathon.backend.services.package_.impl;

import com.hackathon.backend.dto.payment.PackagePaymentDto;
import com.hackathon.backend.entities.package_.PackageBookingEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.package_.PackageBookingRepository;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.services.package_.PackageBookingService;
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

import static com.hackathon.backend.utilities.ErrorUtils.badRequestException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PackageBookingServiceImpl implements PackageBookingService {

    private final PackageBookingRepository packageBookingRepository;
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public PackageBookingServiceImpl(PackageBookingRepository packageBookingRepository,
                                     PackageRepository packageRepository,
                                     UserRepository userRepository,
                                     JavaMailSender javaMailSender) {
        this.packageBookingRepository = packageBookingRepository;
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;


    @Async("bookingTaskExecutor")
    @Transactional
    @Override
    public ResponseEntity<String> payment(long userId,
                                          int packageId,
                                          PackagePaymentDto packagePaymentDto){
        UserEntity user = getUserById(userId);

        if(!user.isVerificationStatus()) {
            return badRequestException("User is Not Verified yet!");
        }

        PackageEntity packageEntity = getPackageById(packageId);

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
                return ResponseEntity.ok("package booked successfully");
            } else {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                        .body("Payment failed. Please check your payment details and try again.");
            }
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    private PackageEntity getPackageById(int packageId){
        return packageRepository.findById(packageId)
                .orElseThrow(()-> new EntityNotFoundException("No such package has this id"));
    }

    private UserEntity getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User id not found"));
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Async("bookingTaskExecutor")
    private void sendEmail(String email,
                           String packageName,
                           String country,
                           String reservationName,
                           LocalDateTime bookedDate) throws MessagingException {
        String formattedBookedDate = bookedDate.format(DATE_TIME_FORMATTER);

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
        """, reservationName, packageName, country, formattedBookedDate);

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
