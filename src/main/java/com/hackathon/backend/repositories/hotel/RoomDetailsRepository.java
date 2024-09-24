package com.hackathon.backend.repositories.hotel;

import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.GetRoomFeaturesDto;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDetailsRepository extends JpaRepository<RoomDetailsEntity, Long> {

    @Query("SELECT new com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto(hf.id, hf.hotelFeatures)" +
            " FROM RoomDetailsEntity rd JOIN rd.hotelFeatures hf WHERE rd.hotel.id = :hotelId")
    List<GetHotelFeaturesDto> findRoomDetailsHotelFeaturesByHotelId(long hotelId);

    @Query("SELECT new com.hackathon.backend.dto.hotelDto.features.GetRoomFeaturesDto(rf.id, rf.roomFeatures)" +
            " FROM RoomDetailsEntity rd JOIN rd.roomFeatures rf WHERE rd.hotel.id = :hotelId")
    List<GetRoomFeaturesDto> findRoomDetailsRoomFeaturesByHotelId(long hotelId);
}
