package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.EditHotelDto;
import com.hackathon.backend.dto.hotelDto.PostHotelDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class HotelService {

    private final PlaceUtils placeUtils;
    private final RoomUtils roomUtils;
    private final HotelUtils hotelUtils;
    private final HotelEvaluationUtils hotelEvaluationUtils;
    private final RoomDetailsUtils roomDetailsUtils;

    private final S3Service s3Service;

    @Autowired
    public HotelService(PlaceUtils placeUtils,
                        RoomUtils roomUtils,
                        HotelUtils hotelUtils,
                        HotelEvaluationUtils hotelEvaluationUtils,
                        RoomDetailsUtils roomDetailsUtils,
                        S3Service s3Service){
        this.placeUtils = placeUtils;
        this.roomUtils = roomUtils;
        this.hotelUtils = hotelUtils;
        this.hotelEvaluationUtils = hotelEvaluationUtils;
        this.roomDetailsUtils = roomDetailsUtils;
        this.s3Service = s3Service;
    }

    @Transactional
    public ResponseEntity<String> createHotel(int countryId,
                                              @NonNull PostHotelDto postHotelDto) {
        try {
            PlaceEntity place = placeUtils.findById(countryId);

            String hotelImageName = s3Service.uploadFile(postHotelDto.getMainImage());

            HotelEntity hotelEntity = new HotelEntity(
                    postHotelDto.getHotelName(),
                    hotelImageName,
                    postHotelDto.getDescription(),
                    postHotelDto.getHotelRoomsCount(),
                    postHotelDto.getAddress(),
                    postHotelDto.getRate(),
                    place
            );

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

            boolean existsRoomDetails = roomDetailsUtils.existsById(roomDetails.getId());
            if (!existsRoomDetails) {
                return notFoundException("Room Details is not created");
            }

            for (int i = 0; i < postHotelDto.getHotelRoomsCount(); i++) {
                RoomEntity roomEntity = new RoomEntity(hotelEntity);
                roomUtils.save(roomEntity);
                hotelEntity.getRooms().add(roomEntity);
            }

            hotelUtils.save(hotelEntity);
            return ResponseEntity.ok("Hotel created successfully: " + postHotelDto.getHotelName());
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getHotels(int placeId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<GetHotelDto> hotels = hotelUtils.findByPlaceId(placeId, pageable);

            List<GetHotelDto> hotelDtoList = new ArrayList<>();
            for (GetHotelDto hotel : hotels) {
                GetHotelDto getHotelDto1 = new GetHotelDto(
                        hotel.getId(),
                        hotel.getHotelName(),
                        hotel.getMainImage(),
                        hotel.getDescription(),
                        hotel.getAddress(),
                        hotel.getRate()
                );
                hotelDtoList.add(getHotelDto1);
            }
            return ResponseEntity.ok(hotelDtoList);
        } catch (Exception e) {
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

            for (RoomEntity room : hotel.getRooms()) {
                roomUtils.delete(room);
            }

            for (HotelEvaluationEntity evaluation : hotel.getEvaluations()) {
                hotelEvaluationUtils.delete(evaluation);
            }

            RoomDetailsEntity roomDetails = hotel.getRoomDetails();
            if (roomDetails != null) {
                List<HotelFeaturesEntity> hotelFeatures = roomDetails.getHotelFeatures();
                if (hotelFeatures != null) {
                    hotelFeatures.clear();
                    roomDetailsUtils.save(roomDetails);
                }

                List<RoomFeaturesEntity> roomFeatures = roomDetails.getRoomFeatures();
                if (roomFeatures != null) {
                    roomFeatures.clear();
                    roomDetailsUtils.save(roomDetails);
                }
                s3Service.deleteFile(roomDetails.getImageOne());
                s3Service.deleteFile(roomDetails.getImageTwo());
                s3Service.deleteFile(roomDetails.getImageThree());
                s3Service.deleteFile(roomDetails.getImageFour());

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
