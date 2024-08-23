package com.hackathon.backend.repositories.hotel;

import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.dto.hotelDto.evaluationDto.GetHotelEvaluationDto;
import com.hackathon.backend.dto.hotelDto.GetRoomsDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    @Query("SELECT new com.hackathon.backend.dto.hotelDto.evaluationDto.GetHotelEvaluationDto" +
            "(e.id, e.comment, e.rate, e.user.id, e.user.username, e.user.image) " +
            "FROM HotelEntity h JOIN h.evaluations e WHERE h.id = :hotelId")
    List<GetHotelEvaluationDto> findAllHotelEvaluationsByHotelId(long hotelId);

}