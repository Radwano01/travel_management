package com.hackathon.backend.Controllers;

import com.hackathon.backend.Dto.PlaneDto.VisaDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Services.VisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "${VISA_API_PATH}")
public class VisaController {

    private final VisaService visaService;

    @Autowired
    public VisaController(VisaService visaService){
        this.visaService = visaService;
    }

    @PostMapping(path = "${VISA_CREATE_PATH}")
    public ResponseEntity<?> createVisa(@PathVariable("planeID") int planeID,@RequestBody VisaDto visaDto){
        return visaService.createVisa(planeID,visaDto);
    }

    @PutMapping(path = "${VISA_EDIT_PATH}")
    public ResponseEntity<?> editVisa(@PathVariable("visaID") int visaID, @RequestBody VisaDto visaDto){
        return visaService.editVisa(visaID, visaDto);
    }

    @GetMapping(path = "${VISA_GET_VALID_PATH}")
    public ResponseEntity<?> getValidVisas(){
        return visaService.getValidVisas();
    }

    @GetMapping(path = "${VISA_GET_SINGLE_PATH}")
    public ResponseEntity<?> getSingleVisa(@PathVariable("visaID") int visaID) {
        return visaService.getSingleVisa(visaID);
    }

    @PutMapping(path = "${VISA_EDIT_STATUS_PATH}")
    public ResponseEntity<?> switchVisasStatus(){
        return visaService.switchVisasStatusToEmpty();
    }

    @PutMapping(path = "${VISA_EDIT_SINGLE_STATUS_PATH}")
    public ResponseEntity<?> switchSingleVisaStatus(@PathVariable("visaID") int visaID){
        return visaService.switchSingleVisaStatusToEmpty(visaID);
    }

    @PostMapping(path = "${VISA_PAYMENT_PATH}")
    public ResponseEntity<?> payment(@RequestBody PaymentDto paymentDto){
        return visaService.payment(paymentDto);
    }

    @GetMapping(path = "${VISA_ORDERED_PATH}")
    public ResponseEntity<?> getAllUserVisaPayment(@PathVariable("token") String token){
        return visaService.getUserVisaPayment(token);
    }

    @DeleteMapping(path = "${VISA_DELETE_PATH}")
    public ResponseEntity<?> deleteVisa(@PathVariable("visaID") int visaID){
        return visaService.deleteVisa(visaID);
    }
}
