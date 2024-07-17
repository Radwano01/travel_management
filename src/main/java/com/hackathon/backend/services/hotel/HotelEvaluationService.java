package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.EditHotelEvaluationDto;
import com.hackathon.backend.dto.hotelDto.HotelEvaluationDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.utilities.user.UserUtils;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class HotelEvaluationService {

    private final HotelUtils hotelUtils;
    private final UserUtils userUtils;
    private final HotelEvaluationUtils hotelEvaluationUtils;

    @Autowired
    public HotelEvaluationService(UserUtils userUtils,
                                  HotelEvaluationUtils hotelEvaluationUtils,
                                  HotelUtils hotelUtils) {
        this.userUtils = userUtils;
        this.hotelEvaluationUtils = hotelEvaluationUtils;
        this.hotelUtils = hotelUtils;
    }

    @Async("commentTaskExecutor")
    @Transactional
    public ResponseEntity<String> addComment(long hotelId, long userId,
                                        @NonNull HotelEvaluationDto
                                                hotelEvaluationDto) {
        try{
            boolean existsUser = hotelEvaluationUtils.existsCommentByUserId(userId);
            if(existsUser){
                return alreadyValidException("This user already has a comment");
            }
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            UserEntity user = userUtils.findById(userId);
            HotelEvaluationEntity hotelEvaluation = new HotelEvaluationEntity(
                    hotelEvaluationDto.getComment(),
                    hotelEvaluationDto.getRate(),
                    hotel,
                    user

            );
            hotelEvaluationUtils.save(hotelEvaluation);

            hotel.getEvaluations().add(hotelEvaluation);
            hotelEvaluationUtils.save(hotelEvaluation);

            user.getHotelEvaluations().add(hotelEvaluation);
            userUtils.save(user);
            return ResponseEntity.ok("Comment added successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getComments(long hotelId){
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            List<HotelEvaluationEntity> hotelEvaluations = hotel.getEvaluations();

            List<HotelEvaluationDto> hotelEvaluationDtos = new ArrayList<>();

            for(HotelEvaluationEntity hotelEvaluation:hotelEvaluations){
                HotelEvaluationDto hotelEvaluationDto = new HotelEvaluationDto(
                        hotelEvaluation.getId(),
                        hotelEvaluation.getComment(),
                        hotelEvaluation.getRate(),
                        hotelEvaluation.getUser().getId(),
                        hotelEvaluation.getUser().getUsername(),
                        hotelEvaluation.getUser().getImage()
                );
                hotelEvaluationDtos.add(hotelEvaluationDto);
            }

            return ResponseEntity.ok(hotelEvaluationDtos);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Async("commentTaskExecutor")
    @Transactional
    public ResponseEntity<String> editComment(long commentId,
                                         EditHotelEvaluationDto editHotelEvaluationDto) {
        try{
            if(!hotelEvaluationUtils.checkHelper(editHotelEvaluationDto)){
                return badRequestException("you sent an empty data to change");
            }
            HotelEvaluationEntity hotelEvaluation = hotelEvaluationUtils.findById(commentId);
            hotelEvaluationUtils.editHelper(hotelEvaluation, editHotelEvaluationDto);
            hotelEvaluationUtils.save(hotelEvaluation);
            return ResponseEntity.ok("Comment updated successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Async("commentTaskExecutor")
    @Transactional
    public ResponseEntity<String> removeComment(long commentId) {
        try {
            HotelEvaluationEntity hotelEvaluation = hotelEvaluationUtils.findById(commentId);
            if(hotelEvaluation == null) {
                return badRequestException("Comment is not found");
            }
            HotelEntity hotel = hotelEvaluation.getHotel();
            UserEntity user = hotelEvaluation.getUser();

            hotel.getEvaluations().remove(hotelEvaluation);
            hotelUtils.save(hotel);

            user.getHotelEvaluations().remove(hotelEvaluation);
            userUtils.save(user);

            hotelEvaluationUtils.delete(hotelEvaluation);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

}
