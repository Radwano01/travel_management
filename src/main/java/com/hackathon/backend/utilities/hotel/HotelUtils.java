package com.hackathon.backend.utilities.hotel;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.utilities.country.CountryUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HotelUtils {

    private final HotelRepository hotelRepository;
    private final CountryUtils countryUtils;

    @Autowired
    public HotelUtils(HotelRepository hotelRepository,
                      CountryUtils countryUtils) {
        this.hotelRepository = hotelRepository;
        this.countryUtils = countryUtils;
    }

    public HotelEntity findHotelById(@NonNull long hotelId){
        return hotelRepository.findById(hotelId)
                .orElseThrow(()-> new EntityNotFoundException("Hotel id not found"));
    }

    public PlaceEntity findPlaceByIdInCountry(@NonNull int countryId,
                                      @NonNull int placeId){
        Optional<PlaceEntity> foundPlace = countryUtils.findCountryById(countryId)
                .getPlaces().stream()
                .filter((place)-> place.getId() == placeId)
                .findFirst();
        return foundPlace.orElse(null);
    }

    public void save(HotelEntity hotelEntity) {
        hotelRepository.save(hotelEntity);
    }

    public List<HotelDto> findByCountryId(int countryId) {
        return hotelRepository.findByCountryId(countryId);
    }

    public void delete(HotelEntity hotel) {
        hotelRepository.delete(hotel);
    }

    public void deleteAll() {
        hotelRepository.deleteAll();
    }
}
