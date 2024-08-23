package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.AirPortDto;
import com.hackathon.backend.dto.planeDto.GetAirPortDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
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
public class AirPortService {

    private final PlaceRepository placeRepository;

    @Autowired
    public AirPortService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public ResponseEntity<String> createAirPort(int placeId, @NonNull AirPortDto airPortDto) {
        PlaceEntity place = getPlaceById(placeId);

        ResponseEntity<String> checkResult = checkIfAirPortNameANDCodeAreAlreadyExist
                (place, airPortDto.getAirPortName(), airPortDto.getAirPortCode());
        if(!checkResult.getStatusCode().equals(HttpStatus.OK)){
            return checkResult;
        }

        prepareANDSetAirportInPlace(place, airPortDto);

        placeRepository.save(place);

        return ResponseEntity.ok("AirPort created successfully");
    }

    private PlaceEntity getPlaceById(int placeId){
        return placeRepository.findById(placeId)
                .orElseThrow(()-> new EntityNotFoundException("Not such place has this id"));
    }

    private void prepareANDSetAirportInPlace(PlaceEntity place, AirPortDto airPortDto) {
        AirPortEntity airPortEntity = new AirPortEntity(
                airPortDto.getAirPortName(),
                airPortDto.getAirPortCode(),
                place
        );

        place.getAirPorts().add(airPortEntity);
    }

    private ResponseEntity<String> checkIfAirPortNameANDCodeAreAlreadyExist(PlaceEntity place,
                                                                            String airPortName,
                                                                            String airPortCode) {
         for(AirPortEntity airPort : place.getAirPorts()){
             if(airPort.getAirPortName().equals(airPortName)){
                 return badRequestException("airport name already exist" + airPortName);
             }
             if(airPort.getAirPortName().equals(airPortCode)){
                 return badRequestException("airport code already exist" + airPortCode);
             }
         }

         return ResponseEntity.ok("OK");
    }

    public ResponseEntity<List<GetAirPortDto>> getAirPortsByPlaceId(int placeId){
        return ResponseEntity.ok(placeRepository.findAllAirportsByPlaceId(placeId));
    }


    @Transactional
    public ResponseEntity<String> editAirPort(int placeId,
                              long airPortId,
                              AirPortDto airPortDto) {
        if(!checkIfSentEmptyData(airPortDto)){
            return badRequestException("you sent an empty data to change");
        }

        PlaceEntity place = getPlaceById(placeId);

        AirPortEntity airPortEntity = getAirportFromPlaceEntity(place, airPortId);

        if(airPortEntity == null){
            return notFoundException("Airport not found in place airports");
        }

        updateToNewData(airPortEntity, airPortDto);

        placeRepository.save(place);

        return ResponseEntity.ok(airPortEntity.toString());
    }

    private AirPortEntity getAirportFromPlaceEntity(PlaceEntity place, long airPortId) {
        for(AirPortEntity airPort : place.getAirPorts()){
            if(airPort.getId() == airPortId) return airPort;
        }
        return null;
    }

    private void updateToNewData(AirPortEntity airPortEntity, AirPortDto airPortDto) {
        if (airPortDto.getAirPortName() != null) {
            airPortEntity.setAirPortName(airPortDto.getAirPortName());
        }

        if (airPortDto.getAirPortCode() != null) {
            airPortEntity.setAirPortCode(airPortDto.getAirPortCode());
        }
    }

    public ResponseEntity<String> deleteAirPort(int placeId, long airPortId){
        PlaceEntity place = getPlaceById(placeId);

        AirPortEntity airPortEntity = getAirportFromPlaceEntity(place, airPortId);

        place.getAirPorts().remove(airPortEntity);

        placeRepository.delete(place);

        return ResponseEntity.ok("AirPort deleted successfully");
    }
}