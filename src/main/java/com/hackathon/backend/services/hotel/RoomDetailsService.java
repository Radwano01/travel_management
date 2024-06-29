package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.EditRoomDetailsDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.dto.hotelDto.GetRoomDetailsDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.*;

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
            GetRoomDetailsDto getRoomDetailsDto = new GetRoomDetailsDto(
                    hotel.getId(),
                    hotel.getHotelName(),
                    hotel.getAddress(),
                    hotel.getRate(),
                    roomDetails.getHotelFeatures(),
                    roomDetails.getRoomFeatures(),
                    roomDetails.getImageOne(),
                    roomDetails.getImageTwo(),
                    roomDetails.getImageThree(),
                    roomDetails.getImageFour(),
                    roomDetails.getDescription(),
                    roomDetails.getPrice()
            );

            return ResponseEntity.ok(getRoomDetailsDto);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> editRoomDetails(long hotelId,
                                             EditRoomDetailsDto editRoomDetailsDto) {
        try{
            if(!roomDetailsUtils.checkHelper(editRoomDetailsDto)){
                return badRequestException("you sent an empty data to change");
            }
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            roomDetailsUtils.editHelper(hotel.getRoomDetails(), editRoomDetailsDto);
            roomDetailsUtils.save(hotel.getRoomDetails());
            return ResponseEntity.ok("Hotel rooms details edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
