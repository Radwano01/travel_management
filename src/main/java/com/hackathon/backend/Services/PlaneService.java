package com.hackathon.backend.Services;


import com.hackathon.backend.Dto.PlaneDto.PlaneDto;
import com.hackathon.backend.Dto.PlaneDto.VisaDto;
import com.hackathon.backend.Entities.PlaneEntity;
import com.hackathon.backend.Entities.VisaEntity;
import com.hackathon.backend.Repositories.PlaneRepository;
import com.hackathon.backend.Repositories.VisaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaneService {

    private final PlaneRepository planeRepository;
    private final VisaRepository visaRepository;

    @Autowired
    public PlaneService(PlaneRepository planeRepository, VisaRepository visaRepository){
        this.planeRepository = planeRepository;
        this.visaRepository = visaRepository;
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

    public ResponseEntity<?> createVisa(VisaDto visaDto){
        try{
            boolean existsPlace = visaRepository.existsByPlaceNumber(visaDto.getPlaceNumber());
            PlaneEntity planeEntity = planeRepository.findByPlaneName(visaDto.getPlaneName())
                    .orElseThrow(()-> new EntityNotFoundException("Plane is Not Found"));

            if(!existsPlace) {
                long numberOfSeats = visaRepository.count();
                if (numberOfSeats >= 100) {
                    return new ResponseEntity<>("Cannot create new visa. Maximum seat capacity reached.", HttpStatus.BAD_REQUEST);
                }

                VisaEntity visaEntity = new VisaEntity();
                visaEntity.setAirportLaunch(visaDto.getAirportLaunch());
                visaEntity.setAirportLand(visaDto.getAirportLand());
                visaEntity.setTimeLaunch(visaDto.getTimeLaunch());
                visaEntity.setTimeLand(visaDto.getTimeLand());
                visaEntity.setPlaceNumber(visaDto.getPlaceNumber());
                visaEntity.setPrice(visaDto.getPrice());
                visaEntity.setStatus(visaDto.getStatus());
                visaEntity.setPlaneEntityList(Collections.singletonList(planeEntity));
                visaRepository.save(visaEntity);
            }else{
                return new ResponseEntity<>("Visa place Already valid", HttpStatus.FOUND);
            }

            return new ResponseEntity<>("Visa Created Successfully", HttpStatus.OK);

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

    public ResponseEntity<?> getAllVisasFromPlane(VisaDto visaDto){
        try{
            List<VisaEntity> visaEntityList = visaRepository.findAll();
            List<VisaDto> dto = new ArrayList<>();
            for(VisaEntity visa:visaEntityList){
                String validPlane = visa.getPlaneEntityList().stream()
                        .map(plane-> plane.getPlaneName()).collect(Collectors.joining());
                if(validPlane.contains(visaDto.getPlaneName())){
                    VisaDto visaEntity = new VisaDto();
                    visaEntity.setId(visa.getId());
                    visaEntity.setAirportLaunch(visa.getAirportLaunch());
                    visaEntity.setAirportLand(visa.getAirportLand());
                    visaEntity.setTimeLaunch(visa.getTimeLaunch());
                    visaEntity.setTimeLand(visa.getTimeLand());
                    visaEntity.setPlaceNumber(visa.getPlaceNumber());
                    visaEntity.setPrice(visa.getPrice());
                    visaEntity.setPlaneName(validPlane);
                    visaEntity.setStatus(visa.getStatus());
                    dto.add(visaEntity);
                }
            }

            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editPlane(int id, PlaneDto planeDto){
        try{
            PlaneEntity planeEntity = planeRepository.findById(id)
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

    @Transactional
    public ResponseEntity<?> editVisa(int id, VisaDto visaDto){
        try{
            VisaEntity visaEntity = visaRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Visa Id is Not Found"));
            if(visaDto.getAirportLaunch() != null){
                visaEntity.setAirportLaunch(visaDto.getAirportLaunch());
            }
            if(visaDto.getAirportLand() != null){
                visaEntity.setAirportLand(visaDto.getAirportLand());
            }
            if(visaDto.getTimeLaunch() != null){
                visaEntity.setTimeLaunch(visaDto.getTimeLaunch());
            }
            if(visaDto.getTimeLand() != null){
                visaEntity.setTimeLand(visaDto.getTimeLand());
            }
            if(visaDto.getPlaceNumber() != null){
                visaEntity.setPlaceNumber(visaDto.getPlaceNumber());
            }
            if(visaDto.getPrice() != null){
                visaEntity.setPrice(visaDto.getPrice());
            }
            if(visaDto.getStatus() != null){
                visaEntity.setStatus(visaDto.getStatus());
            }
            return new ResponseEntity<>("Visa updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deletePlane(int id) {
        try{
            planeRepository.deleteById(id);
            return new ResponseEntity<>("Plane deleted successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteVisa(int id) {
        try{
            visaRepository.deleteById(id);
            return new ResponseEntity<>("Visa deleted successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
