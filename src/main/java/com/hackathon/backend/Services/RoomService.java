package com.hackathon.backend.Services;


import com.hackathon.backend.Dto.HotelDto.HotelDto;
import com.hackathon.backend.Dto.HotelDto.RoomDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Entities.HotelEntity;
import com.hackathon.backend.Entities.RoomEntity;
import com.hackathon.backend.Entities.UserEntity;
import com.hackathon.backend.Entities.VisaEntity;
import com.hackathon.backend.Repositories.HotelRepository;
import com.hackathon.backend.Repositories.RoomRepository;
import com.hackathon.backend.Repositories.UserRepository;
import com.hackathon.backend.Security.JWTGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    private final JWTGenerator jwtGenerator;
    private final UserRepository userRepository;

    @Autowired
    public RoomService(HotelRepository hotelRepository,
                       RoomRepository roomRepository,
                       JWTGenerator jwtGenerator,
                       UserRepository userRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }


    @Transactional
    public ResponseEntity<?> createNewRoom(int hotelID,RoomDto roomDto) {
        try{
            HotelEntity hotelEntity = hotelRepository.findById(hotelID)
                    .orElseThrow(()-> new EntityNotFoundException("Hotel is Not Found"));
            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setHotelName(roomDto.getHotelName());
            roomEntity.setFloor(roomDto.getFloor());
            roomEntity.setDoorNumber(roomDto.getDoorNumber());
            roomEntity.setRoomsNumber(roomDto.getRoomsNumber());
            roomEntity.setBathroomsNumber(roomDto.getBathroomsNumber());
            roomEntity.setBedsNumber(roomDto.getRoomsNumber());
            roomEntity.setPrice(roomDto.getPrice());
            roomEntity.setStatus(roomDto.getStatus());
            roomEntity.setUserId(1);

            roomRepository.save(roomEntity);
            hotelEntity.getRooms().add(roomEntity);
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
            int userId = getUserIdFromToken(token);
            boolean userVerification = getUserVerificationFromToken(token);
            if(userVerification) {
                RoomEntity room = roomRepository.findById(paymentDto.getPaymentId())
                        .orElseThrow(() -> new EntityNotFoundException("Room id is not found"));
                if (!room.getStatus().equals("full")) {
                    room.setUserId(userId);
                    room.setStatus("full");
                    return new ResponseEntity<>("Room given to user id: " + userId, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Room has been taken!", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("User is Not Verify it yet!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getUserRoomPayment(String token){
        try{
            int userId = getUserIdFromToken(token);
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


    private int getUserIdFromToken(String token){
        String username = jwtGenerator.getUsernameFromJWT(token);
        UserEntity user = userRepository.findIdByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("Username is Not Found"));
        return user.getId();
    }

    private boolean getUserVerificationFromToken(String token){
        String username = jwtGenerator.getUsernameFromJWT(token);
        UserEntity user = userRepository.findIdByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("Username is Not Found"));
        return user.isVerification_status();
    }

}
