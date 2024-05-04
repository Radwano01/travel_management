package com.hackathon.backend.utilities.hotel;

import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RoomDetailsUtils {

    private final RoomDetailsRepository roomDetailsRepository;

    @Autowired
    public RoomDetailsUtils(RoomDetailsRepository roomDetailsRepository) {
        this.roomDetailsRepository = roomDetailsRepository;
    }

    public void save(RoomDetailsEntity roomDetails) {
        roomDetailsRepository.save(roomDetails);
    }

    public RoomDetailsEntity findByHotelId(long hotelId) {
        return roomDetailsRepository.findByHotelId(hotelId)
                .orElseThrow(()-> new EntityNotFoundException("Hotel id not found"));
    }

    public void deleteByHotelId(long hotelId) {
        roomDetailsRepository.deleteByHotelId(hotelId);
    }

    public RoomDetailsEntity findById(long roomDetailsId) {
        return roomDetailsRepository.findById(roomDetailsId)
                .orElseThrow(()-> new EntityNotFoundException("Room details id not found"));
    }

    public List<RoomDetailsEntity> findAll() {
        return roomDetailsRepository.findAll();
    }

    public void deleteAll() {
        roomDetailsRepository.deleteAll();
    }
}
