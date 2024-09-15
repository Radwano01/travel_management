package com.hackathon.backend.services.plane.impl;

import com.hackathon.backend.dto.planeDto.EditFlightDto;
import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.dto.planeDto.GetFlightDto;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.plane.AirPortRepository;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import com.hackathon.backend.services.plane.PlaneFlightsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaneFlightsServiceImpl implements PlaneFlightsService {

    private final PlaneFlightsRepository planeFlightsRepository;
    private final AirPortRepository airPortRepository;
    private final PlaneRepository planeRepository;

    @Autowired
    public PlaneFlightsServiceImpl(PlaneFlightsRepository planeFlightsRepository,
                                   AirPortRepository airPortRepository,
                                   PlaneRepository planeRepository) {
        this.planeFlightsRepository = planeFlightsRepository;
        this.airPortRepository = airPortRepository;
        this.planeRepository = planeRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<String> addFlight(long planeId, long departureAirPortId,
                                            long destinationAirPortId, FlightDto flightDto) {
        PlaneEntity plane = getPlaneById(planeId);

        if (plane.getFlight() != null) {
            return alreadyValidException("This plane has already flight");
        }

        PlaneFlightsEntity planeFlights = prepareANDSaveFlightInDB
                (flightDto,getAirportById(departureAirPortId), getAirportById(destinationAirPortId), plane);

        updateANDSavePlaneStatus(plane, false);

        return ResponseEntity.ok(planeFlights.toString());
    }

    private PlaneEntity getPlaneById(long planeId){
        return planeRepository.findById(planeId)
                .orElseThrow(()-> new EntityNotFoundException("No such plane has this id"));
    }

    private AirPortEntity getAirportById(long airportId){
        return airPortRepository.findById(airportId)
                .orElseThrow(()-> new EntityNotFoundException("No such airport has this id"));
    }

    private PlaneFlightsEntity prepareANDSaveFlightInDB(FlightDto flightDto,AirPortEntity departureAirPort,
                                                        AirPortEntity destinationAirPort, PlaneEntity plane){
        PlaneFlightsEntity planeFlights = new PlaneFlightsEntity(
                flightDto.getPrice(),
                plane,
                departureAirPort,
                destinationAirPort,
                flightDto.getDepartureTime(),
                flightDto.getArrivalTime()
        );
        planeFlightsRepository.save(planeFlights);

        return planeFlights;
    }

    private void updateANDSavePlaneStatus(PlaneEntity plane, boolean status){
        plane.setStatus(status);
        planeRepository.save(plane);
    }

    private PlaneFlightsEntity getFlightById(long flightId){
        return planeFlightsRepository.findById(flightId)
                .orElseThrow(()-> new EntityNotFoundException("No such flight has this id"));
    }

    @Override
    public ResponseEntity<List<GetFlightDto>> getFlights(long departureAirPortId,
                                                         long destinationAirPortId,
                                                         int page, int size) {
        return ResponseEntity.ok(planeFlightsRepository.findAllByDepartureAirPortIdAndDestinationAirPortId
                (departureAirPortId, destinationAirPortId, PageRequest.of(page, size)));
    }

    @Override
    public ResponseEntity<List<GetFlightDto>> getFlights(int page, int size,
                                                         Long departureAirPortId,
                                                         Long destinationAirPortId,
                                                         String planeCompanyName){
        return ResponseEntity.ok(planeFlightsRepository.findAllByFilters
                (PageRequest.of(page, size), departureAirPortId, destinationAirPortId, planeCompanyName));
    }

    @Transactional
    @Override
    public ResponseEntity<String> editFlight(long flightId, EditFlightDto editFlightDto) {
        if(!checkIfSentEmptyData(editFlightDto)){
            return badRequestException("you sent an empty data to change");
        }

        PlaneFlightsEntity planeFlights = getFlightById(flightId);

        updateToNewData(planeFlights, editFlightDto);

        planeFlightsRepository.save(planeFlights);

        return ResponseEntity.ok(planeFlights.toString());
    }

    private void updateToNewData(PlaneFlightsEntity planeFlights, EditFlightDto editFlightDto) {
        if (editFlightDto.getPrice() != null && editFlightDto.getPrice() > 0) {
            planeFlights.setPrice(editFlightDto.getPrice());
        }
        if (editFlightDto.getDepartureTime() != null) {
            planeFlights.setDepartureTime(editFlightDto.getDepartureTime());
        }
        if (editFlightDto.getArrivalTime() != null) {
            planeFlights.setArrivalTime(editFlightDto.getArrivalTime());
        }
        if (editFlightDto.getDepartureAirPortId() != null) {
            AirPortEntity airPortEntity = getAirPortById(editFlightDto.getDepartureAirPortId());
            planeFlights.setDepartureAirPort(airPortEntity);
        }

        if (editFlightDto.getDestinationAirPortId() != null) {
            AirPortEntity airPortEntity = getAirPortById(editFlightDto.getDestinationAirPortId());
            planeFlights.setDestinationAirPort(airPortEntity);
        }
    }


    private AirPortEntity getAirPortById(long airportId){
        return airPortRepository.findById(airportId)
                .orElseThrow(()-> new EntityNotFoundException("No such airport has this id"));
    }

    @Override
    public ResponseEntity<String> deleteFlight(long flightId) {
        PlaneFlightsEntity planeFlights = getFlightById(flightId);

        PlaneEntity plane = planeFlights.getPlane();

        updateANDSavePlaneStatus(plane, true);

        planeFlightsRepository.delete(planeFlights);

        return ResponseEntity.ok("flight deleted successfully");
    }


    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void removeFlights() {
        List<PlaneFlightsEntity> flights = planeFlightsRepository.findAll();

        LocalDateTime currentDateTime = LocalDateTime.now();

        for (PlaneFlightsEntity flight : flights) {
            LocalDateTime endTime = flight.getArrivalTime();

            if (currentDateTime.isAfter(endTime)) {
                PlaneEntity plane = flight.getPlane();
                plane.setStatus(true);
                planeRepository.save(plane);
                planeFlightsRepository.deleteById(flight.getId());
            }
        }
    }

}