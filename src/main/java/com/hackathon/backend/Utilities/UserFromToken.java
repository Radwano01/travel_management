package com.hackathon.backend.Utilities;

import com.hackathon.backend.Entities.UserEntity;
import com.hackathon.backend.Repositories.UserRepository;
import com.hackathon.backend.Security.JWTGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserFromToken {

    private final JWTGenerator jwtGenerator;
    private final UserRepository userRepository;


    @Autowired
    public UserFromToken(JWTGenerator jwtGenerator,
                         UserRepository userRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }

    public int getUserIdFromToken(String token){
        String username = jwtGenerator.getUsernameFromJWT(token);
        UserEntity user = userRepository.findIdByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("Username is Not Found"));
        return user.getId();
    }

    public boolean getUserVerificationFromToken(String token){
        String username = jwtGenerator.getUsernameFromJWT(token);
        UserEntity user = userRepository.findIdByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException("Username is Not Found"));
        return user.isVerification_status();
    }
}
