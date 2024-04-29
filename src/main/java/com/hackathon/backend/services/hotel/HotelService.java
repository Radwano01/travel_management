package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.dto.hotelDto.RoomDetailsDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class HotelService {

    private final PlaceUtils placeUtils;
    private final CountryUtils countryUtils;
    private final RoomUtils roomUtils;
    private final HotelUtils hotelUtils;
    private final RoomDetailsUtils roomDetailsUtils;

    @Autowired
    public HotelService(PlaceUtils placeUtils,
                        CountryUtils countryUtils,
                        RoomUtils roomUtils,
                        HotelUtils hotelUtils,
                        RoomDetailsUtils roomDetailsUtils){
        this.placeUtils = placeUtils;
        this.countryUtils = countryUtils;
        this.roomUtils = roomUtils;
        this.hotelUtils = hotelUtils;
        this.roomDetailsUtils = roomDetailsUtils;
    }

    public ResponseEntity<?> createHotel(int countryId,
                                         int placeId,
                                         @NonNull HotelDto hotelDto) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);
            PlaceEntity place = placeUtils.findById(placeId);
            HotelEntity hotelEntity = new HotelEntity(
                    hotelDto.getHotelName(),
                    hotelDto.getMainImage(),
                    hotelDto.getTitle(),
                    hotelDto.getDescription(),
                    hotelDto.getHotelRoomsCount(),
                    hotelDto.getAddress(),
                    country,
                    place
            );
            hotelUtils.save(hotelEntity);

            RoomDetailsDto roomDetailsDto = hotelDto.getRoomDetails();
            RoomDetailsEntity roomDetails = new RoomDetailsEntity(
                    roomDetailsDto.getImageOne(),
                    roomDetailsDto.getImageTwo(),
                    roomDetailsDto.getImageThree(),
                    roomDetailsDto.getImageFour(),
                    roomDetailsDto.getDescription(),
                    roomDetailsDto.getPrice(),
                    hotelEntity
            );
            roomDetailsUtils.save(roomDetails);
            return ResponseEntity.ok("Hotel created successfully: " + hotelDto.getHotelName());
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }


    public ResponseEntity<?> getHotels(int countryId) {
        try{
            List<HotelDto> hotels = hotelUtils.findByCountryId(countryId);
            return ResponseEntity.ok(hotels);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editHotel(long hotelId, int countryId,
                                       int placeId, HotelDto hotelDto){
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            if(countryId != hotel.getCountry().getId()){
                CountryEntity country = countryUtils.findCountryById(countryId);
                hotel.setCountry(country);
            }
            if(placeId != hotel.getPlace().getId()){
                PlaceEntity place = hotelUtils.findPlaceByIdInCountry(countryId,placeId);
                hotel.setPlace(place);
            }
            editHelper(hotel, hotelDto);
            hotelUtils.save(hotel);
            return ResponseEntity.ok("Hotel updated Successfully: "+hotel.getHotelName());
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteHotel(long hotelId) {
        try {
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            RoomDetailsEntity roomDetails = roomDetailsUtils.findByHotelId(hotelId);
            if(hotel != null && roomDetails != null){
                for(RoomEntity room: hotel.getRooms()){
                    roomUtils.delete(room);
                }
                hotelUtils.delete(hotel);
                roomDetailsUtils.deleteByHotelId(hotelId);
                return ResponseEntity.ok("Hotel deleted Successfully");
            }else{
                return notFoundException("Hotel not found");
            }
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(HotelEntity hotel,
                            HotelDto hotelDto) {
        if (hotelDto.getHotelName() != null) {
            hotel.setHotelName(hotelDto.getHotelName());
        }
        if (hotelDto.getMainImage() != null) {
            hotel.setMainImage(hotelDto.getMainImage());
        }
        if (hotelDto.getTitle() != null) {
            hotel.setTitle(hotelDto.getTitle());
        }
        if (hotelDto.getDescription() != null) {
            hotel.setDescription(hotelDto.getDescription());
        }
        if (hotel.getHotelRoomsCount() >= hotelDto.getHotelRoomsCount()) {
            hotel.setHotelRoomsCount(hotelDto.getHotelRoomsCount());
        }
        if (hotelDto.getAddress() != null) {
            hotel.setAddress(hotelDto.getAddress());
        }
        if (hotelDto.getRate() != hotel.getRate()) {
            hotel.setRate(hotelDto.getRate());
        }
    }
}
