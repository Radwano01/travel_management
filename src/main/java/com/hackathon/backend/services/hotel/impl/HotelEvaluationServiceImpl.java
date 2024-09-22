package com.hackathon.backend.services.hotel.impl;

import com.hackathon.backend.dto.hotelDto.evaluationDto.CreateHotelEvaluationDto;
import com.hackathon.backend.dto.hotelDto.evaluationDto.EditHotelEvaluationDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.services.hotel.HotelEvaluationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class HotelEvaluationServiceImpl implements HotelEvaluationService {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    @Autowired
    public HotelEvaluationServiceImpl(HotelRepository hotelRepository,
                                      UserRepository userRepository) {
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
    }

    @Async("commentTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> addComment(long hotelId, long userId,
                                                           @NonNull CreateHotelEvaluationDto createHotelEvaluationDto){
        HotelEntity hotel = findHotelById(hotelId);

        ResponseEntity<String> checkResult = checkIfUserAlreadyCommented(hotel, userId);
        if(!checkResult.getStatusCode().equals(HttpStatus.OK)){
            return CompletableFuture.completedFuture(checkResult);
        }

        UserEntity user = getUserById(userId);

        prepareANDSetEvaluationHotel(createHotelEvaluationDto, hotel, user);

        hotelRepository.save(hotel);

        return CompletableFuture.completedFuture
                (ResponseEntity.ok("Comment added successfully " + createHotelEvaluationDto.getComment()));
    }

    private ResponseEntity<String> checkIfUserAlreadyCommented(HotelEntity hotel, long userId){
        for(HotelEvaluationEntity hotelEvaluation : hotel.getEvaluations()){
            UserEntity user = hotelEvaluation.getUser();
            if(user != null && user.getId() == userId){
                return badRequestException("This user has already commented on this hotel");
            }
        }
        return ResponseEntity.ok("OK");
    }

    private HotelEntity findHotelById(long hotelId){
        return hotelRepository.findById(hotelId)
                .orElseThrow(()-> new EntityNotFoundException("Hotel id not found"));
    }

    private UserEntity getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User id not found"));
    }

    @Async("commentTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> getComments(long hotelId){
        return CompletableFuture.completedFuture(
                ResponseEntity.ok(hotelRepository.findAllHotelEvaluationsByHotelId(hotelId)));
    }

    @Async("commentTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> editComment(long hotelId, long commentId,
                                                            EditHotelEvaluationDto editHotelEvaluationDto) {
        if(!checkIfSentEmptyData(editHotelEvaluationDto)){
            return CompletableFuture.completedFuture(badRequestException("you sent an empty data to change"));
        }

        HotelEntity hotel = findHotelById(hotelId);

        HotelEvaluationEntity hotelEvaluation = getHotelEvaluationFromHotel(hotel, commentId);

        updateToNewData(hotelEvaluation, editHotelEvaluationDto);

        hotelRepository.save(hotel);

        return CompletableFuture.completedFuture
                (ResponseEntity.ok("Comment edited successfully" + hotelEvaluation.getComment()));
    }

    private void updateToNewData(HotelEvaluationEntity hotelEvaluation,
                                 EditHotelEvaluationDto editHotelEvaluationDto){
        if(editHotelEvaluationDto.getComment() != null){
            hotelEvaluation.setComment(editHotelEvaluationDto.getComment());
        }
        if(editHotelEvaluationDto.getRate() != null &&
                editHotelEvaluationDto.getRate() >= 0 &&
                editHotelEvaluationDto.getRate() <= 5){
            hotelEvaluation.setRate(editHotelEvaluationDto.getRate());
        }
    }

    @Async("commentTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> removeComment(long hotelId, long commentId) {
        HotelEntity hotel = findHotelById(hotelId);

        HotelEvaluationEntity hotelEvaluation = getHotelEvaluationFromHotel(hotel, commentId);

        hotel.getEvaluations().remove(hotelEvaluation);

        hotelRepository.save(hotel);

        return CompletableFuture.completedFuture(ResponseEntity.ok("Comment deleted successfully"));
    }



    private HotelEvaluationEntity getHotelEvaluationFromHotel(HotelEntity hotel, long commentId){
        return hotel.getEvaluations().stream()
                .filter((data) -> data.getId() == commentId).findFirst()
                .orElseThrow(()-> new EntityNotFoundException("No such hotel has this comment id"));
    }

    private void prepareANDSetEvaluationHotel(CreateHotelEvaluationDto createHotelEvaluationDto,
                                              HotelEntity hotel, UserEntity userId){
        HotelEvaluationEntity hotelEvaluation = new HotelEvaluationEntity(
                createHotelEvaluationDto.getComment(),
                createHotelEvaluationDto.getRate(),
                hotel,
                userId
        );

        hotel.getEvaluations().add(hotelEvaluation);
    }
}
