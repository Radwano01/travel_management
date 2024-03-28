package com.hackathon.backend.Services;


import com.hackathon.backend.Dto.HotelDto.RoomDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Entities.HotelEntity;
import com.hackathon.backend.RelationShips.RoomEntity;
import com.hackathon.backend.Entities.UserEntity;
import com.hackathon.backend.Repositories.HotelRepository;
import com.hackathon.backend.Repositories.RoomRepository;
import com.hackathon.backend.Repositories.UserRepository;
import com.hackathon.backend.Security.JWTGenerator;
import com.hackathon.backend.Utilities.UserFromToken;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeError;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RoomService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final UserFromToken userFromToken;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Autowired
    public RoomService(HotelRepository hotelRepository,
                       RoomRepository roomRepository, UserFromToken userFromToken) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.userFromToken = userFromToken;
    }


    @Transactional
    public ResponseEntity<?> createNewRoom(int hotelID,RoomDto roomDto) {
        try{
            HotelEntity hotelEntity = hotelRepository.findById(hotelID)
                    .orElseThrow(()-> new EntityNotFoundException("Hotel is Not Found"));
            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setHotelName(hotelEntity.getHotelName());
            roomEntity.setFloor(roomDto.getFloor());
            roomEntity.setDoorNumber(roomDto.getDoorNumber());
            roomEntity.setRoomsNumber(roomDto.getRoomsNumber());
            roomEntity.setBathroomsNumber(roomDto.getBathroomsNumber());
            roomEntity.setBedsNumber(roomDto.getRoomsNumber());
            roomEntity.setPrice(roomDto.getPrice());
            roomEntity.setStatus(roomDto.getStatus());
            roomEntity.setUserId(1);

            hotelEntity.getRooms().add(roomEntity);
            roomRepository.save(roomEntity);
            return new ResponseEntity<>("Rooms created Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getSingleRoom(int roomID) {
        try{
            RoomEntity roomEntity = roomRepository.findById(roomID)
                    .orElseThrow(()-> new EntityNotFoundException("Room id is Not Found: "+roomID));
            return new ResponseEntity<>(roomEntity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getValidRooms(){
        try{
            List<HotelEntity> hotelEntityList = hotelRepository.findAll();
            List<RoomDto> dto = new ArrayList<>();
            for(HotelEntity hotel:hotelEntityList){
                for(RoomEntity room:hotel.getRooms()){
                    String roomStatus = room.getStatus();
                    if(roomStatus.contains("empty")){
                        RoomDto roomDto = new RoomDto();
                        roomDto.setId(room.getId());
                        roomDto.setFloor(room.getFloor());
                        roomDto.setDoorNumber(room.getDoorNumber());
                        roomDto.setRoomsNumber(room.getRoomsNumber());
                        roomDto.setBathroomsNumber(room.getBathroomsNumber());
                        roomDto.setBedsNumber(room.getBedsNumber());
                        roomDto.setPrice(room.getPrice());
                        roomDto.setStatus(room.getStatus());
                        dto.add(roomDto);
                    }
                }
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteRooms(int roomID){
        try{
            roomRepository.deleteById(roomID);
            return new ResponseEntity<>("Room deleted Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> payment(PaymentDto paymentDto){
        try{
            String token = paymentDto.getAccessToken();
            int userId = userFromToken.getUserIdFromToken(token);
            boolean userVerification = userFromToken.getUserVerificationFromToken(token);
            if(userVerification) {
                RoomEntity room = roomRepository.findById(paymentDto.getPaymentId())
                        .orElseThrow(() -> new EntityNotFoundException("Room id is not found"));
                if (!room.getStatus().equals("full")) {
                    try {
                        Stripe.apiKey = stripeSecretKey;
                        PaymentIntentCreateParams.Builder paramsBuilder = new PaymentIntentCreateParams.Builder()
                                .setCurrency("USD")
                                .setAmount(1000L)
                                .addPaymentMethodType("card")
                                .setPaymentMethod(paymentDto.getPaymentIntent())
                                .setConfirm(true)
                                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                                .setErrorOnRequiresAction(true);

                        PaymentIntent paymentIntent = PaymentIntent.create(paramsBuilder.build());

                        if (paymentIntent.getStatus().equals("succeeded")) {
                            room.setUserId(userId);
                            room.setStatus("full");
                            return new ResponseEntity<>("Room has been assigned to user ID: " + userId, HttpStatus.OK);
                        } else {
                            StripeError lastError = paymentIntent.getLastPaymentError();
                            if (lastError != null) {
                                return new ResponseEntity<>("Payment failed: " + lastError.getMessage(), HttpStatus.PAYMENT_REQUIRED);
                            } else {
                                return new ResponseEntity<>("Payment failed. Please check your payment details and try again.", HttpStatus.PAYMENT_REQUIRED);
                            }
                        }
                    } catch (Exception e) {
                        return new ResponseEntity<>("Payment failed due to an error. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity<>("Room Not Valid!", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("User is Not Verified yet!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getUserRoomPayment(String token){
        try{
            int userId = userFromToken.getUserIdFromToken(token);
            List<RoomEntity> userRooms = roomRepository.findByUserId(userId);
            if(userRooms.isEmpty()){
                return new ResponseEntity<>("No rooms Found for the Provided user ID", HttpStatus.NOT_FOUND);
            }
            List<RoomDto> dto = new ArrayList<>();
            for(RoomEntity room : userRooms){

                RoomDto roomDto = new RoomDto();
                roomDto.setId(room.getId());
                roomDto.setFloor(room.getFloor());
                roomDto.setDoorNumber(room.getDoorNumber());
                roomDto.setRoomsNumber(room.getRoomsNumber());
                roomDto.setBathroomsNumber(room.getBathroomsNumber());
                roomDto.setBedsNumber(room.getBedsNumber());
                roomDto.setHotelName(room.getHotelName());
                roomDto.setPrice(room.getPrice());
                roomDto.setStatus(room.getStatus());
                dto.add(roomDto);
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    public ResponseEntity<?> editRoom(int roomID, RoomDto roomDto) {
        try{
            RoomEntity roomEntity = roomRepository.findById(roomID)
                    .orElseThrow(()-> new EntityNotFoundException("Room Id is Not Found"));
            if(roomDto.getHotelName() != null){
                roomEntity.setHotelName(roomDto.getHotelName());
            }
            if(roomDto.getFloor() != null){
                roomEntity.setFloor(roomDto.getFloor());
            }
            if(roomDto.getDoorNumber() != null){
                roomEntity.setDoorNumber(roomDto.getDoorNumber());
            }
            if(roomDto.getRoomsNumber() != null){
                roomEntity.setRoomsNumber(roomDto.getRoomsNumber());
            }
            if(roomDto.getBathroomsNumber() != null){
                roomEntity.setBathroomsNumber(roomDto.getBathroomsNumber());
            }
            if(roomDto.getBedsNumber() != null){
                roomEntity.setBedsNumber(roomDto.getFloor());
            }
            if(roomDto.getPrice() != null){
                roomEntity.setPrice(roomDto.getPrice());
            }
            if(roomDto.getStatus() != null){
                roomEntity.setStatus(roomDto.getStatus());
            }
            return new ResponseEntity<>("Room updated Successfully",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    public ResponseEntity<?> switchRoomsStatusToEmpty(){
        try{
            List<RoomEntity> roomEntities = roomRepository.findAll();
            for(RoomEntity room:roomEntities){
                if(room.getStatus().equals("full") && room.getUserId() >= 0){
                    room.setStatus("empty");
                    room.setUserId(1);
                    roomRepository.save(room);
                }
            }
            return new ResponseEntity<>("Rooms Status Switched To Empty", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> switchSingleRoomStatusToEmpty(int roomID){
        try {
            RoomEntity roomEntity = roomRepository.findById(roomID)
                    .orElseThrow(() -> new EntityNotFoundException("Room Id is Not Found"));
            roomEntity.setUserId(1);
            roomEntity.setStatus("empty");
            roomRepository.save(roomEntity);
            return new ResponseEntity<>("Room id Status updated: "+roomID, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
