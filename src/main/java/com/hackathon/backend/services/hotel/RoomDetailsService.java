package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.dto.hotelDto.RoomDetailsDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class RoomDetailsService {

    private final HotelUtils hotelUtils;
    private final RoomDetailsUtils roomDetailsUtils;

    @Autowired
    public RoomDetailsService(HotelUtils hotelUtils,
                              RoomDetailsUtils roomDetailsUtils){
        this.hotelUtils = hotelUtils;
        this.roomDetailsUtils = roomDetailsUtils;
    }

    public ResponseEntity<?> getRoomAllDetails(long hotelId){
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            RoomDetailsEntity roomDetails = hotel.getRoomDetails();
            RoomDetailsDto roomDetailsDto = new RoomDetailsDto(
                    roomDetails.getImageOne(),
                    roomDetails.getImageTwo(),
                    roomDetails.getImageThree(),
                    roomDetails.getImageFour(),
                    roomDetails.getDescription(),
                    roomDetails.getPrice()
            );

            HotelDto hotelDto = new HotelDto(
                    hotel.getId(),
                    hotel.getHotelName(),
                    hotel.getAddress(),
                    hotel.getRate(),
                    roomDetailsDto,
                    roomDetails.getHotelFeatures(),
                    roomDetails.getRoomFeatures()

            );
            return ResponseEntity.ok(hotelDto);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editRoomDetails(long hotelId,
                                             RoomDetailsDto roomDetailsDto) {
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            editHelper(hotel.getRoomDetails(),roomDetailsDto);
            roomDetailsUtils.save(hotel.getRoomDetails());
            return ResponseEntity.ok("Hotel rooms details edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(RoomDetailsEntity roomDetails,
                                 RoomDetailsDto roomDetailsDto) {
        if (roomDetailsDto.getImageOne() != null) {
            roomDetails.setImageOne(roomDetailsDto.getImageOne());
        }
        if (roomDetailsDto.getImageTwo() != null) {
            roomDetails.setImageTwo(roomDetailsDto.getImageTwo());
        }
        if (roomDetailsDto.getImageThree() != null) {
            roomDetails.setImageThree(roomDetailsDto.getImageThree());
        }
        if (roomDetailsDto.getImageFour() != null) {
            roomDetails.setImageFour(roomDetailsDto.getImageFour());
        }
        if (roomDetailsDto.getDescription() != null) {
            roomDetails.setDescription(roomDetailsDto.getDescription());
        }
        if (roomDetailsDto.getPrice() > 0) {
            roomDetails.setPrice(roomDetailsDto.getPrice());
        }
    }
}
