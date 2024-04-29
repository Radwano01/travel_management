package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.services.hotel.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    private RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping(path = "${ADD_ROOM_PATH}")
    public ResponseEntity<?> addRoom(@PathVariable("hotelId") long hotelId){
        return roomService.addRoom(hotelId);
    }

    @PutMapping(path = "${EDIT_ROOM_PATH}")
    public ResponseEntity<?> editRoom(@PathVariable("hotelId") long hotelId,
                                      @PathVariable("roomId") long roomId){
        return roomService.editRoom(hotelId,roomId);
    }

    @DeleteMapping(path = "${DELETE_ROOM_PATH}")
    public ResponseEntity<?> deleteRoom(@PathVariable("hotelId") long hotelId,
                                        @PathVariable("roomId") long roomId){
        return roomService.deleteRooms(hotelId,roomId);
    }
}
