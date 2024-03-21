package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.HotelDto.HotelDto;
import com.hackathon.backend.Dto.HotelDto.RoomDto;
import com.hackathon.backend.Services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "${hotel.controller.path}")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    private HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @PostMapping(path = "${hotel.create.hotel.path}")
    public ResponseEntity<?> createHotel(@RequestBody HotelDto hotelDto){
        return hotelService.createHotel(hotelDto);
    }

    @PostMapping(path = "${hotel.create.room.path}")
    public ResponseEntity<?> createRoom(@RequestBody RoomDto roomDto){
        return hotelService.createNewRoom(roomDto);
    }

    @GetMapping(path = "${hotel.get.hotels.path}")
    public ResponseEntity<?> getAllHotel(){
        return hotelService.getAllHotels();
    }

    @GetMapping(path = "${hotel.get.rooms.path}")
    public ResponseEntity<?> getAllRoomsFromHotel(@RequestBody HotelDto hotelDto){
        return hotelService.getAllRoomsFromHotel(hotelDto);
    }

    @PutMapping(path = "${hotel.edit.hotel.path}")
    public ResponseEntity<?> editHotel(@PathVariable int id, @RequestBody HotelDto hotelDto){
        return hotelService.editHotel(id, hotelDto);
    }

    @PutMapping(path = "${hotel.edit.room.path}")
    public ResponseEntity<?> editRoom(@PathVariable int id, @RequestBody RoomDto roomDto){
        return hotelService.editRoom(id,roomDto);
    }

    @DeleteMapping(path = "${hotel.delete.hotel.path}")
    public ResponseEntity<?> deleteHotel(@PathVariable int id){
        return hotelService.deleteHotel(id);
    }

    @DeleteMapping(path = "${hotel.delete.room.path}")
    public ResponseEntity<?> deleteRoom(@PathVariable int id){
        return hotelService.deleteRooms(id);
    }
}

