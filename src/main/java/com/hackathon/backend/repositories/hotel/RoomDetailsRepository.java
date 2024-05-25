package com.hackathon.backend.repositories.hotel;

import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomDetailsRepository extends JpaRepository<RoomDetailsEntity, Long> {
}
