package com.hackathon.backend.Services;


import com.hackathon.backend.Dto.PlaneDto.VisaDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Entities.PlaneEntity;
import com.hackathon.backend.Entities.UserEntity;
import com.hackathon.backend.Entities.VisaEntity;
import com.hackathon.backend.Repositories.PlaneRepository;
import com.hackathon.backend.Repositories.UserRepository;
import com.hackathon.backend.Repositories.VisaRepository;
import com.hackathon.backend.Security.JWTGenerator;
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
public class VisaService {

    private final PlaneRepository planeRepository;
    private final VisaRepository visaRepository;
    private final JWTGenerator jwtGenerator;
    private final UserRepository userRepository;

    @Autowired
    public VisaService(PlaneRepository planeRepository,
                        VisaRepository visaRepository,
                        JWTGenerator jwtGenerator,
                        UserRepository userRepository){
        this.planeRepository = planeRepository;
        this.visaRepository = visaRepository;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
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
                visaEntity.setUserId(1);
                visaRepository.save(visaEntity);
            }else{
                return new ResponseEntity<>("Visa place Already valid", HttpStatus.FOUND);
            }

            return new ResponseEntity<>("Visa Created Successfully", HttpStatus.OK);

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

    public ResponseEntity<?> getSingleVisa(int visaID){
        try{
            VisaEntity visaEntity = visaRepository.findById(visaID)
                    .orElseThrow(()-> new EntityNotFoundException("Visa id is Not Found: "+visaID));
            return new ResponseEntity<>(visaEntity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getValidVisas(){
        try{
            List<VisaEntity> visaEntityList = visaRepository.findAll();
            List<VisaDto> dto = new ArrayList<>();
            for(VisaEntity visa:visaEntityList){
                String validPlane = visa.getPlaneEntityList().stream()
                        .map(plane-> plane.getPlaneName()).collect(Collectors.joining());
                if(visa.getStatus().equals("empty")){
                    VisaDto visaDto = new VisaDto();
                    visaDto.setId(visa.getId());
                    visaDto.setAirportLaunch(visa.getAirportLaunch());
                    visaDto.setAirportLand(visa.getAirportLand());
                    visaDto.setTimeLaunch(visa.getTimeLaunch());
                    visaDto.setTimeLand(visa.getTimeLand());
                    visaDto.setPlaceNumber(visa.getPlaceNumber());
                    visaDto.setPrice(visa.getPrice());
                    visaDto.setPlaneName(validPlane);
                    visaDto.setStatus(visa.getStatus());
                    dto.add(visaDto);
                }
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editVisa(int visaID, VisaDto visaDto){
        try{
            VisaEntity visaEntity = visaRepository.findById(visaID)
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

    public ResponseEntity<?> deleteVisa(int visaID) {
        try{
            visaRepository.deleteById(visaID);
            return new ResponseEntity<>("Visa deleted successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> payment(PaymentDto paymentDto){
        try{
            String token = paymentDto.getAccessToken();
            int userId = getUserIdFromToken(token);
            boolean userVerification = getUserVerificationFromToken(token);
            if(userVerification) {
                VisaEntity visa = visaRepository.findById(paymentDto.getPaymentId())
                        .orElseThrow(() -> new EntityNotFoundException("Visa id is not found"));
                if (!visa.getStatus().equals("full")) {
                    visa.setUserId(userId);
                    visa.setStatus("full");
                    return new ResponseEntity<>("Visa given to user id: " + userId, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Visa has been taken!", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("User is Not Verify it yet!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getUserVisaPayment(String token){
        try{
            List<VisaEntity> visaEntity = visaRepository.findAll();
            List<VisaDto> dto = new ArrayList<>();
            int userId = getUserIdFromToken(token);
            for(VisaEntity visa:visaEntity){
                String validPlane = visa.getPlaneEntityList().stream()
                        .map(plane-> plane.getPlaneName()).collect(Collectors.joining());
                if(visa.getUserId() == userId){
                    VisaDto visaDto = new VisaDto();
                    visaDto.setId(visa.getId());
                    visaDto.setAirportLaunch(visa.getAirportLaunch());
                    visaDto.setAirportLand(visa.getAirportLand());
                    visaDto.setTimeLaunch(visa.getTimeLaunch());
                    visaDto.setTimeLand(visa.getTimeLand());
                    visaDto.setPlaceNumber(visa.getPlaceNumber());
                    visaDto.setPrice(visa.getPrice());
                    visaDto.setPlaneName(validPlane);
                    visaDto.setStatus(visa.getStatus());
                    dto.add(visaDto);
                    break;
                }else{
                    return new ResponseEntity<>("UserId doesn't match the valid UserId", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    public ResponseEntity<?> switchVisasStatusToEmpty(){
        try{
            List<VisaEntity> visaEntityList = visaRepository.findAll();
            for(VisaEntity visa:visaEntityList){
                if(visa.getStatus().equals("full") && visa.getUserId() >= 0){
                    visa.setStatus("empty");
                    visa.setUserId(1);
                    visaRepository.save(visa);
                }
            }
            return new ResponseEntity<>("Visas Status Switched To Empty", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> switchSingleVisaStatusToEmpty(int visaID){
        try {
            VisaEntity visaEntity = visaRepository.findById(visaID)
                    .orElseThrow(() -> new EntityNotFoundException("Visa Id is Not Found"));
            visaEntity.setUserId(1);
            visaEntity.setStatus("empty");
            visaRepository.save(visaEntity);
            return new ResponseEntity<>("Visa id Status updated: "+visaID, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private int getUserIdFromToken(String token){
        String username = jwtGenerator.getUsernameFromJWT(token);
        UserEntity user = userRepository.findIdByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("Username is Not Found"));
        return user.getId();
    }

    private boolean getUserVerificationFromToken(String token){
        String username = jwtGenerator.getUsernameFromJWT(token);
        UserEntity user = userRepository.findIdByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("Username is Not Found"));
        return user.isVerification_status();
    }
}
