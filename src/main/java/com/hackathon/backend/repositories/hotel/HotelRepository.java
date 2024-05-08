package com.hackathon.backend.repositories.hotel;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    @Query("SELECT new com.hackathon.backend.dto.hotelDto.HotelDto(h.id, h.hotelName, h.mainImage, h.description, h.address, h.rate)" +
            " FROM HotelEntity h WHERE h.country.id = :countryId"
    )
    List<HotelDto> findByCountryId(int countryId);
}