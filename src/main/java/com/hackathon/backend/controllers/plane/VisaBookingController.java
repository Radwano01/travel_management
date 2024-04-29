//package com.hackathon.backend.Controllers.PlaneControllers;
//
//import com.hackathon.backend.Dto.payment.VisaPaymentDto;
//import com.hackathon.backend.Services.PlaneServices.VisaBookingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(path = "${BASE_API}/visas/booking")
//public class VisaBookingController {
//
//    private final VisaBookingService visaBookingService;
//
//    @Autowired
//    public VisaBookingController(VisaBookingService visaBookingService) {
//        this.visaBookingService = visaBookingService;
//    }
//
//    @PostMapping(path = "${VISA_PAYMENT_PATH}")
//    public ResponseEntity<?> payment(@PathVariable("token") String token,@RequestBody VisaPaymentDto visaPaymentDto){
//        return visaBookingService.payment(token,visaPaymentDto);
//    }
//
//    @GetMapping(path = "${GET_ORDERED_VISA_PATH}")
//    public ResponseEntity<?> getAllUserVisaPayment(@PathVariable("token") String token){
//        return visaBookingService.getUserOrderedVisas(token);
//    }
//
//    @GetMapping(path = "${GET_SINGLE_ORDERED_VISA_PATH}")
//    public ResponseEntity<?> getOrderedSingleVisaDetails(@PathVariable("token") String token) {
//        return visaBookingService.getOrderedSingleVisaDetails(token);
//    }
//}
