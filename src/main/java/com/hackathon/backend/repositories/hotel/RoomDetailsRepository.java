package com.hackathon.backend.repositories.hotel;

import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDetailsRepository extends JpaRepository<RoomDetailsEntity, Long> {
}
