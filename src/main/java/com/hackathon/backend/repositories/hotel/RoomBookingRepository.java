package com.hackathon.backend.repositories.hotel;

import com.hackathon.backend.entities.hotel.RoomBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBookingEntity, Long> {
    List<RoomBookingEntity> findAllByEndTime(LocalDateTime nowTime);

    List<RoomBookingEntity> findByUserId(long userId);
}
