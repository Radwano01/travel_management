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


    @Transactional
    public ResponseEntity<?> createVisa(int planeID,VisaDto visaDto){
        try{
            PlaneEntity planeEntity = planeRepository.findById(planeID)
                    .orElseThrow(()-> new EntityNotFoundException("Plane is Not Found"));
            if(planeEntity.getSitsCount() >= 100){
                return new ResponseEntity<>("Plane Visas jumped above 100 "+planeEntity.getPlaneName(), HttpStatus.BAD_REQUEST);
            }else {
                VisaEntity visaEntity = new VisaEntity();
                visaEntity.setPlaceNumber(visaDto.getPlaceNumber());
                visaEntity.setPlaneName(visaDto.getPlaneName());
                visaEntity.setPrice(visaDto.getPrice());
                visaEntity.setStatus(visaDto.getStatus());
                visaEntity.setUserId(1);
                visaRepository.save(visaEntity);

                planeEntity.getVisas().add(visaEntity);
                return new ResponseEntity<>("Visa Created Successfully", HttpStatus.OK);
            }
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
                if(visa.getStatus().equals("empty")){
                    VisaDto visaDto = new VisaDto();
                    visaDto.setId(visa.getId());
                    visaDto.setPlaceNumber(visa.getPlaceNumber());
                    visaDto.setPrice(visa.getPrice());
                    visaDto.setPlaneName(visa.getPlaneName());
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
            List<VisaDto> dto = new ArrayList<>();
            int userId = getUserIdFromToken(token);
            List<VisaEntity> visaEntity = visaRepository.findByUserId(userId);
            for(VisaEntity visa:visaEntity){
                if(visa.getUserId() == userId){
                    VisaDto visaDto = new VisaDto();
                    visaDto.setId(visa.getId());
                    visaDto.setPlaceNumber(visa.getPlaceNumber());
                    visaDto.setPrice(visa.getPrice());
                    visaDto.setPlaneName(visa.getPlaneName());
                    visaDto.setStatus(visa.getStatus());
                    dto.add(visaDto);
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
