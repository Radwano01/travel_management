package com.hackathon.backend.controllers.hotel;

import com.hackathon.backend.dto.hotelDto.evaluationDto.CreateHotelEvaluationDto;
import com.hackathon.backend.dto.hotelDto.evaluationDto.EditHotelEvaluationDto;
import com.hackathon.backend.services.hotel.HotelEvaluationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelEvaluationController {

    private final HotelEvaluationService hotelEvaluationService;

    @Autowired
    public HotelEvaluationController(HotelEvaluationService hotelEvaluationService) {
        this.hotelEvaluationService = hotelEvaluationService;
    }

    @PostMapping(path = "${ADD_HOTEL_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> addComment(@PathVariable("hotelId") long hotelId,
                                                           @PathVariable("userId") long userId,
                                                           @RequestBody CreateHotelEvaluationDto createHotelEvaluationDto){
        try {
            return hotelEvaluationService.addComment(hotelId, userId, createHotelEvaluationDto);
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture(notFoundException(e));
        }catch (Exception e){
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @GetMapping(path = "${GET_HOTEL_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> getComments(@PathVariable("hotelId") long hotelId){
        try {
            return hotelEvaluationService.getComments(hotelId);
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture(notFoundException(e));
        }catch (Exception e){
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @PutMapping(path = "${EDIT_HOTEL_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> editComment(@PathVariable("hotelId") long hotelId,
                                                            @PathVariable("commentId") long commentId,
                                                            @RequestBody EditHotelEvaluationDto editHotelEvaluationDto){
        try {
            return hotelEvaluationService.editComment(hotelId, commentId, editHotelEvaluationDto);
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture(notFoundException(e));
        }catch (Exception e){
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @DeleteMapping(path = "${REMOVE_HOTEL_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> deleteComment(@PathVariable("hotelId") long hotelId,
                                                              @PathVariable("commentId") long commentId){
        try {
            return hotelEvaluationService.removeComment(hotelId, commentId);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }
}
