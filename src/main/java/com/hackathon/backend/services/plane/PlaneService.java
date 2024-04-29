package com.hackathon.backend.services.plane;


import com.hackathon.backend.dto.planeDto.PlaneDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.repositories.plane.PlaneSeatsRepository;
import com.hackathon.backend.utilities.plane.PlaneSeatsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PlaneService{

    private final PlaneSeatsUtils planeSeatsUtils;
    private final PlaneUtils planeUtils;

    @Autowired
    public PlaneService(PlaneSeatsUtils planeSeatsUtils,
                        PlaneUtils planeUtils){
        this.planeSeatsUtils = planeSeatsUtils;
        this.planeUtils = planeUtils;
    }

    public ResponseEntity<?> createPlane(@NonNull PlaneDto planeDto) {
        try{
            PlaneEntity planeEntity = new PlaneEntity(
                    planeDto.getPlaneCompanyName(),
                    planeDto.getNumSeats()
            );
            planeUtils.save(planeEntity);
            return ResponseEntity.ok("Plane Name created Successfully");
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editPlane(long planeId,
                                       PlaneDto planeDto){
        try{
            PlaneEntity planeEntity = planeUtils.findPlaneById(planeId);
            editHelper(planeEntity, planeDto);
            planeUtils.save(planeEntity);
            return ResponseEntity.ok("Plane updated Successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deletePlane(long planeId) {
        try{
            PlaneEntity plane = planeUtils.findById(planeId);
            List<PlaneSeatsEntity> planeSeats = planeSeatsUtils.findAllSeatsByPlaneId(planeId);
            if(plane != null || planeSeats != null){
                planeUtils.deleteById(planeId);
                planeSeatsUtils.deleteById(planeId);
                return ResponseEntity.ok("Plane deleted successfully");
            }else{
                return ResponseEntity.ok("plane not found");
            }
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(PlaneEntity plane,
                            PlaneDto planeDto) {
        if(planeDto.getPlaneCompanyName() != null){
            plane.setPlaneCompanyName(planeDto.getPlaneCompanyName());
        }
        if(plane.getNumSeats() >= planeDto.getNumSeats()){
            plane.setNumSeats(planeDto.getNumSeats());
        }
    }
}
