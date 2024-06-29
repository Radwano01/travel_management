package com.hackathon.backend.utilities.plane;

import com.hackathon.backend.dto.planeDto.AirPortDto;
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

    public boolean checkHelper(AirPortDto airPortDto) {
        return airPortDto.getAirPortName() != null || airPortDto.getAirPortCode() != null;
    }

    public void editHelper(AirPortEntity airPortEntity, AirPortDto airPortDto) {
        if (airPortDto.getAirPortName() != null) {
            airPortEntity.setAirPortName(airPortDto.getAirPortName());
        }

        if (airPortDto.getAirPortCode() != null) {
            airPortEntity.setAirPortCode(airPortDto.getAirPortCode());
        }
    }
}
