package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.services.hotel.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelController {
    private final HotelService hotelService;

    @Autowired
    private HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @PostMapping(path = "${CREATE_HOTEL_PATH}")
    public ResponseEntity<?> createHotel(@PathVariable("countryId") int countryId,
                                         @PathVariable("placeId") int placeId,
                                         @RequestBody HotelDto hotelDto){
        return hotelService.createHotel(countryId, placeId, hotelDto);
    }

    @GetMapping(path = "${GET_HOTELS_PATH}")
    public ResponseEntity<?> getHotels(@PathVariable("countryId") int countryId){
        return hotelService.getHotels(countryId);
    }

    @PutMapping(path = "${EDIT_HOTEL_PATH}")
    public ResponseEntity<?> editHotel(@PathVariable("hotelId") long hotelId,
                                       @PathVariable("countryId") int countryId,
                                       @PathVariable("placeId") int placeId,
                                       @RequestBody HotelDto hotelDto){
        return hotelService.editHotel(hotelId, countryId, placeId, hotelDto);
    }

    @DeleteMapping(path = "${DELETE_HOTEL_PATH}")
    public ResponseEntity<?> deleteHotel(@PathVariable("hotelId") long hotelId){
        return hotelService.deleteHotel(hotelId);
    }
}
