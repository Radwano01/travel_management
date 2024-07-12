package com.hackathon.backend.utilities.hotel;

import com.hackathon.backend.dto.hotelDto.EditRoomDetailsDto;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RoomDetailsUtils {

    private final RoomDetailsRepository roomDetailsRepository;

    private final S3Service s3Service;

    @Autowired
    public RoomDetailsUtils(RoomDetailsRepository roomDetailsRepository,
                            S3Service s3Service) {
        this.roomDetailsRepository = roomDetailsRepository;
        this.s3Service = s3Service;
    }

    public void save(RoomDetailsEntity roomDetails) {
        roomDetailsRepository.save(roomDetails);
    }

    public RoomDetailsEntity findById(long roomDetailsId) {
        return roomDetailsRepository.findById(roomDetailsId)
                .orElseThrow(()-> new EntityNotFoundException("Room details id not found"));
    }

    public List<RoomDetailsEntity> findAll() {
        return roomDetailsRepository.findAll();
    }

    public void delete(RoomDetailsEntity roomDetails) {
        roomDetailsRepository.delete(roomDetails);
    }

    public boolean checkHelper(EditRoomDetailsDto editRoomDetailsDto){
        return  editRoomDetailsDto.getImageOne() != null ||
                editRoomDetailsDto.getImageTwo() != null ||
                editRoomDetailsDto.getImageThree() != null ||
                editRoomDetailsDto.getImageFour() != null ||
                editRoomDetailsDto.getPrice() != null ||
                editRoomDetailsDto.getDescription() != null;
    }

    public void editHelper(RoomDetailsEntity roomDetails,
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
        if (editRoomDetailsDto.getPrice() != null && editRoomDetailsDto.getPrice() > 0) {
            roomDetails.setPrice(editRoomDetailsDto.getPrice());
        }
    }

    public boolean existsById(long id) {
        return roomDetailsRepository.existsById(id);
    }
}
