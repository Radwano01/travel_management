package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.HotelDto.HotelDto;
import com.hackathon.backend.Dto.HotelDto.RoomDto;
import com.hackathon.backend.Services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/v1/hotel")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    private HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @PostMapping(path = "/create-new-hotel")
    public ResponseEntity<?> createHotel(@RequestBody HotelDto hotelDto){
        return hotelService.createHotel(hotelDto);
    }

    @PostMapping(path = "/create-new-room")
    public ResponseEntity<?> createRoom(@RequestBody RoomDto roomDto){
        return hotelService.createNewRoom(roomDto);
    }

    @GetMapping(path = "/get-all-hotels")
    public ResponseEntity<?> getAllHotel(){
        return hotelService.getAllHotels();
    }

    @GetMapping(path = "/get-all-rooms")
    public ResponseEntity<?> getAllRoomsFromHotel(@RequestBody HotelDto hotelDto){
        return hotelService.getAllRoomsFromHotel(hotelDto);
    }

    @PutMapping(path = "/edit-hotel/{id}")
    public ResponseEntity<?> editHotel(@PathVariable int id, @RequestBody HotelDto hotelDto){
        return hotelService.editHotel(id, hotelDto);
    }

    @PutMapping(path = "/edit-room/{id}")
    public ResponseEntity<?> editRoom(@PathVariable int id, @RequestBody RoomDto roomDto){
        return hotelService.editRoom(id,roomDto);
    }

    @DeleteMapping(path = "/delete-hotel/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable int id){
        return hotelService.deleteHotel(id);
    }

    @DeleteMapping(path = "/delete-room/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable int id){
        return hotelService.deleteRooms(id);
    }
}
