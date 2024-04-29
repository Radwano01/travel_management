package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.RoomDetailsDto;
import com.hackathon.backend.services.hotel.RoomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return roomDetailsService.getRoomAllDetails(hotelId);
    }

    @PutMapping(path = "${EDIT_ROOM_DETAILS_PATH}")
    public ResponseEntity<?> editRoomDetails(@PathVariable("roomDetailsId") long roomDetailsId,
                                             @RequestBody RoomDetailsDto roomDetailsDto){
        return roomDetailsService.editRoomDetails(roomDetailsId,roomDetailsDto);
    }
}
