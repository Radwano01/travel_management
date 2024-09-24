package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.EditRoomDetailsDto;

import com.hackathon.backend.services.hotel.impl.RoomDetailsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomDetailsController {

    private final RoomDetailsServiceImpl roomDetailsServiceImpl;

    @Autowired
    private RoomDetailsController(RoomDetailsServiceImpl roomDetailsServiceImpl){
        this.roomDetailsServiceImpl = roomDetailsServiceImpl;
    }

    @GetMapping(path = "${GET_ROOM_ALL_DETAILS_PATH}")
    public ResponseEntity<?> getRoomAllDetails(@PathVariable("hotelId") long hotelId){
        try {
            return roomDetailsServiceImpl.getHotelRoomDetailsByHotelId(hotelId);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_HOTEL_FEATURES_FROM_ROOM_DETAILS_PATH}")
    public ResponseEntity<?> getAllHotelFeaturesFromRoomDetails(@PathVariable("hotelId") long hotelId){
        try{
            return roomDetailsServiceImpl.getHotelFeaturesFromRoomDetails(hotelId);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_ROOM_FEATURES_FROM_ROOM_DETAILS_PATH}")
    public ResponseEntity<?> getAllRoomFeaturesFromRoomDetails(@PathVariable("hotelId") long hotelId){
        try{
            return roomDetailsServiceImpl.getRoomFeaturesFromRoomDetails(hotelId);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_ROOM_DETAILS_PATH}")
    public ResponseEntity<String> editRoomDetails(@PathVariable("hotelId") long hotelId,
                                                  @ModelAttribute EditRoomDetailsDto editRoomDetailsDto) {
        try {
            return roomDetailsServiceImpl.editRoomDetails(hotelId, editRoomDetailsDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
