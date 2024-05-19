package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.dto.hotelDto.RoomDetailsDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
import com.hackathon.backend.utilities.hotel.features.RoomFeaturesUtils;
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

    private final CountryUtils countryUtils;
    private final RoomUtils roomUtils;
    private final HotelUtils hotelUtils;
    private final HotelFeaturesUtils hotelFeaturesUtils;
    private final RoomFeaturesUtils roomFeaturesUtils;
    private final HotelEvaluationUtils hotelEvaluationUtils;
    private final RoomDetailsUtils roomDetailsUtils;

    @Autowired
    public HotelService(CountryUtils countryUtils,
                        RoomUtils roomUtils,
                        HotelUtils hotelUtils,
                        HotelFeaturesUtils hotelFeaturesUtils,
                        RoomFeaturesUtils roomFeaturesUtils,
                        HotelEvaluationUtils hotelEvaluationUtils,
                        RoomDetailsUtils roomDetailsUtils){
        this.countryUtils = countryUtils;
        this.roomUtils = roomUtils;
        this.hotelUtils = hotelUtils;
        this.hotelFeaturesUtils = hotelFeaturesUtils;
        this.roomFeaturesUtils = roomFeaturesUtils;
        this.hotelEvaluationUtils = hotelEvaluationUtils;
        this.roomDetailsUtils = roomDetailsUtils;
    }

    public ResponseEntity<?> createHotel(int countryId,
                                         @NonNull HotelDto hotelDto) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);
            HotelEntity hotelEntity = new HotelEntity(
                    hotelDto.getHotelName(),
                    hotelDto.getMainImage(),
                    hotelDto.getDescription(),
                    hotelDto.getHotelRoomsCount(),
                    hotelDto.getAddress(),
                    country
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

            hotelEntity.setRoomDetails(roomDetails);
            hotelUtils.save(hotelEntity);
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
    public ResponseEntity<?> editHotel(long hotelId,
                                       int countryId,
                                       HotelDto hotelDto){
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            CountryEntity country = countryUtils.findCountryById(countryId);
            hotel.setCountry(country);
            editHelper(hotel, hotelDto);
            hotelUtils.save(hotel);
            countryUtils.save(country);
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
            RoomDetailsEntity roomDetails = hotel.getRoomDetails();
            CountryEntity country = hotel.getCountry();

            for(RoomEntity room: hotel.getRooms()){
                roomUtils.delete(room);
            }

            country.getHotels().remove(hotel);
            countryUtils.save(country);

            for(HotelEvaluationEntity evaluation:hotel.getEvaluations()){
                hotelEvaluationUtils.delete(evaluation);
            }

            List<HotelFeaturesEntity> hotelFeatures = roomDetails.getHotelFeatures();

            for(HotelFeaturesEntity hotelFeature:hotelFeatures){
                roomDetails.getHotelFeatures().remove(hotelFeature);
                hotelFeaturesUtils.save(hotelFeature);
            }

            List<RoomFeaturesEntity> roomFeatures = roomDetails.getRoomFeatures();

            for(RoomFeaturesEntity roomFeature:roomFeatures){
                roomDetails.getRoomFeatures().remove(roomFeature);
                roomFeaturesUtils.save(roomFeature);
            }

            roomDetailsUtils.delete(roomDetails);
            hotelUtils.delete(hotel);
            return ResponseEntity.ok("Hotel deleted Successfully");
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
