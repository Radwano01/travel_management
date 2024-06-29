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
import org.springframework.stereotype.Service;

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
            hotel.getEvaluations().add(hotelEvaluation);
            hotelEvaluationUtils.save(hotelEvaluation);
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
            List<HotelEvaluationDto> hotelEvaluationDto = hotelEvaluations.stream()
                    .map((evaluation)-> new HotelEvaluationDto(
                            evaluation.getId(),
                            evaluation.getComment(),
                            evaluation.getRate(),
                            evaluation.getUser().getUsername(),
                            evaluation.getUser().getImage()
                    )).toList();
            return ResponseEntity.ok(hotelEvaluationDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

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

    @Transactional
    public ResponseEntity<String> removeComment(long hotelId,
                                           long userId,
                                           long commentId) {
        try {
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            UserEntity user = userUtils.findById(userId);
            HotelEvaluationEntity hotelEvaluation = hotelEvaluationUtils.findById(commentId);
            if(hotel != null && user != null && hotelEvaluation != null){
                hotel.getEvaluations().remove(hotelEvaluation);
                user.getEvaluations().remove(hotelEvaluation);
                hotelUtils.save(hotel);
                userUtils.save(user);
                hotelEvaluationUtils.delete(hotelEvaluation);
                return ResponseEntity.ok("Comment deleted successfully");
            }else{
                return notFoundException("Hotel or User or Hotel Evaluation not found");
            }
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

}
