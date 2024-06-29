package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.EditHotelDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.dto.hotelDto.PostHotelDto;
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
    public ResponseEntity<String> createHotel(@PathVariable("countryId") int countryId,
                                         @RequestParam("hotelName") String hotelName,
                                         @RequestParam("mainImage") MultipartFile mainImage,
                                         @RequestParam("description") String description,
                                         @RequestParam("hotelRoomsCount") int hotelRoomsCount,
                                         @RequestParam("address") String address,
                                         @RequestParam("rate") int rate,
                                         @RequestParam("imageOne") MultipartFile imageOne,
                                         @RequestParam("imageTwo") MultipartFile imageTwo,
                                         @RequestParam("imageThree") MultipartFile imageThree,
                                         @RequestParam("imageFour") MultipartFile imageFour,
                                         @RequestParam("roomDescription") String roomDescription,
                                         @RequestParam("price") int price){
        PostHotelDto h = new PostHotelDto(
                hotelName,
                mainImage,
                description,
                hotelRoomsCount,
                address,
                rate,
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
    public ResponseEntity<String> editHotel(@PathVariable("hotelId") long hotelId,
                                       @RequestParam(name = "hotelName", required = false) String hotelName,
                                       @RequestParam(name = "mainImage", required = false) MultipartFile mainImage,
                                       @RequestParam(name = "description", required = false) String description,
                                       @RequestParam(name = "hotelRoomsCount", required = false) Integer hotelRoomsCount,
                                       @RequestParam(name = "address", required = false) String address,
                                       @RequestParam(name = "price", required = false) Integer price,
                                       @RequestParam(name = "rate", required = false) Integer rate,
                                       @RequestParam(name = "countryId", required = false) Integer countryId){
        EditHotelDto editHotelDto = new EditHotelDto(
                hotelName,
                mainImage,
                description,
                hotelRoomsCount,
                address,
                price,
                rate,
                countryId
        );
        return hotelService.editHotel(hotelId, editHotelDto);
    }

    @DeleteMapping(path = "${DELETE_HOTEL_PATH}")
    public ResponseEntity<String> deleteHotel(@PathVariable("hotelId") long hotelId){
        return hotelService.deleteHotel(hotelId);
    }
}
