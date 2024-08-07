package com.hackathon.backend.controllers.user;

import com.hackathon.backend.dto.userDto.*;
import com.hackathon.backend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${BASE_API}")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userServices) {
        this.userService = userServices;
    }

    @PostMapping(path="${USER_REGISTER_PATH}")
    public CompletableFuture<ResponseEntity<?>> registerUserDetails(@RequestBody RegisterUserDto registerUserDto){
        return userService.registerUser(registerUserDto);
    }

    @PostMapping(path = "${USER_VERIFICATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> verifyUserDetails(@PathVariable("email") String email,
                                                                  @PathVariable("token") String token){
        return userService.verifyUser(email);
    }

    @PostMapping(path = "${USER_SEND_VERIFICATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> sendVerificationLink(@PathVariable("userId") long userId,
                                                                     @PathVariable("token") String token){
        return userService.sendVerificationLink(userId, token);
    }

    @PostMapping(path="${USER_LOGIN_PATH}")
    public CompletableFuture<ResponseEntity<?>> loginUser(@RequestBody LoginUserDto loginUserDto) {
        return userService.loginUser(loginUserDto);
    }

    @DeleteMapping(path="${USER_DELETE_PATH}")
    public CompletableFuture<ResponseEntity<?>> removeUserDetails(@PathVariable("userId") long userId){
        return userService.deleteUser(userId);
    }

    @PutMapping(path="${USER_EDIT_PATH}")
    public CompletableFuture<ResponseEntity<?>> editUserDetails(@PathVariable("userId") long userId,
                                                                @RequestBody EditUserDto editUserDto){
        return userService.editUser(userId, editUserDto);

    }

    @GetMapping(path = "${GET_USER_DETAILS_PATH}")
    public CompletableFuture<ResponseEntity<?>> getUserDetails(@PathVariable("userId") long userId){
        return userService.getUserDetails(userId);
    }

    @PutMapping(path = "${EDIT_USER_DETAILS_PATH}")
    public CompletableFuture<ResponseEntity<?>> editUserDetails(@PathVariable("userId") long userId,
                                                                @RequestBody EditUserDetailsDto editUserDetailsDto){
        EditUserDetailsDto userDetailsDto = new EditUserDetailsDto(
                editUserDetailsDto.getFullName(),
                editUserDetailsDto.getCountry(),
                editUserDetailsDto.getPhoneNumber(),
                editUserDetailsDto.getAddress(),
                editUserDetailsDto.getDateOfBirth(),
                editUserDetailsDto.getImage()
        );
        return userService.editUserDetails(userId, userDetailsDto);
    }

    @PostMapping(path = "${VERIFY_USER_PHONE_NUMBER_PATH}")
    public void verifyPhoneNumber(@RequestBody String phoneNumber){
        userService.sendSms(phoneNumber);
    }

    @PostMapping("${VERIFY_USER_PHONE_NUMBER_CODE_PATH}")
    public boolean verifyCode(@RequestBody VerifyPhoneNumberDto verifyPhoneNumberDto) {
        return userService.verifyCode(verifyPhoneNumberDto.getPhoneNumber(), verifyPhoneNumberDto.getCode());
    }
}
