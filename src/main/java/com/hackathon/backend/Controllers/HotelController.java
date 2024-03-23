package com.hackathon.backend.Controllers;

import com.hackathon.backend.Dto.HotelDto.HotelDto;
import com.hackathon.backend.Dto.HotelDto.RoomDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
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

    @PostMapping(path = "${HOTEL_CREATE_PATH}")
    public ResponseEntity<?> createHotel(@RequestBody HotelDto hotelDto){
        return hotelService.createHotel(hotelDto);
    }

    @GetMapping(path = "${HOTEL_GET_PATH}")
    public ResponseEntity<?> getAllHotel(){
        return hotelService.getAllHotels();
    }

    @GetMapping(path = "${HOTEL_GET_SINGLE_PATH}")
    public ResponseEntity<?> getSingleHotel(@PathVariable int hotelID){
        return hotelService.getSingleHotel(hotelID);
    }

    @PutMapping(path = "${HOTEL_EDIT_PATH}")
    public ResponseEntity<?> editHotel(@PathVariable("hotelID") int hotelID, @RequestBody HotelDto hotelDto){
        return hotelService.editHotel(hotelID, hotelDto);
    }

    @DeleteMapping(path = "${HOTEL_DELETE_PATH}")
    public ResponseEntity<?> deleteHotel(@PathVariable("hotelID") int hotelID){
        return hotelService.deleteHotel(hotelID);
    }

}
