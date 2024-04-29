package com.hackathon.backend.utilities.hotel;

import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.repositories.hotel.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomUtils {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomUtils(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void save(RoomEntity roomEntity) {
        roomRepository.save(roomEntity);
    }

    public void delete(RoomEntity roomEntity) {
        roomRepository.delete(roomEntity);
    }
}
