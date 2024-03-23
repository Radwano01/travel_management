package com.hackathon.backend.Services;


import com.hackathon.backend.Dto.PlaneDto.PlaneDto;
import com.hackathon.backend.Entities.PlaneEntity;
import com.hackathon.backend.Repositories.PlaneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaneService{

    private final PlaneRepository planeRepository;

    @Autowired
    public PlaneService(PlaneRepository planeRepository){
        this.planeRepository = planeRepository;

    }
    public ResponseEntity<?> createPlane(PlaneDto planeDto) {
        try{
            boolean checkExistsPlane = planeRepository.existsByPlaneName(planeDto.getPlaneName());
            if(!checkExistsPlane){
                PlaneEntity planeEntity = new PlaneEntity();
                planeEntity.setPlaneName(planeDto.getPlaneName());
                planeEntity.setSitsCount(planeDto.getSitsCount());
                planeRepository.save(planeEntity);
                return new ResponseEntity<>("Plane Name created Successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Plane Name valid", HttpStatus.FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllPlanes(){
        try{
            List<PlaneEntity> planeEntityList = planeRepository.findAll();
            return new ResponseEntity<>(planeEntityList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getSinglePlane(int planeID) {
        try{
            PlaneEntity planeEntity = planeRepository.findById(planeID)
                    .orElseThrow(()-> new EntityNotFoundException("Plane Id is Not Found"));
            return new ResponseEntity<>(planeEntity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editPlane(int planeID, PlaneDto planeDto){
        try{
            PlaneEntity planeEntity = planeRepository.findById(planeID)
                    .orElseThrow(()-> new EntityNotFoundException("Plane Id is Not Found"));
            if(planeDto.getPlaneName() != null){
                planeEntity.setPlaneName(planeDto.getPlaneName());
            }
            if(planeDto.getSitsCount() != null){
                planeEntity.setSitsCount(planeDto.getSitsCount());
            }
            return new ResponseEntity<>("Plane updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deletePlane(int planeID) {
        try{
            planeRepository.deleteById(planeID);
            return new ResponseEntity<>("Plane deleted successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
