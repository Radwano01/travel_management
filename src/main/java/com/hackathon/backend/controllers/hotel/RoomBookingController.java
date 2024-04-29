//package com.hackathon.backend.Controllers.hotelControllers;
//
//
//import com.hackathon.backend.Dto.payment.RoomPaymentDto;
//import com.hackathon.backend.Services.BookingSerivices.RoomBookingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(path = "${BASE_API}/rooms/booking")
//public class RoomBookingController {
//
//    private final RoomBookingService roomBookingService;
//    @Autowired
//    public RoomBookingController(RoomBookingService roomBookingService){
//        this.roomBookingService = roomBookingService;
//    }
//
//    @PostMapping("/payment/{token}")
//    public ResponseEntity<?> payment(@PathVariable("token") String token,
//                                     @RequestBody RoomPaymentDto roomPaymentDto){
//        return roomBookingService.payment(token, roomPaymentDto);
//    }
//
//    @GetMapping(path = "${GET_ORDERED_ROOM_PATH}")
//    public ResponseEntity<?> getOrderedRooms(@PathVariable("token") String token) {
//        return roomBookingService.getOrderedRooms(token);
//    }
//
//    @GetMapping(path = "${GET_SINGLE_ORDERED_ROOM_PATH}")
//    public ResponseEntity<?> getOrderedSingleRoomDetails(@PathVariable("token") String token) {
//        return roomBookingService.getOrderedSingleRoomDetails(token);
//    }
//}
