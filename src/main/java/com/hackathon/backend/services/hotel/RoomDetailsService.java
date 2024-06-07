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

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class RoomDetailsService {

    private final HotelUtils hotelUtils;
    private final RoomDetailsUtils roomDetailsUtils;
    private final S3Service s3Service;

    @Autowired
    public RoomDetailsService(HotelUtils hotelUtils,
                              RoomDetailsUtils roomDetailsUtils,
                              S3Service s3Service){
        this.hotelUtils = hotelUtils;
        this.roomDetailsUtils = roomDetailsUtils;
        this.s3Service = s3Service;
    }

    public ResponseEntity<?> getRoomAllDetails(long hotelId){
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            RoomDetailsEntity roomDetails = hotel.getRoomDetails();
            GetRoomDetailsDto getRoomDetailsDto = new GetRoomDetailsDto(
                    roomDetails.getImageOne(),
                    roomDetails.getImageTwo(),
                    roomDetails.getImageThree(),
                    roomDetails.getImageFour(),
                    roomDetails.getDescription(),
                    roomDetails.getPrice()
            );

            GetHotelDto getHotelDto = new GetHotelDto(
                    hotel.getId(),
                    hotel.getHotelName(),
                    hotel.getAddress(),
                    hotel.getRate(),
                    getRoomDetailsDto,
                    roomDetails.getHotelFeatures(),
                    roomDetails.getRoomFeatures()

            );
            return ResponseEntity.ok(getHotelDto);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editRoomDetails(long hotelId,
                                             EditRoomDetailsDto editRoomDetailsDto) {
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            editHelper(hotel.getRoomDetails(), editRoomDetailsDto);
            roomDetailsUtils.save(hotel.getRoomDetails());
            return ResponseEntity.ok("Hotel rooms details edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(RoomDetailsEntity roomDetails,
                                 EditRoomDetailsDto editRoomDetailsDto) {
        if (editRoomDetailsDto.getImageOne() != null) {
            s3Service.deleteFile(roomDetails.getImageOne());
            String roomDetailsImageOneName = s3Service.uploadFile(editRoomDetailsDto.getImageOne());
            roomDetails.setImageOne(roomDetailsImageOneName);
        }
        if (editRoomDetailsDto.getImageTwo() != null) {
            s3Service.deleteFile(roomDetails.getImageTwo());
            String roomDetailsImageTwoName = s3Service.uploadFile(editRoomDetailsDto.getImageTwo());
            roomDetails.setImageTwo(roomDetailsImageTwoName);
        }
        if (editRoomDetailsDto.getImageThree() != null) {
            s3Service.deleteFile(roomDetails.getImageThree());
            String roomDetailsImageThreeName = s3Service.uploadFile(editRoomDetailsDto.getImageThree());
            roomDetails.setImageThree(roomDetailsImageThreeName);
        }
        if (editRoomDetailsDto.getImageFour() != null) {
            s3Service.deleteFile(roomDetails.getImageFour());
            String roomDetailsImageFourName = s3Service.uploadFile(editRoomDetailsDto.getImageFour());
            roomDetails.setImageFour(roomDetailsImageFourName);
        }
        if (editRoomDetailsDto.getDescription() != null) {
            roomDetails.setDescription(editRoomDetailsDto.getDescription());
        }
        if (editRoomDetailsDto.getPrice() > 0) {
            roomDetails.setPrice(editRoomDetailsDto.getPrice());
        }
    }
}
