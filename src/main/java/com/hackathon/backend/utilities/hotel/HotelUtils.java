package com.hackathon.backend.utilities.hotel;

import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.utilities.country.CountryUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public void save(HotelEntity hotelEntity) {
        hotelRepository.save(hotelEntity);
    }

    public List<GetHotelDto> findByCountryId(int countryId) {
        return hotelRepository.findByCountryId(countryId);
    }

    public void delete(HotelEntity hotel) {
        hotelRepository.delete(hotel);
    }
}
