package com.hackathon.backend.repositories.country;

import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.dto.planeDto.GetAirPortDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity,Integer> {

    @Query("SELECT new com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto" +
            "(p.id, p.place, ap.airPortName) " +
            "FROM PlaceEntity p " +
            "JOIN p.airPorts ap " +
            "WHERE p.place = :place")
    List<GetPlaceForFlightDto> findPlaceByPlace(String place);

    @Query("SELECT new com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto" +
            "(p.id, p.place, p.mainImage, d.imageOne, d.imageTwo, d.imageThree, d.description)" +
            "FROM PlaceEntity p JOIN p.placeDetails d WHERE p.id = :placeId")
    GetPlaceDetailsDto findPlaceWithPlaceDetailsByPlaceId(int placeId);

    @Query("SELECT new com.hackathon.backend.dto.hotelDto.GetHotelDto" +
            "(h.id, h.hotelName, h.mainImage, h.description, h.address, h.rate)" +
            " FROM PlaceEntity p JOIN p.hotels h WHERE p.id = :placeId")
    Page<GetHotelDto> findHotelByPlaceId(int placeId, Pageable pageable);

    @Query("SELECT new com.hackathon.backend.dto.planeDto.GetAirPortDto" +
            "(a.id, a.airPortName, a.airPortCode)" +
            " FROM PlaceEntity p JOIN p.airPorts a WHERE p.id = :placeId")
    List<GetAirPortDto> findAllAirportsByPlaceId(int placeId);
}
