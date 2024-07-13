package com.hackathon.backend.repositories.hotel;

import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.dto.hotelDto.GetRoomsDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    @Query("SELECT new com.hackathon.backend.dto.hotelDto.GetHotelDto(h.id, h.hotelName, h.mainImage, h.description, h.address, h.rate)" +
            " FROM HotelEntity h WHERE h.country.id = :countryId"
    )
    Page<GetHotelDto> findByCountryId(int countryId, Pageable pageable);

    @Query("SELECT new com.hackathon.backend.dto.hotelDto.GetRoomsDto(r.id, r.status) FROM HotelEntity h JOIN h.rooms r WHERE h.id = :hotelId")
    Optional<List<GetRoomsDto>> findRoomsByHotelId(long hotelId);
}