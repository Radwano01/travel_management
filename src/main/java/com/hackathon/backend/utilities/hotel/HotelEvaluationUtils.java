package com.hackathon.backend.utilities.hotel;

import com.hackathon.backend.dto.hotelDto.EditHotelEvaluationDto;
import com.hackathon.backend.dto.hotelDto.HotelEvaluationDto;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.repositories.hotel.HotelEvaluationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HotelEvaluationUtils {

    private final HotelEvaluationRepository hotelEvaluationRepository;

    @Autowired
    public HotelEvaluationUtils(HotelEvaluationRepository hotelEvaluationRepository) {
        this.hotelEvaluationRepository = hotelEvaluationRepository;
    }

    public boolean existsCommentByUserId(long userId) {
        return hotelEvaluationRepository.existsCommentByUserId(userId);
    }

    public void save(HotelEvaluationEntity hotelEvaluation) {
        hotelEvaluationRepository.save(hotelEvaluation);
    }

    public HotelEvaluationEntity findById(long commentId) {
        return hotelEvaluationRepository.findById(commentId)
                .orElseThrow(()-> new EntityNotFoundException("Comment id not found"));
    }

    public void delete(HotelEvaluationEntity hotelEvaluation) {
        hotelEvaluationRepository.delete(hotelEvaluation);
    }

    public boolean checkHelper(EditHotelEvaluationDto editHotelEvaluationDto){
        return  editHotelEvaluationDto.getComment() != null ||
                editHotelEvaluationDto.getRate() != null;
    }

    public void editHelper(HotelEvaluationEntity hotelEvaluation,
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
}
