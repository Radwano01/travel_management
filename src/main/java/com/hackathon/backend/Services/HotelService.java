package com.hackathon.backend.Services;


import com.hackathon.backend.Dto.HotelDto.HotelDto;
import com.hackathon.backend.Dto.HotelDto.RoomDto;
import com.hackathon.backend.Entities.HotelEntity;
import com.hackathon.backend.Entities.RoomEntity;
import com.hackathon.backend.Repositories.HotelRepository;
import com.hackathon.backend.Repositories.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public HotelService(HotelRepository hotelRepository, RoomRepository roomRepository){
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    public ResponseEntity<?> createHotel(HotelDto hotelDto) {
        try{
            boolean existsHotel = hotelRepository.existsByHotelName(hotelDto.getHotelName());
            if(!existsHotel){
                HotelEntity hotelEntity = new HotelEntity();
                hotelEntity.setHotelName(hotelDto.getHotelName());
                hotelRepository.save(hotelEntity);
                return new ResponseEntity<>("Hotel created Successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Hotel already Valid", HttpStatus.CONFLICT);
            }

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> createNewRoom(RoomDto roomDto) {
        try{
            HotelEntity hotelEntity = hotelRepository.findByHotelName(roomDto.getHotelName())
                    .orElseThrow(()-> new EntityNotFoundException("Hotel is Not Found"));

            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setFloor(roomDto.getFloor());
            roomEntity.setDoorNumber(roomDto.getDoorNumber());
            roomEntity.setRoomsNumber(roomDto.getRoomsNumber());
            roomEntity.setBathroomsNumber(roomDto.getBathroomsNumber());
            roomEntity.setBedsNumber(roomDto.getRoomsNumber());
            roomEntity.setHotelEntityList(Collections.singletonList(hotelEntity));
            roomEntity.setPrice(roomDto.getPrice());
            roomEntity.setStatus(roomDto.getStatus());

            roomRepository.save(roomEntity);
            return new ResponseEntity<>("Rooms created Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllHotels(){
        try{
            List<HotelEntity> hotelEntity = hotelRepository.findAll();
            return new ResponseEntity<>(hotelEntity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllRoomsFromHotel(HotelDto hotelDto){
        try{
            List<RoomEntity> roomEntities = roomRepository.findAll();
            List<RoomDto> dto = new ArrayList<>();
            for(RoomEntity rooms:roomEntities){
                String validHotel = rooms.getHotelEntityList().stream()
                        .map(hotel-> hotel.getHotelName()).collect(Collectors.joining(", "));
                if(validHotel.contains(hotelDto.getHotelName())){
                    RoomDto roomDto = new RoomDto();
                    roomDto.setId(rooms.getId());
                    roomDto.setFloor(rooms.getFloor());
                    roomDto.setDoorNumber(rooms.getDoorNumber());
                    roomDto.setRoomsNumber(rooms.getRoomsNumber());
                    roomDto.setBathroomsNumber(rooms.getBathroomsNumber());
                    roomDto.setBedsNumber(rooms.getBedsNumber());
                    roomDto.setHotelName(validHotel);
                    roomDto.setPrice(rooms.getPrice());
                    roomDto.setStatus(rooms.getStatus());
                    dto.add(roomDto);
                }
            }
            return  new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editHotel(int id,HotelDto hotelDto){
        try{
            HotelEntity hotelEntity = hotelRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Hotel Id is Not Found"));
            if(hotelDto.getHotelName() != null){
            hotelEntity.setHotelName(hotelDto.getHotelName());
            }
            return new ResponseEntity<>("Hotel updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editRoom(int id, RoomDto roomDto) {
        try{
            RoomEntity roomEntity = roomRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Room Id is Not Found"));
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
            if(roomDto.getHotelName() != null){
                HotelEntity hotelEntity = hotelRepository.findByHotelName(roomDto.getHotelName())
                        .orElseThrow(()-> new EntityNotFoundException("Hotel is Not Found"));
                roomEntity.setHotelEntityList(Collections.singletonList(hotelEntity));
            }
            return new ResponseEntity<>("Room updated Successfully",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteHotel(int id) {
        try{
            hotelRepository.deleteById(id);
            return new ResponseEntity<>("Hotel deleted Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteRooms(int id){
        try{
            roomRepository.deleteById(id);
            return new ResponseEntity<>("Room deleted Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
