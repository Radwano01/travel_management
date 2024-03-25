package com.hackathon.backend.Controllers;

import com.hackathon.backend.Dto.UserDto.EditUserDto;
import com.hackathon.backend.Dto.UserDto.LoginUserDto;
import com.hackathon.backend.Dto.UserDto.RegisterUserDto;
import com.hackathon.backend.Dto.UserDto.VerifyDto;
import com.hackathon.backend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "${USER_API_PATH}")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userServices) {
        this.userService = userServices;
    }

    @PostMapping(path="${USER_REGISTER_PATH}")
    public ResponseEntity<?> registerUserDetails(@RequestBody RegisterUserDto registerUserDto){
        return userService.RegisterUser(registerUserDto);
    }

    @PostMapping(path = "${USER_VERIFICATION_PATH}")
    public ResponseEntity<?> verifyUserDetails(@PathVariable("email") String email,
                                               @RequestParam(required = true) String token){
        return userService.VerifyUser(email);
    }

    @PostMapping(path = "${USER_SEND_VERIFICATION_PATH}")
    public ResponseEntity<?> sendVerificationLink(@PathVariable("token") String token){
        return userService.sendVerificationLink(token);
    }

    @PostMapping(path="${USER_LOGIN_PATH}")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserDto loginUserDto) {
        return userService.LoginUser(loginUserDto);
    }

    @DeleteMapping(path="${USER_DELETE_PATH}")
    public ResponseEntity<?> removeUserDetails(@PathVariable("userID") int userID){
        return userService.DeleteUser(userID);
    }

    @PutMapping(path="${USER_EDIT_PATH}")
    public ResponseEntity<?> editUserDetails(@RequestBody EditUserDto editUserDto){
        return userService.EditUser(editUserDto);

    }


}
