//package com.hackathon.backend.Services.PlaneServices;
//
//import com.hackathon.backend.Dto.BookingDto.OrderedVisasDto;
//import com.hackathon.backend.Dto.payment.VisaPaymentDto;
//import com.hackathon.backend.Entities.plane.PlaneSeatsEntity;
//import com.hackathon.backend.Entities.plane.VisaBookingEntity;
//import com.hackathon.backend.Repositories.plane.VisaBookingRepository;
//import com.hackathon.backend.Repositories.plane.VisaRepository;
//import com.hackathon.backend.Utilities.UserTokenUtils;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.stripe.param.PaymentIntentCreateParams;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//import static com.hackathon.backend.Utilities.ErrorUtils.notFoundException;
//import static com.hackathon.backend.Utilities.ErrorUtils.serverErrorException;
//
//@Service
//public class VisaBookingService {
//
//    private final UserTokenUtils userTokenUtils;
//    private final VisaRepository visaRepository;
//    private final VisaBookingRepository visaBookingRepository;
//
//    @Value("${STRIPE_SECRET_KEY}")
//    private String stripeSecretKey;
//
//    @Autowired
//    public VisaBookingService(UserTokenUtils userTokenUtils,
//                              VisaRepository visaRepository,
//                              VisaBookingRepository visaBookingRepository) {
//        this.userTokenUtils = userTokenUtils;
//        this.visaRepository = visaRepository;
//        this.visaBookingRepository = visaBookingRepository;
//    }
//
//    @Transactional
//    public ResponseEntity<?> payment(String token,VisaPaymentDto visaPaymentDto){
//        try{
//            Long userId = userTokenUtils.getUserIdFromToken(token);
//            boolean userVerification = userTokenUtils.getUserVerificationFromToken(token);
//            if(!userVerification) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is Not Verified yet!");
//            }
//            PlaneSeatsEntity visa = visaRepository.findById(visaPaymentDto.getPaymentId())
//                    .orElseThrow(() -> new EntityNotFoundException("Visa id is not found"));
//            if (!visa.isStatus()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Visa Not Valid!");
//            }
//            try {
//                PaymentIntent paymentIntent = createPayment(visaPaymentDto);
//                if (paymentIntent.getStatus().equals("succeeded")) {
//                    visa.setStatus(false);
//                    return ResponseEntity.ok("Visa has been assigned to user ID: "+userId);
//                } else {
//                    return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
//                            .body("Payment failed. Please check your payment details and try again.");
//                }
//            } catch (Exception e) {
//                return serverErrorException(e);
//            }
//        }catch (Exception e){
//            return serverErrorException(e);
//        }
//    }
//
////    public ResponseEntity<?> getUserVisaPayment(String token){
////        try{
////            List<VisaDto> dto = new ArrayList<>();
////            Long userId = userTokenUtils.getUserIdFromToken(token);
////            List<PlaneSeatsEntity> visaEntity = visaRepository.findByUserId(userId);
////            for(PlaneSeatsEntity visa:visaEntity){
////                if(visa.getUserId().equals(userId)){
////                    VisaDto visaDto = new VisaDto();
////                    visaDto.setId(visa.getId());
////                    visaDto.setPlaceNumber(visa.getPlaceNumber());
////                    visaDto.setPrice(visa.getPrice());
////                    visaDto.setPlaneName(visa.getPlaneName());
////                    visaDto.setStatus(visa.getStatus());
////                    dto.add(visaDto);
////                }else{
////                    return new ResponseEntity<>("UserId doesn't match the valid UserId", HttpStatus.BAD_REQUEST);
////                }
////            }
////            return new ResponseEntity<>(dto, HttpStatus.OK);
////        }catch (Exception e){
////            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////    }
////
//    public ResponseEntity<?> getUserOrderedVisas(String token) {
//        try{
//            Long userId = userTokenUtils.getUserIdFromToken(token);
//            List<VisaBookingEntity> userVisas = visaBookingRepository.findAllByUserId(userId);
//            if(userVisas.isEmpty()){
//                return notFoundException("No Visas Found for the Provided user ID");
//            }
//            List<OrderedVisasDto> bookedVisas = userVisas.stream().map((booked)-> new OrderedVisasDto(
//                    booked.getId(),
//                    booked.getVisas().getPlaneName(),
//                    booked.getVisas().getPlaceNumber(),
//                    booked.getVisas().getPrice(),
//                    booked.getVisas().getPlane().getAirportLaunch(),
//                    booked.getVisas().getPlane().getAirportLand(),
//                    booked.getVisas().getPlane().getLaunchTime(),
//                    booked.getVisas().getPlane().getLandTime()
//            )).toList();
//            return ResponseEntity.ok(bookedVisas);
//        }catch (Exception e){
//            return serverErrorException(e);
//        }
//    }
//
//    public ResponseEntity<?> getOrderedSingleVisaDetails(String token) {
//        try {
//            Long userId = userTokenUtils.getUserIdFromToken(token);
//            VisaBookingEntity visa = visaBookingRepository.findVisaIdByUserId(userId);
//            return ResponseEntity.ok(visa);
//        }catch (Exception e){
//            return serverErrorException(e);
//        }
//    }
//
//    private PaymentIntent createPayment(VisaPaymentDto visaPaymentDto) throws StripeException {
//        Stripe.apiKey = stripeSecretKey;
//        PaymentIntentCreateParams.Builder paramsBuilder = new PaymentIntentCreateParams.Builder()
//                .setCurrency("USD")
//                .setAmount(1000L)
//                .addPaymentMethodType("card")
//                .setPaymentMethod(visaPaymentDto.getPaymentIntent())
//                .setConfirm(true)
//                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
//                .setErrorOnRequiresAction(true);
//        return PaymentIntent.create(paramsBuilder.build());
//    }
//
//
//
//}
