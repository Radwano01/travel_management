package com.hackathon.backend.utilities.plane;

import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlaneUtils {

    private final PlaneRepository planeRepository;

    @Autowired
    public PlaneUtils(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    public PlaneEntity findPlaneById(long planeId){
        return planeRepository.findById(planeId)
                .orElseThrow(()-> new EntityNotFoundException("Plane id not found"));
    }

    public void save(PlaneEntity planeEntity) {
        planeRepository.save(planeEntity);
    }

    public PlaneEntity findById(long planeId) {
        return planeRepository.findById(planeId)
                .orElseThrow(()-> new EntityNotFoundException("Plane id not found"));
    }

    public void deleteById(long planeId) {
        planeRepository.deleteById(planeId);
    }

    public void deleteAll() {
        planeRepository.deleteAll();
    }
}
