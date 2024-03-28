package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.CountryEntity;
import com.hackathon.backend.RelationShips.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomRepository extends JpaRepository<RoomEntity,Integer> {
    List<RoomEntity> findByUserId(int userId);
}
