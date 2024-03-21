package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.UserDto.EditUserDto;
import com.hackathon.backend.Dto.UserDto.LoginUserDto;
import com.hackathon.backend.Dto.UserDto.RegisterUserDto;
import com.hackathon.backend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userServices) {
        this.userService = userServices;
    }

    @PostMapping(path="/register")
    public ResponseEntity<?> registerUserDetails(@RequestBody RegisterUserDto registerUserDto){
        return userService.RegisterUser(registerUserDto);
    }

    @PostMapping(path="/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserDto loginUserDto) {
        return userService.LoginUser(loginUserDto);
    }

    @DeleteMapping(path="/delete/{id}")
    public ResponseEntity<?> removeUserDetails(@PathVariable("id") int id){
        return userService.DeleteUser(id);
    }

    @PutMapping(path="/edit/{id}")
    public ResponseEntity<?> editUserDetails(@PathVariable("id") int id,
                                             @RequestParam(required = false) String email,
                                             @RequestParam(required = false) String username,
                                             @RequestParam(required = false) String password,
                                             @RequestBody EditUserDto editUserDto){
        return userService.EditUser(editUserDto);

    }

    @PostMapping(path = "/verification/{email}/{token}")
    public ResponseEntity<?> verifyUserDetails(@PathVariable("email") String email,
                                               @PathVariable("token") String token){
        return userService.VerifyUser(email,token);
    }

}
