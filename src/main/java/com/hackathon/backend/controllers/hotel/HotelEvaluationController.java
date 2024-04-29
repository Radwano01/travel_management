package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.HotelEvaluationDto;
import com.hackathon.backend.services.hotel.HotelEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelEvaluationController {

    private final HotelEvaluationService hotelEvaluationService;

    @Autowired
    public HotelEvaluationController(HotelEvaluationService hotelEvaluationService) {
        this.hotelEvaluationService = hotelEvaluationService;
    }

    @PostMapping(path = "${ADD_HOTEL_EVALUATION_PATH}")
    public ResponseEntity<?> addComment(@PathVariable("hotelId") long hotelId,
                                        @PathVariable("userId") long userId,
                                        @RequestBody HotelEvaluationDto hotelEvaluationDto){
        return hotelEvaluationService.addComment(hotelId, userId, hotelEvaluationDto);
    }

    @GetMapping(path = "${GET_HOTEL_EVALUATION_PATH}")
    public ResponseEntity<?> getComments(@PathVariable("hotelId") long hotelId){
        return hotelEvaluationService.getComments(hotelId);
    }

    @PutMapping(path = "${EDIT_HOTEL_EVALUATION_PATH}")
    public ResponseEntity<?> editComment(@PathVariable("commentId") long commentId,
                                        @RequestBody HotelEvaluationDto hotelEvaluationDto){
        return hotelEvaluationService.editComment(commentId,hotelEvaluationDto);
    }

    @DeleteMapping(path = "${REMOVE_HOTEL_EVALUATION_PATH}")
    public ResponseEntity<?> deleteComment(@PathVariable("hotelId") long hotelId,
                                         @PathVariable("userId") long userId,
                                         @PathVariable("commentId") long commentId){
        return hotelEvaluationService.deleteComment(hotelId,userId,commentId);
    }
}
