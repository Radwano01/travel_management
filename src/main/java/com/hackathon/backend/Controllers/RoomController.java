package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.HotelDto.HotelDto;
import com.hackathon.backend.Dto.HotelDto.RoomDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(path = "${ROOM_API_PATH}")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping(path = "${ROOM_CREATE_PATH}")
    public ResponseEntity<?> createRoom(@PathVariable("hotelID") int hotelID ,@RequestBody RoomDto roomDto){
    return roomService.createNewRoom(hotelID,roomDto);
}

    @GetMapping(path = "${ROOM_GET_VALID_PATH}")
    public ResponseEntity<?> getValidVisas(){
        return roomService.getValidRooms();
    }

    @GetMapping(path = "${ROOM_ORDERED_PATH}")
    public ResponseEntity<?> getAllUserRoomPayment(@RequestParam("token") String token){
        return roomService.getUserRoomPayment(token);
    }
    @GetMapping(path = "${ROOM_GET_SINGLE_PATH}")
    public ResponseEntity<?> getSingleRoom(@PathVariable("roomID") int roomID){
        return roomService.getSingleRoom(roomID);
    }
    @PutMapping(path = "${ROOM_EDIT_SINGLE_STATUS_PATH}")
    public ResponseEntity<?> switchSingleRoomStatus(@PathVariable("roomID") int roomID){
        return roomService.switchSingleRoomStatusToEmpty(roomID);
    }

    @DeleteMapping(path = "${ROOM_DELETE_PATH}")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomID") int roomID){
        return roomService.deleteRooms(roomID);
    }

    @PutMapping(path = "${ROOM_EDIT_STATUS_PATH}")
    public ResponseEntity<?> switchRoomsStatus(){
        return roomService.switchRoomsStatusToEmpty();
    }

    @PutMapping(path = "${ROOM_EDIT_PATH}")
    public ResponseEntity<?> editRoom(@PathVariable("roomID") int roomID, @RequestBody RoomDto roomDto){
        return roomService.editRoom(roomID,roomDto);
    }

    @PostMapping(path = "${ROOM_PAYMENT_PATH}")
    public ResponseEntity<?> payment(@RequestBody PaymentDto paymentDto){
        return roomService.payment(paymentDto);
    }
}
