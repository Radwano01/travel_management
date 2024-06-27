package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.PlaneAirPortDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.plane.AirPortsUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class AirPortService {

    private final AirPortsUtils airPortsUtils;
    private final PlaceUtils placeUtils;

    @Autowired
    public AirPortService(AirPortsUtils airPortsUtils,
                          PlaceUtils placeUtils) {
        this.airPortsUtils = airPortsUtils;
        this.placeUtils = placeUtils;
    }

    public ResponseEntity<?> createAirPort(int placeId,
                                           @NonNull PlaneAirPortDto planeAirPortDto) {
        try{
            PlaceEntity place = placeUtils.findById(placeId);

            boolean existsAirPort = airPortsUtils
                    .existsAirPortByAirPort(planeAirPortDto.getAirPortName());
            if(existsAirPort){
                return alreadyValidException("airport already found");
            }

            AirPortEntity airPortEntity = new AirPortEntity(
                    planeAirPortDto.getAirPortName(),
                    planeAirPortDto.getAirPortCode(),
                    place
            );

            airPortsUtils.save(airPortEntity);

            return ResponseEntity.ok("AirPort created successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }


    @Transactional
    public ResponseEntity<?> editAirPort(long airPortId,
                                         PlaneAirPortDto planeAirPortDto) {
        try{
            if(!airPortsUtils.checkHelper(planeAirPortDto)){
                return badRequestException("you sent an empty data to change");
            }
            AirPortEntity airPortEntity = airPortsUtils.findById(airPortId);
            airPortsUtils.editHelper(airPortEntity,planeAirPortDto);
            airPortsUtils.save(airPortEntity);
            return ResponseEntity.ok("AirPort edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> deleteAirPort(long airPortId){
        try{
            airPortsUtils.deleteById(airPortId);
            return ResponseEntity.ok("AirPort deleted successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}