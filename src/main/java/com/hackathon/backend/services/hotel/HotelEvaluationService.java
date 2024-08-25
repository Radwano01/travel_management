package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.evaluationDto.CreateHotelEvaluationDto;
import com.hackathon.backend.dto.hotelDto.evaluationDto.EditHotelEvaluationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.concurrent.CompletableFuture;

public interface HotelEvaluationService {

    CompletableFuture<ResponseEntity<?>> addComment
            (long hotelId, long userId, @NonNull CreateHotelEvaluationDto createHotelEvaluationDto);

    CompletableFuture<ResponseEntity<?>> getComments(long hotelId);

    CompletableFuture<ResponseEntity<?>> editComment
            (long hotelId, long commentId, EditHotelEvaluationDto editHotelEvaluationDto);

    CompletableFuture<ResponseEntity<?>> removeComment(long hotelId, long commentId);
}

