package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Services.PackageService;
import com.hackathon.backend.Services.UserPackagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@Controller
@RequestMapping("/api/v1")
public class UserPackagesController {

    private final UserPackagesService userPackagesService;

    @Autowired
    public UserPackagesController(UserPackagesService userPackagesService) {
        this.userPackagesService = userPackagesService;
    }

    @PostMapping(path = "${USER_PACKAGE_PAYMENT_PATH}")
    public ResponseEntity<?> payment(@PathVariable("token") String token, @RequestBody PaymentDto paymentDto){
        return userPackagesService.payment(token,paymentDto);
    }

    @GetMapping(path = "${USER_PACKAGE_GET_SINGLE_USER_PACKAGES_PATH}")
    public ResponseEntity<?> getSingleUserPackages(@PathVariable("token") String token){
        return userPackagesService.getSingleUserPackages(token);
    }

    @GetMapping(path = "${USER_PACKAGE_GET_SINGLE_USER_TODOS_PATH}")
    public ResponseEntity<?> getSingleUserTodos(@PathVariable("userID") int userID){
        return userPackagesService.getAvailableTodos(userID);
    }

    @PutMapping(path = "${USER_PACKAGE_EDIT_TODOS_STATUS_PATH}")
    public ResponseEntity<?> editTodosStatus(@PathVariable("id") int id){
        return userPackagesService.setTodosCompleted(id);
    }
}
