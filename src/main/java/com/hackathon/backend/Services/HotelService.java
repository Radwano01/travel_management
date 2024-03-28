package com.hackathon.backend.Services;


import com.hackathon.backend.Dto.HotelDto.HotelDto;
import com.hackathon.backend.Entities.CountryEntity;
import com.hackathon.backend.Entities.HotelEntity;
import com.hackathon.backend.Repositories.CountryRepository;
import com.hackathon.backend.Repositories.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final CountryRepository countryRepository;

    public HotelService(HotelRepository hotelRepository,
                        CountryRepository countryRepository){
        this.hotelRepository = hotelRepository;

        this.countryRepository = countryRepository;
    }

    public ResponseEntity<?> createHotel(HotelDto hotelDto) {
        try{
            boolean existsHotel = hotelRepository.existsByHotelName(hotelDto.getHotelName());
            if(!existsHotel){
                CountryEntity country = getCountry(hotelDto.getCountry());
                HotelEntity hotelEntity = new HotelEntity();
                hotelEntity.setHotelName(hotelDto.getHotelName());
                hotelEntity.setCountry(country);
                hotelRepository.save(hotelEntity);
                return new ResponseEntity<>("Hotel created Successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Hotel already Valid", HttpStatus.CONFLICT);
            }

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllHotels(){
        try{
            List<HotelEntity> hotelEntity = hotelRepository.findAll();
            return new ResponseEntity<>(hotelEntity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getSingleHotel(int hotelID) {
        try{
            HotelEntity hotelEntity = hotelRepository.findById(hotelID)
                    .orElseThrow(()-> new EntityNotFoundException("Hotel Id is Not Found: "+hotelID));
            return  new ResponseEntity<>(hotelEntity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getHotelInfo() {
        try{
            List<HotelEntity> hotelEntityList = hotelRepository.findAll();
            List<HotelDto> dto = new ArrayList<>();
            if(!hotelEntityList.isEmpty()){
                for(HotelEntity hotel:hotelEntityList) {
                    HotelDto hotelDto = new HotelDto();
                    hotelDto.setId(hotel.getId());
                    hotelDto.setHotelName(hotel.getHotelName());
                    hotelDto.setCountry(hotel.getCountry().getCountry());
                    dto.add(hotelDto);
                }
                return new ResponseEntity<>(dto, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Hotels Data is Empty", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editHotel(int hotelID,HotelDto hotelDto){
        try{
            HotelEntity hotelEntity = hotelRepository.findById(hotelID)
                    .orElseThrow(()-> new EntityNotFoundException("Hotel Id is Not Found"));
            CountryEntity country = getCountry(hotelDto.getCountry());
            if(hotelDto.getHotelName() != null){
            hotelEntity.setHotelName(hotelDto.getHotelName());
            }
            if(hotelDto.getCountry() != null){
                hotelEntity.setCountry(country);
            }
            return new ResponseEntity<>("Hotel updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteHotel(int hotelID) {
        try{
            hotelRepository.deleteById(hotelID);
            return new ResponseEntity<>("Hotel deleted Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CountryEntity getCountry(String country){
        return countryRepository.findByCountry(country)
                .orElseThrow(()-> new EntityNotFoundException("Country is Not Found"));
    }
}
