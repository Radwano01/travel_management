package com.hackathon.backend.utilities;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorUtils {

    public static ResponseEntity<?> serverErrorException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    public static ResponseEntity<?> notFoundException(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    public static ResponseEntity<?> notFoundException(String message){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    public static ResponseEntity<?> alreadyValidException(String message){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    public static ResponseEntity<?> badRequestException(String message){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
