package com.hackathon.backend.controllers.user;

import com.hackathon.backend.dto.userDto.*;
import com.hackathon.backend.services.user.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServicesImpl) {
        this.userServiceImpl = userServicesImpl;
    }

    @PostMapping(path="${USER_REGISTER_PATH}")
    public CompletableFuture<ResponseEntity<?>> registerUserDetails(@ModelAttribute RegisterUserDto registerUserDto){
        try{
            return userServiceImpl.registerUser(registerUserDto);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @PostMapping(path = "${USER_VERIFICATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> verifyUserDetails(@PathVariable("email") String email,
                                                                  @PathVariable("token") String token){
        try {
            return userServiceImpl.verifyUser(email);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture((notFoundException(e)));
        }catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @PostMapping(path = "${USER_SEND_VERIFICATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> sendVerificationLink(@PathVariable("userId") long userId,
                                                                     @PathVariable("token") String token){
        try {
            return userServiceImpl.sendVerificationLink(userId, token);
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @PostMapping(path="${USER_LOGIN_PATH}")
    public CompletableFuture<ResponseEntity<?>> loginUser(@RequestBody LoginUserDto loginUserDto) {
        try{
            return userServiceImpl.loginUser(loginUserDto);
        }catch(AuthenticationException e){
            return CompletableFuture.completedFuture(
                    (new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED)));
        }
    }

    @DeleteMapping(path="${USER_DELETE_PATH}")
    public CompletableFuture<ResponseEntity<?>> deleteUserDetails(@PathVariable("userId") long userId){
        try{
        return userServiceImpl.deleteUser(userId);
        }catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @GetMapping(path = "${GET_USERID_BY_EMAIL_PATH}")
    public CompletableFuture<ResponseEntity<?>> getUserIdByEmail(@RequestParam("email") String email){
        try{
            return userServiceImpl.getUserIdByEmail(email);
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture(notFoundException(e));
        }catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @PutMapping(path="${USER_EDIT_PATH}")
    public CompletableFuture<ResponseEntity<?>> editUserPassword(@PathVariable("userId") long userId,
                                                                 @RequestBody EditUserDto editUserDto){
        try {
            return userServiceImpl.editUserPassword(userId, editUserDto);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }

    }

    @GetMapping(path = "${GET_USER_DETAILS_PATH}")
    public CompletableFuture<ResponseEntity<?>> getUserDetails(@PathVariable("userId") long userId){
        try {
            return userServiceImpl.getUserDetails(userId);
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture(notFoundException(e));
        }catch (Exception e){
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @PutMapping(path = "${EDIT_USER_DETAILS_PATH}")
    public CompletableFuture<ResponseEntity<?>> editUserDetails(@PathVariable("userId") long userId,
                                                                @ModelAttribute EditUserDetailsDto editUserDetailsDto){
        try {
            return userServiceImpl.editUserDetails(userId, editUserDetailsDto);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @PostMapping(path = "${VERIFY_USER_PHONE_NUMBER_PATH}")
    public void verifyPhoneNumber(@PathVariable("phoneNumber") String phoneNumber){
        userServiceImpl.sendSms(phoneNumber);
    }

    @PostMapping("${VERIFY_USER_PHONE_NUMBER_CODE_PATH}")
    public boolean verifyCode(@RequestBody VerifyPhoneNumberDto verifyPhoneNumberDto) {
        return userServiceImpl.verifyCode(verifyPhoneNumberDto.getPhoneNumber(), verifyPhoneNumberDto.getCode());
    }
}
