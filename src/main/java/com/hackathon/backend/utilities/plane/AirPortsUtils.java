package com.hackathon.backend.utilities.plane;

import com.hackathon.backend.dto.planeDto.PlaneAirPortDto;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.repositories.plane.AirPortRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AirPortsUtils {

    private final AirPortRepository airPortRepository;

    @Autowired
    public AirPortsUtils(AirPortRepository airPortRepository) {
        this.airPortRepository = airPortRepository;
    }


    public AirPortEntity findById(long id){
        return airPortRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("AirPort id not found"));
    }

    public AirPortEntity findAirPortByAirPort(String airport) {
        return airPortRepository.findAirPortByAirPortName(airport)
                .orElseThrow(()-> new EntityNotFoundException("AirPort: "+ airport + "not found"));
    }

    public boolean existsAirPortByAirPort(String airPortName) {
        return airPortRepository.existsAirPortByAirPortName(airPortName);
    }

    public void save(AirPortEntity airPortEntity) {
        airPortRepository.save(airPortEntity);
    }

    public void deleteById(long airPortId) {
        airPortRepository.deleteById(airPortId);
    }

    public void deleteAll() {
        airPortRepository.deleteAll();
    }

    public boolean checkHelper(PlaneAirPortDto planeAirPortDto) {
        return planeAirPortDto.getAirPortName() != null || planeAirPortDto.getAirPortCode() != null;
    }

    public void editHelper(AirPortEntity airPortEntity, PlaneAirPortDto planeAirPortDto) {
        if (planeAirPortDto.getAirPortName() != null) {
            airPortEntity.setAirPortName(planeAirPortDto.getAirPortName());
        }

        if (planeAirPortDto.getAirPortCode() != null) {
            airPortEntity.setAirPortCode(planeAirPortDto.getAirPortCode());
        }
    }
}
