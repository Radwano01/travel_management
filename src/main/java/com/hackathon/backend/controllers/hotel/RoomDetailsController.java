package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.EditRoomDetailsDto;

import com.hackathon.backend.services.hotel.RoomDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomDetailsController {

    private final RoomDetailsService roomDetailsService;

    @Autowired
    private RoomDetailsController(RoomDetailsService roomDetailsService){
        this.roomDetailsService = roomDetailsService;
    }

    @GetMapping(path = "${GET_ROOM_ALL_DETAILS_PATH}")
    public ResponseEntity<?> getRoomAllDetails(@PathVariable("hotelId") long hotelId){
        try {
            return roomDetailsService.getHotelRoomDetailsByHotelId(hotelId);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_ROOM_DETAILS_PATH}")
    public ResponseEntity<String> editRoomDetails(@PathVariable("hotelId") long hotelId,
                                                  @ModelAttribute EditRoomDetailsDto editRoomDetailsDto) {
        try {
            return roomDetailsService.editRoomDetails(hotelId, editRoomDetailsDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
