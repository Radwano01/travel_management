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
    public ResponseEntity<String> addRoom(@PathVariable("hotelId") long hotelId){
        return roomService.addRoom(hotelId);
    }

    @GetMapping(path = "${GET_ROOM_PATH}")
    public ResponseEntity<?> getRooms(@PathVariable("hotelId") long hotelId){
        return roomService.getRooms(hotelId);
    }

    @PutMapping(path = "${EDIT_ROOM_PATH}")
    public ResponseEntity<String> editRoom(@PathVariable("hotelId") long hotelId,
                                      @PathVariable("roomId") long roomId){
        return roomService.editRoom(hotelId,roomId);
    }

    @DeleteMapping(path = "${DELETE_ROOM_PATH}")
    public ResponseEntity<String> deleteRoom(@PathVariable("hotelId") long hotelId,
                                        @PathVariable("roomId") long roomId){
        return roomService.deleteRooms(hotelId,roomId);
    }
}
