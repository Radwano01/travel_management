package com.hackathon.backend.Controllers;

import com.hackathon.backend.Dto.HotelDto.HotelDto;
import com.hackathon.backend.Dto.HotelDto.RoomDto;
import com.hackathon.backend.Services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "${HOTEL_API_PATH}")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    private HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @PostMapping(path = "${HOTEL_CREATE_HOTEL_PATH}")
    public ResponseEntity<?> createHotel(@RequestBody HotelDto hotelDto){
        return hotelService.createHotel(hotelDto);
    }

    @PostMapping(path = "${HOTEL_CREATE_ROOM_PATH}")
    public ResponseEntity<?> createRoom(@RequestBody RoomDto roomDto){
        return hotelService.createNewRoom(roomDto);
    }

    @GetMapping(path = "${HOTEL_GET_HOTELS_PATH}")
    public ResponseEntity<?> getAllHotel(){
        return hotelService.getAllHotels();
    }

    @GetMapping(path = "${HOTEL_GET_ROOMS_PATH}")
    public ResponseEntity<?> getAllRoomsFromHotel(@RequestBody HotelDto hotelDto){
        return hotelService.getAllRoomsFromHotel(hotelDto);
    }

    @PutMapping(path = "${HOTEL_EDIT_HOTEL_PATH}")
    public ResponseEntity<?> editHotel(@PathVariable int id, @RequestBody HotelDto hotelDto){
        return hotelService.editHotel(id, hotelDto);
    }

    @PutMapping(path = "${HOTEL_EDIT_ROOM_PATH}")
    public ResponseEntity<?> editRoom(@PathVariable int id, @RequestBody RoomDto roomDto){
        return hotelService.editRoom(id,roomDto);
    }

    @DeleteMapping(path = "${HOTEL_DELETE_HOTEL_PATH}")
    public ResponseEntity<?> deleteHotel(@PathVariable int id){
        return hotelService.deleteHotel(id);
    }

    @DeleteMapping(path = "${HOTEL_DELETE_ROOM_PATH}")
    public ResponseEntity<?> deleteRoom(@PathVariable int id){
        return hotelService.deleteRooms(id);
    }
}
