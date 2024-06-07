package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.EditRoomDetailsDto;
import com.hackathon.backend.dto.hotelDto.GetRoomDetailsDto;
import com.hackathon.backend.services.hotel.RoomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> editRoomDetails(@PathVariable("hotelId") long hotelId,
                                             @RequestParam(name = "imageOne", required = false) MultipartFile imageOne,
                                             @RequestParam(name = "imageTwo", required = false) MultipartFile imageTwo,
                                             @RequestParam(name = "imageThree", required = false) MultipartFile imageThree,
                                             @RequestParam(name = "imageFour", required = false) MultipartFile imageFour,
                                             @RequestParam(name = "description", required = false) String description,
                                             @RequestParam(name = "price", required = false) int price) {
        EditRoomDetailsDto editRoomDetailsDto = new EditRoomDetailsDto(
                imageOne,
                imageTwo,
                imageThree,
                imageFour,
                description,
                price
        );
        return roomDetailsService.editRoomDetails(hotelId, editRoomDetailsDto);
    }
}
