package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.AirPortDto;
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

    public ResponseEntity<String> createAirPort(int placeId,
                                           @NonNull AirPortDto airPortDto) {
        try{
            PlaceEntity place = placeUtils.findById(placeId);

            boolean existsAirPort = airPortsUtils
                    .existsAirPortByAirPort(airPortDto.getAirPortName());
            if(existsAirPort){
                return alreadyValidException("airport already found");
            }

            AirPortEntity airPortEntity = new AirPortEntity(
                    airPortDto.getAirPortName(),
                    airPortDto.getAirPortCode(),
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
    public ResponseEntity<String> editAirPort(long airPortId,
                                         AirPortDto airPortDto) {
        try{
            if(!airPortsUtils.checkHelper(airPortDto)){
                return badRequestException("you sent an empty data to change");
            }
            AirPortEntity airPortEntity = airPortsUtils.findById(airPortId);
            airPortsUtils.editHelper(airPortEntity, airPortDto);
            airPortsUtils.save(airPortEntity);
            return ResponseEntity.ok("AirPort edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<String> deleteAirPort(long airPortId){
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