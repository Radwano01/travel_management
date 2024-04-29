package com.hackathon.backend.repositories.hotel;

import com.hackathon.backend.entities.hotel.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomRepository extends JpaRepository<RoomEntity,Long> {
}
