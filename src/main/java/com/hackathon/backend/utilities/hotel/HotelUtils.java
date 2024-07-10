package com.hackathon.backend.utilities.hotel;

import com.hackathon.backend.dto.hotelDto.EditHotelDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.dto.hotelDto.GetRoomsDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HotelUtils {

    private final HotelRepository hotelRepository;
    private final S3Service s3Service;
    private final CountryUtils countryUtils;

    @Autowired
    public HotelUtils(HotelRepository hotelRepository,
                      S3Service s3Service,
                      CountryUtils countryUtils) {
        this.hotelRepository = hotelRepository;
        this.s3Service = s3Service;
        this.countryUtils = countryUtils;
    }

    public HotelEntity findHotelById(long hotelId){
        return hotelRepository.findById(hotelId)
                .orElseThrow(()-> new EntityNotFoundException("Hotel id not found"));
    }
    public List<GetRoomsDto> findRoomsByHotelId(long hotelId){
        return hotelRepository.findRoomsByHotelId(hotelId)
                .orElseThrow(()-> new EntityNotFoundException("Hotel id not found"));
    }


    public void save(HotelEntity hotelEntity) {
        hotelRepository.save(hotelEntity);
    }

    public Page<GetHotelDto> findByCountryId(int countryId, Pageable pageable) {
        return hotelRepository.findByCountryId(countryId, pageable);
    }

    public void delete(HotelEntity hotel) {
        hotelRepository.delete(hotel);
    }

    public boolean checkHelper(EditHotelDto editHotelDto){
        return  editHotelDto.getHotelName() != null ||
                editHotelDto.getMainImage() != null ||
                editHotelDto.getDescription() != null ||
                editHotelDto.getHotelRoomsCount() != null ||
                editHotelDto.getAddress() != null ||
                editHotelDto.getPrice() != null ||
                editHotelDto.getRate() != null;
    }

    public void editHelper(HotelEntity hotel, EditHotelDto editHotelDto) {
        if (editHotelDto.getHotelName() != null) {
            hotel.setHotelName(editHotelDto.getHotelName());
        }
        if (editHotelDto.getMainImage() != null) {
            s3Service.deleteFile(hotel.getMainImage());
            String hotelMainImageName = s3Service.uploadFile(editHotelDto.getMainImage());
            hotel.setMainImage(hotelMainImageName);
        }
        if (editHotelDto.getDescription() != null) {
            hotel.setDescription(editHotelDto.getDescription());
        }
        if (editHotelDto.getHotelRoomsCount() != null) {
            hotel.setHotelRoomsCount(editHotelDto.getHotelRoomsCount());
        }
        if (editHotelDto.getAddress() != null) {
            hotel.setAddress(editHotelDto.getAddress());
        }
        if (editHotelDto.getRate() != null && editHotelDto.getRate() > 0 && editHotelDto.getRate() <= 5) {
            hotel.setRate(editHotelDto.getRate());
        }
        if (editHotelDto.getCountryId() != null) {
            CountryEntity country = countryUtils.findCountryById(editHotelDto.getCountryId());
            hotel.setCountry(country);
        }
    }
}
