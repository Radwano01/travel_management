package com.hackathon.backend.services.plane;


import com.hackathon.backend.dto.planeDto.EditPlaneDto;
import com.hackathon.backend.dto.planeDto.GetPlaneDto;
import com.hackathon.backend.dto.planeDto.PlaneDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.repositories.plane.PlaneSeatsRepository;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
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

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaneService{

    private final PlaneSeatsUtils planeSeatsUtils;
    private final PlaneUtils planeUtils;
    private final PlaneFlightsUtils planeFlightsUtils;

    @Autowired
    public PlaneService(PlaneSeatsUtils planeSeatsUtils,
                        PlaneUtils planeUtils,
                        PlaneFlightsUtils planeFlightsUtils){
        this.planeSeatsUtils = planeSeatsUtils;
        this.planeUtils = planeUtils;
        this.planeFlightsUtils = planeFlightsUtils;
    }

    public ResponseEntity<String> createPlane(@NonNull PlaneDto planeDto) {
        try{
            PlaneEntity planeEntity = new PlaneEntity(
                    planeDto.getPlaneCompanyName(),
                    planeDto.getNumSeats()
            );


            for(int i = 0; i < planeDto.getNumSeats(); i++){
                PlaneSeatsEntity planeSeats = new PlaneSeatsEntity();
                planeSeatsUtils.save(planeSeats);
                planeEntity.getPlaneSeats().add(planeSeats);
            }
            planeUtils.save(planeEntity);
            return ResponseEntity.ok("Plane Name created Successfully");
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getPlanes(){
        try{
            List<GetPlaneDto> plane = planeUtils.findAllPlanes();
            return ResponseEntity.ok(plane);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> editPlane(long planeId,
                                       EditPlaneDto editPlaneDto){
        try{
            if(!planeUtils.checkHelper(editPlaneDto)){
                return badRequestException("you sent an empty data to change");
            }
            PlaneEntity planeEntity = planeUtils.findPlaneById(planeId);
            planeUtils.editHelper(planeEntity, editPlaneDto);
            planeUtils.save(planeEntity);
            return ResponseEntity.ok("Plane updated Successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> deletePlane(long planeId) {
        try{
            PlaneEntity plane = planeUtils.findById(planeId);

            for(PlaneSeatsEntity planeSeats:plane.getPlaneSeats()){
                planeSeatsUtils.delete(planeSeats);
            }
            planeFlightsUtils.delete(plane.getFlight());
            planeUtils.delete(plane);
            return ResponseEntity.ok("Plane deleted successfully");
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
