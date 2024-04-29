//package com.hackathon.backend.Services.BookingSerivices;
//
//
//import com.hackathon.backend.Dto.BookingDto.OrderedRoomsDto;
//import com.hackathon.backend.Dto.payment.RoomPaymentDto;
//import com.hackathon.backend.Entities.hotel.RoomBookingEntity;
//import com.hackathon.backend.Entities.hotel.RoomEntity;
//import com.hackathon.backend.Repositories.hotel.RoomBookingRepository;
//import com.hackathon.backend.Repositories.hotel.RoomRepository;
//import com.hackathon.backend.Services.HotelService;
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
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static com.hackathon.backend.Utilities.ErrorUtils.notFoundException;
//import static com.hackathon.backend.Utilities.ErrorUtils.serverErrorException;
//
//@Service
//public class RoomBookingService {
//
//    private final RoomBookingRepository roomBookingRepository;
//    private final RoomRepository roomRepository;
//    private final UserTokenUtils userTokenUtils;
//    private final HotelService hotelService;
//
//    @Value("${STRIPE_SECRET_KEY}")
//    private String stripeSecretKey;
//
//    @Autowired
//    public RoomBookingService(RoomBookingRepository roomBookingRepository,
//                              RoomRepository roomRepository,
//                              UserTokenUtils userTokenUtils,
//                              HotelService hotelService) {
//        this.roomBookingRepository = roomBookingRepository;
//        this.roomRepository = roomRepository;
//        this.userTokenUtils = userTokenUtils;
//        this.hotelService = hotelService;
//    }
//
//
//    @Transactional
//    public ResponseEntity<?> payment(String token, RoomPaymentDto roomPaymentDto){
//        try{
//            Long userId = userTokenUtils.getUserIdFromToken(token);
//            boolean userVerification = userTokenUtils.getUserVerificationFromToken(token);
//            if(!userVerification) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is Not Verified yet!");
//            }
//            RoomEntity room = roomRepository.findById(roomPaymentDto.getPaymentId())
//                    .orElseThrow(() -> new EntityNotFoundException("Room ID is not found: "+ roomPaymentDto.getPaymentId()));
//            if (!room.isStatus()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Room Not Valid!");
//            }
//            try {
//                PaymentIntent paymentIntent = createPayment(roomPaymentDto);
//                if (paymentIntent.getStatus().equals("succeeded")) {
//                    room.setStatus(false);
//                    new RoomBookingEntity(
//                            userId,
//                            room.getId(),
//                            roomPaymentDto.getStartTime(),
//                            roomPaymentDto.getEndTime()
//                    );
//                    return ResponseEntity.ok("Room booked successfully");
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
//    public ResponseEntity<?> getOrderedRooms(String token){
//        try{
//            Long userId = userTokenUtils.getUserIdFromToken(token);
//            List<RoomBookingEntity> userRooms = roomBookingRepository.findAllByUserId(userId);
//            if(userRooms.isEmpty()){
//                return notFoundException("No rooms Found for the Provided user ID");
//            }
//            List<OrderedRoomsDto> bookedRooms = userRooms.stream().map((booked)-> new OrderedRoomsDto(
//                    booked.getRooms().getId(),
//                    booked.getRooms().getHotelName(),
//                    booked.getRooms().getFloor(),
//                    booked.getRooms().getDoorNumber(),
//                    booked.getRooms().getRoomsNumber(),
//                    booked.getRooms().getBathroomsNumber(),
//                    booked.getRooms().getBedsNumber(),
//                    booked.getStartTime(),
//                    booked.getEndTime()
//            )).toList();
//            return ResponseEntity.ok(bookedRooms);
//        }catch (EntityNotFoundException e) {
//            return notFoundException(e);
//        } catch (Exception e){
//            return serverErrorException(e);
//        }
//    }
//
//    public ResponseEntity<?> getOrderedSingleRoomDetails(String token){
//        try{
//            Long userId = userTokenUtils.getUserIdFromToken(token);
//            RoomBookingEntity room = roomBookingRepository.findRoomIdByUserId(userId);
//            return ResponseEntity.ok(hotelService.getSingleRoom(room.getId()));
//        }catch (Exception e){
//            return serverErrorException(e);
//        }
//    }
//
//
//    private PaymentIntent createPayment(RoomPaymentDto roomPaymentDto) throws StripeException {
//        Stripe.apiKey = stripeSecretKey;
//        PaymentIntentCreateParams.Builder paramsBuilder = new PaymentIntentCreateParams.Builder()
//                .setCurrency("USD")
//                .setAmount(1000L)
//                .addPaymentMethodType("card")
//                .setPaymentMethod(roomPaymentDto.getPaymentIntent())
//                .setConfirm(true)
//                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
//                .setErrorOnRequiresAction(true);
//        return PaymentIntent.create(paramsBuilder.build());
//    }
//
//    @Scheduled(fixedRate = 60000)
//    public void removeExpiredBookings(){
//        LocalDateTime nowTime = LocalDateTime.now();
//        List<RoomBookingEntity> expiredBookings = roomBookingRepository.findAllByEndTime(nowTime);
//        roomBookingRepository.deleteAll(expiredBookings);
//    }
//}
