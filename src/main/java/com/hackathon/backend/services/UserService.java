package com.hackathon.backend.services;

import com.hackathon.backend.dto.userDto.*;
import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.security.JWTGenerator;
import com.hackathon.backend.utilities.UserUtils;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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
    private final S3Service s3Service;


    @Value("${VERIFY_LINK_TO_USER}")
    private String verifyLink;


    @Autowired
    public UserService(AuthenticationManager authenticationManager,
                       UserUtils userUtils,
                       RoleRepository roleRepository,
                       JavaMailSender javaMailSender,
                       PasswordEncoder passwordEncoder,
                       JWTGenerator jwtGenerator,
                       S3Service s3Service) {
        this.authenticationManager = authenticationManager;
        this.userUtils = userUtils;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.s3Service = s3Service;
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

            String defaultImageForNewUsers = "https://travelmanagementimages.s3.eu-central-1.amazonaws.com/image_2024-06-07_134630578.png";
            UserEntity userEntity = new UserEntity(
                    registerUserDto.getUsername(),
                    registerUserDto.getEmail(),
                    passwordEncoder.encode(registerUserDto.getPassword()),
                    role,
                    defaultImageForNewUsers
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
            UserEntity user = userUtils.findById(userId);
            s3Service.deleteFile(user.getImage());
            userUtils.delete(user);
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
            userUtils.save(user);
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

            userUtils.sendMessageToEmail(user.getEmail(), verificationLink);
            javaMailSender.send(
                    userUtils.sendMessageToEmail(user.getEmail(), verificationLink)
            );

            return ResponseEntity.ok("Verification email sent");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(UserEntity user, EditUserDto editUserDto) {
        if (editUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(editUserDto.getPassword()));
        }
        if(editUserDto.getImage() != null){
            if(user.getImage() != null) {
                s3Service.deleteFile(user.getImage());
            }
            String userImageName = s3Service.uploadFile(editUserDto.getImage());
            user.setImage(userImageName);
        }
    }
}
