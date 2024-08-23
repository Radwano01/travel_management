package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.CreatePlaneDto;
import com.hackathon.backend.dto.planeDto.EditPlaneDto;
import com.hackathon.backend.dto.planeDto.GetPlaneDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaneService{
    private final PlaneRepository planeRepository;

    @Autowired
    public PlaneService(PlaneRepository planeRepository){
        this.planeRepository = planeRepository;
    }

    public ResponseEntity<String> createPlane(@NonNull CreatePlaneDto createPlaneDto) {
        PlaneEntity planeEntity = preparePlaneEntityANDAddToDB(createPlaneDto);

        return ResponseEntity.ok(planeEntity.toString());
    }

    private PlaneEntity getPlaneById(long planeId){
        return planeRepository.findById(planeId)
                .orElseThrow(()-> new EntityNotFoundException("No such plane has this id"));
    }

    private PlaneEntity preparePlaneEntityANDAddToDB(CreatePlaneDto createPlaneDto){
        PlaneEntity plane = new PlaneEntity(
                createPlaneDto.getPlaneCompanyName(),
                createPlaneDto.getNumSeats()
        );

        planeRepository.save(plane);

        return plane;
    }

    public ResponseEntity<List<GetPlaneDto>> getPlanes(){
        return ResponseEntity.ok(planeRepository.findAllPlanes());
    }

    @Transactional
    public ResponseEntity<String> editPlane(long planeId, EditPlaneDto editPlaneDto){
        if(!checkIfSentEmptyData(editPlaneDto)){
            return badRequestException("you sent an empty data to change");
        }

        PlaneEntity planeEntity = getPlaneById(planeId);

        updateToNewData(planeEntity, editPlaneDto);

        planeRepository.save(planeEntity);

        return ResponseEntity.ok(planeEntity.toString());
    }

    private void updateToNewData(PlaneEntity plane, EditPlaneDto editPlaneDto) {
        if(editPlaneDto.getStatus() != null){
            plane.setStatus(editPlaneDto.getStatus());
        }
        if(editPlaneDto.getPlaneCompanyName() != null){
            plane.setPlaneCompanyName(editPlaneDto.getPlaneCompanyName());
        }
        if(editPlaneDto.getNumSeats() != null){
            plane.setNumSeats(editPlaneDto.getNumSeats());
        }
    }

    @Transactional
    public ResponseEntity<String> deletePlane(long planeId) {
        PlaneEntity plane = getPlaneById(planeId);

        PlaneFlightsEntity flight = plane.getFlight();
        if (flight != null) {
            return alreadyValidException("You can't delete a plane that has flight" + flight);
        }

        planeRepository.delete(plane);

        return ResponseEntity.ok("Plane deleted successfully");
    }
}
