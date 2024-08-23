package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.CreateHotelDto;
import com.hackathon.backend.dto.hotelDto.EditHotelDto;
import com.hackathon.backend.services.hotel.HotelService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;


@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelController {
    private final HotelService hotelService;

    @Autowired
    private HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @PostMapping(path = "${CREATE_HOTEL_PATH}")
    public ResponseEntity<?> createHotel(@PathVariable("placeId") int placeId,
                                         @ModelAttribute CreateHotelDto createHotelDto){
        try {
            return hotelService.createHotel(placeId, createHotelDto);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_HOTELS_PATH}")
    public ResponseEntity<?> getHotels(@PathVariable("placeId") int placeId,
                                       @RequestParam("page") int page,
                                       @RequestParam("size") int size){
        try {
            return hotelService.getHotels(placeId, page, size);
        }catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_HOTEL_PATH}")
    public ResponseEntity<String> editHotel(@PathVariable("placeId") int placeId,
                                            @PathVariable("hotelId") long hotelId,
                                            @ModelAttribute EditHotelDto editHotelDto){
        try {
            return hotelService.editHotel(placeId, hotelId, editHotelDto);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_HOTEL_PATH}")
    public ResponseEntity<?> deleteHotel(@PathVariable("placeId") int placeId,
                                         @PathVariable("hotelId") long hotelId){
        try {
            return hotelService.deleteHotel(placeId, hotelId);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}
