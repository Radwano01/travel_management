package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaneFlightsService {

    private final PlaneFlightsUtils planeFlightsUtils;
    private final PlaneUtils planeUtils;
    private final CountryUtils countryUtils;

    @Autowired
    public PlaneFlightsService(PlaneFlightsUtils planeFlightsUtils,
                               PlaneUtils planeUtils, CountryUtils countryUtils) {
        this.planeFlightsUtils = planeFlightsUtils;
        this.planeUtils = planeUtils;
        this.countryUtils = countryUtils;
    }

    public ResponseEntity<?> addFlight(long planeId, int departureCountryId,
                                       int destinationCountryId, FlightDto flightDto) {
        try{
            PlaneEntity plane = planeUtils.findPlaneById(planeId);
            if(plane.getFlight() != null){
                return alreadyValidException("This plane has already flight");
            }
            CountryEntity departureCountry = countryUtils.findCountryById(departureCountryId);
            CountryEntity destinationCountry = countryUtils.findCountryById(destinationCountryId);
            PlaneFlightsEntity planeFlights = new PlaneFlightsEntity(
                    flightDto.getPrice(),
                    plane,
                    departureCountry,
                    destinationCountry,
                    flightDto.getDepartureTime(),
                    flightDto.getArrivalTime()
            );
            planeFlightsUtils.save(planeFlights);
            return ResponseEntity.ok("Flight added successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
    
    // For now, we filter using country in the future I'll add cities
    public ResponseEntity<?> getFlights(int departureCountryId,
                                        int destinationCountryId) {
        try{
            List<PlaneFlightsEntity> planeFlights = planeFlightsUtils
                    .findAllByDepartureCountryIdAndDestinationCountryId(
                            departureCountryId, destinationCountryId
                    );
            List<FlightDto> flights = planeFlights.stream()
                    .map((flight)-> new FlightDto(
                            flight.getId(),
                            flight.getPrice(),
                            flight.getPlane().getPlaneCompanyName(),
                            flight.getDepartureCountry().getCountry(),
                            flight.getDestinationCountry().getCountry(),
                            flight.getDepartureTime(),
                            flight.getArrivalTime()
                    )).toList();
            return ResponseEntity.ok(flights);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> editFlight(long flightId,
                                        int departureCountryId,
                                        int destinationCountryId,
                                        FlightDto flightDto) {
        try{
            PlaneFlightsEntity planeFlights = planeFlightsUtils.findById(flightId);
            editHelper(planeFlights, flightDto, departureCountryId, destinationCountryId);
            planeFlightsUtils.save(planeFlights);
            return ResponseEntity.ok("Flight edit successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> deleteFlight(long flightId) {
        try{
            PlaneFlightsEntity planeFlights = planeFlightsUtils.findById(flightId);
            if(planeFlights != null) {
                planeFlightsUtils.deleteById(flightId);
                return ResponseEntity.ok("flight deleted successfully");
            }else{
                return notFoundException("flight not found");
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(PlaneFlightsEntity planeFlights,
                            FlightDto flightDto,
                            int departureCountryId,
                            int destinationCountryId) {
        if(flightDto.getPrice() >= 0){
            planeFlights.setPrice(flightDto.getPrice());
        }
        if(flightDto.getDepartureTime() != null){
            planeFlights.setDepartureTime(flightDto.getDepartureTime());
        }
        if(flightDto.getArrivalTime() != null){
            planeFlights.setArrivalTime(flightDto.getArrivalTime());
        }
        if(planeFlights.getDepartureCountry().getId() != departureCountryId){
            CountryEntity departureCountry = countryUtils.findCountryById(departureCountryId);
            planeFlights.setDepartureCountry(departureCountry);
        }
        if(planeFlights.getDestinationCountry().getId() != destinationCountryId){
            CountryEntity destinationCountry = countryUtils.findCountryById(destinationCountryId);
            planeFlights.setDestinationCountry(destinationCountry);
        }
    }
}
