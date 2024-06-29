package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.EditHotelDto;
import com.hackathon.backend.dto.hotelDto.PostHotelDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
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

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class HotelService {

    private final CountryUtils countryUtils;
    private final RoomUtils roomUtils;
    private final HotelUtils hotelUtils;
    private final HotelFeaturesUtils hotelFeaturesUtils;
    private final RoomFeaturesUtils roomFeaturesUtils;
    private final HotelEvaluationUtils hotelEvaluationUtils;
    private final RoomDetailsUtils roomDetailsUtils;

    private final S3Service s3Service;

    @Autowired
    public HotelService(CountryUtils countryUtils,
                        RoomUtils roomUtils,
                        HotelUtils hotelUtils,
                        HotelFeaturesUtils hotelFeaturesUtils,
                        RoomFeaturesUtils roomFeaturesUtils,
                        HotelEvaluationUtils hotelEvaluationUtils,
                        RoomDetailsUtils roomDetailsUtils,
                        S3Service s3Service){
        this.countryUtils = countryUtils;
        this.roomUtils = roomUtils;
        this.hotelUtils = hotelUtils;
        this.hotelFeaturesUtils = hotelFeaturesUtils;
        this.roomFeaturesUtils = roomFeaturesUtils;
        this.hotelEvaluationUtils = hotelEvaluationUtils;
        this.roomDetailsUtils = roomDetailsUtils;
        this.s3Service = s3Service;
    }

    public ResponseEntity<String> createHotel(int countryId,
                                         @NonNull PostHotelDto postHotelDto) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);

            String hotelImageName = s3Service.uploadFile(postHotelDto.getMainImage());

            HotelEntity hotelEntity = new HotelEntity(
                    postHotelDto.getHotelName(),
                    hotelImageName,
                    postHotelDto.getDescription(),
                    postHotelDto.getHotelRoomsCount(),
                    postHotelDto.getAddress(),
                    postHotelDto.getRate(),
                    country
            );

            hotelUtils.save(hotelEntity);

            String roomDetailsImageNameOne = s3Service.uploadFile(postHotelDto.getImageOne());
            String roomDetailsImageNameTwo = s3Service.uploadFile(postHotelDto.getImageTwo());
            String roomDetailsImageNameThree = s3Service.uploadFile(postHotelDto.getImageThree());
            String roomDetailsImageNameFour = s3Service.uploadFile(postHotelDto.getImageFour());

            RoomDetailsEntity roomDetails = new RoomDetailsEntity(
                    roomDetailsImageNameOne,
                    roomDetailsImageNameTwo,
                    roomDetailsImageNameThree,
                    roomDetailsImageNameFour,
                    postHotelDto.getRoomDescription(),
                    postHotelDto.getPrice(),
                    hotelEntity
            );

            roomDetailsUtils.save(roomDetails);

            hotelEntity.setRoomDetails(roomDetails);
            hotelUtils.save(hotelEntity);
            return ResponseEntity.ok("Hotel created successfully: " + postHotelDto.getHotelName());
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }


    public ResponseEntity<?> getHotels(int countryId) {
        try{
            List<GetHotelDto> hotels = hotelUtils.findByCountryId(countryId);
            return ResponseEntity.ok(hotels);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> editHotel(long hotelId,
                                       EditHotelDto editHotelDto){
        try{
            if(!hotelUtils.checkHelper(editHotelDto)){
                return badRequestException("you sent an empty data to change");
            }
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            hotelUtils.editHelper(hotel, editHotelDto);
            hotelUtils.save(hotel);
            return ResponseEntity.ok("Hotel updated Successfully to: "+hotel.getHotelName());
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteHotel(long hotelId) {
        try {
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);

            if (hotel == null) {
                throw new EntityNotFoundException("Hotel not found");
            }

            RoomDetailsEntity roomDetails = hotel.getRoomDetails();
            CountryEntity country = hotel.getCountry();

            if (country != null) {
                country.getHotels().remove(hotel);
                countryUtils.save(country);
            }

            for (RoomEntity room : hotel.getRooms()) {
                roomUtils.delete(room);
            }

            for (HotelEvaluationEntity evaluation : hotel.getEvaluations()) {
                hotelEvaluationUtils.delete(evaluation);
            }

            if (roomDetails != null) {
                List<HotelFeaturesEntity> hotelFeatures = roomDetails.getHotelFeatures();
                if (hotelFeatures != null) {
                    for (HotelFeaturesEntity hotelFeature : hotelFeatures) {
                        roomDetails.getHotelFeatures().remove(hotelFeature);
                        hotelFeaturesUtils.save(hotelFeature);
                    }
                }

                List<RoomFeaturesEntity> roomFeatures = roomDetails.getRoomFeatures();
                if (roomFeatures != null) {
                    for (RoomFeaturesEntity roomFeature : roomFeatures) {
                        roomDetails.getRoomFeatures().remove(roomFeature);
                        roomFeaturesUtils.save(roomFeature);
                    }
                }

                String[] ls = new String[]{
                        roomDetails.getImageOne(),
                        roomDetails.getImageTwo(),
                        roomDetails.getImageThree(),
                        roomDetails.getImageFour()
                };

                s3Service.deleteFiles(ls);
                roomDetailsUtils.delete(roomDetails);
            }

            s3Service.deleteFile(hotel.getMainImage());
            hotelUtils.delete(hotel);
            return ResponseEntity.ok("Hotel deleted Successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}
