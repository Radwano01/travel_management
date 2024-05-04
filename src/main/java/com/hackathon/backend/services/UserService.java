package com.hackathon.backend.services;

import com.hackathon.backend.dto.userDto.*;
import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.security.JWTGenerator;
import com.hackathon.backend.utilities.UserUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserUtils userUtils;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final JavaMailSender javaMailSender;


    @Value("${VERIFY_LINK_TO_USER}")
    private String verifyLink;


    @Autowired
    public UserService(AuthenticationManager authenticationManager,
                       UserUtils userUtils,
                       RoleRepository roleRepository,
                       JavaMailSender javaMailSender,
                       PasswordEncoder passwordEncoder,
                       JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userUtils = userUtils;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    public ResponseEntity<?> registerUser(@NonNull RegisterUserDto registerUserDto) {
        try {
            boolean checkExistsEmail = userUtils
                    .existsByEmail(registerUserDto.getEmail());
            if (checkExistsEmail) {
                return alreadyValidException("Email exists");
            }
            boolean existsUsername = userUtils
                    .existsUsernameByUsername(registerUserDto.getUsername());
            if (existsUsername) {
                return alreadyValidException("Username already valid");
            }
            RoleEntity role = roleRepository.findByRole("USER")
                    .orElseThrow(()-> new EntityNotFoundException("Role not found"));

            UserEntity userEntity = new UserEntity(
                    registerUserDto.getUsername(),
                    registerUserDto.getEmail(),
                    passwordEncoder.encode(registerUserDto.getPassword()),
                    role
            );
            userUtils.save(userEntity);
            return ResponseEntity.ok("Account Created");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    //login
    public ResponseEntity<?> loginUser(LoginUserDto loginUserDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            UserEntity user = userUtils
                    .findUserByUsername(jwtGenerator.getUsernameFromJWT(token));

            if(user != null){
                EssentialUserDto essentialUserDto = new EssentialUserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getImage()
                );
                AuthResponseDto authResponseDto = new AuthResponseDto(token,essentialUserDto);
                return ResponseEntity.ok(authResponseDto);
            }
            return badRequestException("Something went wrong");
        }catch(AuthenticationException e){
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }


    //delete
    public ResponseEntity<?> deleteUser(long userId) {
        try{
            userUtils.deleteById(userId);
            return ResponseEntity.ok("Account deleted successfully");
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editUser(long userId,
                                      EditUserDto editUserDto) {
        try {
            UserEntity user = userUtils.findById(userId);
            editHelper(user,editUserDto);
            return ResponseEntity.ok("user updated successfully");
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> verifyUser(String email) {
        try{
            UserEntity userEntity = userUtils.findUserByEmail(email);
            userEntity.setVerificationStatus(true);
            userUtils.save(userEntity);
            return ResponseEntity.ok("User has been verified");
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> sendVerificationLink(long userId, String token) {
        try{
            UserEntity user = userUtils.findById(userId);
            if(user.isVerificationStatus()){
                return badRequestException("This account has verified already");
            }

            String verificationLink = verifyLink+user.getEmail()+"/"+token;
            sendMail(user.getEmail(), verificationLink);
            javaMailSender.send(sendMail(user.getEmail(), verificationLink));
            return ResponseEntity.ok("Verification email sent");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(UserEntity user, EditUserDto editUserDto) {
        if(editUserDto.getEmail() != null){
            user.setEmail(editUserDto.getEmail());
        }
        if (editUserDto.getUsername() != null) {
            user.setUsername(editUserDto.getUsername());
        }
        if (editUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(editUserDto.getPassword()));
        }
        if(editUserDto.getImage() != null){
            user.setImage(editUserDto.getImage());
        }
    }

    private SimpleMailMessage sendMail(String email, String verificationLink){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Email Verification From Hackathon Project");
        mailMessage.setText("Please Click to the link to verify your account: "+verificationLink);
        return mailMessage;
    }
}
