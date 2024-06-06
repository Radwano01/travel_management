package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.services.hotel.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                                         @RequestParam("hotelName") String hotelName,
                                         @RequestParam("mainImage") MultipartFile mainImage,
                                         @RequestParam("description") String description,
                                         @RequestParam("hotelRoomsCount") int hotelRoomsCount,
                                         @RequestParam("address") String address,
                                         @RequestParam("imageOne") MultipartFile imageOne,
                                         @RequestParam("imageTwo") MultipartFile imageTwo,
                                         @RequestParam("imageThree") MultipartFile imageThree,
                                         @RequestParam("imageFour") MultipartFile imageFour,
                                         @RequestParam("roomDescription") String roomDescription,
                                         @RequestParam("price") int price){
        PostH h = new PostH(
                hotelName,
                mainImage,
                description,
                hotelRoomsCount,
                address,
                0,
                imageOne,
                imageTwo,
                imageThree,
                imageFour,
                roomDescription,
                price
        );
        return hotelService.createHotel(countryId, h);
    }

    @GetMapping(path = "${GET_HOTELS_PATH}")
    public ResponseEntity<?> getHotels(@PathVariable("countryId") int countryId){
        return hotelService.getHotels(countryId);
    }

    @PutMapping(path = "${EDIT_HOTEL_PATH}")
    public ResponseEntity<?> editHotel(@PathVariable("hotelId") long hotelId,
                                       @PathVariable("countryId") int countryId,
                                       @RequestBody HotelDto hotelDto){
        return hotelService.editHotel(hotelId, countryId, hotelDto);
    }

    @DeleteMapping(path = "${DELETE_HOTEL_PATH}")
    public ResponseEntity<?> deleteHotel(@PathVariable("hotelId") long hotelId){
        return hotelService.deleteHotel(hotelId);
    }
}
