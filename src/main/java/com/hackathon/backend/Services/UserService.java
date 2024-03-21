package com.hackathon.backend.Services;

import com.hackathon.backend.Dto.UserDto.AuthResponseDto;
import com.hackathon.backend.Dto.UserDto.EditUserDto;
import com.hackathon.backend.Dto.UserDto.LoginUserDto;
import com.hackathon.backend.Dto.UserDto.RegisterUserDto;
import com.hackathon.backend.Entities.RoleEntity;
import com.hackathon.backend.Entities.UserEntity;
import com.hackathon.backend.Repositories.RoleRepository;
import com.hackathon.backend.Repositories.TodoListRepository;
import com.hackathon.backend.Repositories.UserRepository;
import com.hackathon.backend.Security.JWTGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TodoListRepository todoListRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, TodoListRepository todoListRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.todoListRepository = todoListRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    //create
    public ResponseEntity<?> RegisterUser(RegisterUserDto registerUserDto) {
        try{
            boolean checkExistsEmail = userRepository.existsByEmail(registerUserDto.getEmail());
            if(!checkExistsEmail){
                RoleEntity roleEntity = roleRepository.findByRole("USER")
                        .orElseThrow(()-> new EntityNotFoundException("this Role is Not Found"));

                UserEntity userEntity = new UserEntity();
                userEntity.setEmail(registerUserDto.getEmail());
                userEntity.setUsername(registerUserDto.getUsername());
                userEntity.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
                userEntity.setRole(Collections.singletonList(roleEntity));

                userRepository.save(userEntity);


                return new ResponseEntity<>("Account Created", HttpStatus.OK);

            }else{
                return new ResponseEntity<>("Email exists", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //login
    public ResponseEntity<?> LoginUser(LoginUserDto loginUserDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            AuthResponseDto authResponseDto = new AuthResponseDto(token);
            return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
        }catch(AuthenticationException e){
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }


    //delete
    public ResponseEntity<?> DeleteUser(int id) {
        try{
            boolean checkExistsEmail = userRepository.existsById(id);
            if(!checkExistsEmail){
                return new ResponseEntity<>("Account id not found: "+id, HttpStatus.BAD_REQUEST);
            }else{
                userRepository.deleteById(id);
                return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Edit
    @Transactional
    public ResponseEntity<String> EditUser(EditUserDto editUserDto) {
        try {
            UserEntity existsUser = userRepository.findUserByEmail(editUserDto.getEmail())
                    .orElseThrow(() -> new IllegalStateException("User not found with email: " + editUserDto.getEmail()));

            if(editUserDto.getEmail() != null && !editUserDto.getEmail().isEmpty()){
                existsUser.setEmail(editUserDto.getEmail());
                return new ResponseEntity<>("Email updated successfully.", HttpStatus.OK);
            }
            if (editUserDto.getUsername() != null && !editUserDto.getUsername().isEmpty()) {
                existsUser.setUsername(editUserDto.getUsername());
                return new ResponseEntity<>("Username updated successfully.", HttpStatus.OK);
            }
            if (editUserDto.getPassword() != null && !editUserDto.getPassword().isEmpty()) {
                existsUser.setPassword(editUserDto.getPassword());
                return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
            }

            return new ResponseEntity<>("Nothing updated", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> VerifyUser(String email,String token) {
        try{
            UserEntity userEntity = userRepository.findUserByEmail(email)
                    .orElseThrow(()-> new EntityNotFoundException("Email not Found! "+email));
            boolean checkValidationToken = jwtGenerator.validateToken(token);
            if(checkValidationToken) {
                userEntity.setVerification_status(true);
                return new ResponseEntity<>("Email Verified", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Token is Wrong!" ,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
