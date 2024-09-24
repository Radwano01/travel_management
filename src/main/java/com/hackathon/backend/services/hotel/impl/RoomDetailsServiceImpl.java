package com.hackathon.backend.services.hotel.impl;

import com.hackathon.backend.dto.hotelDto.EditRoomDetailsDto;
import com.hackathon.backend.dto.hotelDto.GetRoomDetailsDto;
import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.GetRoomFeaturesDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.services.hotel.RoomDetailsService;
import com.hackathon.backend.utilities.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class RoomDetailsServiceImpl implements RoomDetailsService {

    private final HotelRepository hotelRepository;
    private final RoomDetailsRepository roomDetailsRepository;
    private final S3Service s3Service;

    @Autowired
    public RoomDetailsServiceImpl(HotelRepository hotelRepository,
                                  RoomDetailsRepository roomDetailsRepository,
                                  S3Service s3Service){
        this.hotelRepository = hotelRepository;
        this.roomDetailsRepository = roomDetailsRepository;
        this.s3Service = s3Service;
    }

    @Override
    public ResponseEntity<GetRoomDetailsDto> getHotelRoomDetailsByHotelId(long hotelId){
        HotelEntity hotel = findHotelById(hotelId);

        return ResponseEntity.ok
                (new GetRoomDetailsDto(
                    hotel.getId(),
                    hotel.getHotelName(),
                    hotel.getAddress(),
                    hotel.getRate(),
                    hotel.getRoomDetails().getHotelFeatures() != null ? hotel.getRoomDetails().getHotelFeatures() : null,
                    hotel.getRoomDetails().getRoomFeatures() != null ? hotel.getRoomDetails().getRoomFeatures() : null,
                    hotel.getRoomDetails().getImageOne(),
                    hotel.getRoomDetails().getImageTwo(),
                    hotel.getRoomDetails().getImageThree(),
                    hotel.getRoomDetails().getImageFour(),
                    hotel.getRoomDetails().getDescription(),
                    hotel.getRoomDetails().getPrice()
                )
        );
    }

    @Override
    public ResponseEntity<List<GetHotelFeaturesDto>> getHotelFeaturesFromRoomDetails(long hotelId){
        return ResponseEntity.ok(roomDetailsRepository.findRoomDetailsHotelFeaturesByHotelId(hotelId));
    }

    @Override
    public ResponseEntity<List<GetRoomFeaturesDto>> getRoomFeaturesFromRoomDetails(long hotelId){
        return ResponseEntity.ok(roomDetailsRepository.findRoomDetailsRoomFeaturesByHotelId(hotelId));
    }

    @Transactional
    @Override
    public ResponseEntity<String> editRoomDetails(long hotelId, EditRoomDetailsDto editRoomDetailsDto) {
        if(!checkIfSentEmptyData(editRoomDetailsDto)){
            return badRequestException("you sent an empty data to change");
        }

        HotelEntity hotel = findHotelById(hotelId);

        updateToNewData(hotel.getRoomDetails(), editRoomDetailsDto);

        hotelRepository.save(hotel);

        return ResponseEntity.ok(hotel.getRoomDetails().toString());
    }

    private HotelEntity findHotelById(long hotelId){
        return hotelRepository.findById(hotelId)
                .orElseThrow(()-> new EntityNotFoundException("Hotel id not found"));
    }

    private void updateToNewData(RoomDetailsEntity roomDetails, EditRoomDetailsDto editRoomDetailsDto) {
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
        if (editRoomDetailsDto.getPrice() != null && editRoomDetailsDto.getPrice() > 0) {
            roomDetails.setPrice(editRoomDetailsDto.getPrice());
        }
    }
}
