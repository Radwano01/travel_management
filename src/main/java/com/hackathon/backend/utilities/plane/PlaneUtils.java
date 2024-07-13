package com.hackathon.backend.utilities.plane;

import com.hackathon.backend.dto.planeDto.EditPlaneDto;
import com.hackathon.backend.dto.planeDto.GetPlaneDto;
import com.hackathon.backend.dto.planeDto.PlaneDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public void delete(PlaneEntity plane) {
        planeRepository.delete(plane);
    }

    public List<GetPlaneDto> findAllPlanes() {
        return planeRepository.findAllPlanes();
    }

    public boolean checkHelper(EditPlaneDto editPlaneDto){
        return  editPlaneDto.getPlaneCompanyName() != null ||
                editPlaneDto.getNumSeats() != null ||
                editPlaneDto.getStatus() != null;
    }

    public void editHelper(PlaneEntity plane,
                           EditPlaneDto editPlaneDto) {
        if(editPlaneDto.getStatus() != null){
            plane.setStatus(editPlaneDto.getStatus());
        }
        if(editPlaneDto.getPlaneCompanyName() != null){
            plane.setPlaneCompanyName(editPlaneDto.getPlaneCompanyName());
        }
        if(editPlaneDto.getNumSeats() != null && editPlaneDto.getNumSeats() > 0){
            plane.setNumSeats(editPlaneDto.getNumSeats());
        }
    }
}
