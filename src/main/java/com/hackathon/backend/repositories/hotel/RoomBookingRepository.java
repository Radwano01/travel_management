//package com.hackathon.backend.Repositories.hotel;
//
//import com.hackathon.backend.Entities.hotel.RoomBookingEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface RoomBookingRepository extends JpaRepository<RoomBookingEntity, Long> {
//    List<RoomBookingEntity> findAllByEndTime(LocalDateTime nowTime);
//
//    RoomBookingEntity findRoomIdByUserId(Long userId);
//
//    List<RoomBookingEntity> findAllByUserId(Long userId);
//}
