package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomRepository extends JpaRepository<RoomEntity,Integer> {
}
