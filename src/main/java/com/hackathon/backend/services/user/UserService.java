package com.hackathon.backend.services.user;

import com.hackathon.backend.dto.userDto.*;
import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.security.JWTGenerator;
import com.hackathon.backend.utilities.user.UserUtils;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

import static com.hackathon.backend.utilities.ErrorUtils.*;
import static com.hackathon.backend.utilities.user.PasswordChecker.isStrongPassword;

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

    @Value("${DEFAULT_USER_IMAGE}")
    private String default_user_image;


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

    @Async("userServiceTaskExecutor")
    public CompletableFuture<ResponseEntity<?>> registerUser(@NonNull RegisterUserDto registerUserDto) {
        if(!isStrongPassword(registerUserDto.getPassword())){
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 8 characters long and contain"
                    + " at least one uppercase letter, one lowercase letter, one number, and one special character."));
        }
        try {
            boolean checkExistsEmail = userUtils
                    .existsByEmail(registerUserDto.getEmail());
            if (checkExistsEmail) {
                return CompletableFuture.completedFuture((alreadyValidException("Email exists")));
            }
            boolean existsUsername = userUtils
                    .existsUsernameByUsername(registerUserDto.getUsername());
            if (existsUsername) {
                return CompletableFuture.completedFuture((alreadyValidException("Username already valid")));
            }
            RoleEntity role = roleRepository.findByRole("USER")
                    .orElseThrow(()-> new EntityNotFoundException("Role not found"));

            String defaultImageForNewUsers = default_user_image;
            UserEntity userEntity = new UserEntity(
                    registerUserDto.getUsername(),
                    registerUserDto.getEmail(),
                    passwordEncoder.encode(registerUserDto.getPassword()),
                    role,
                    defaultImageForNewUsers
            );
            userUtils.save(userEntity);
            return CompletableFuture.completedFuture((ResponseEntity.ok("Account Created")));
        } catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    //login
    @Async("userServiceTaskExecutor")
    public CompletableFuture<ResponseEntity<?>> loginUser(LoginUserDto loginUserDto) {
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
                        user.getImage(),
                        user.isVerificationStatus()
                );
                AuthResponseDto authResponseDto = new AuthResponseDto(token,essentialUserDto);
                return CompletableFuture.completedFuture((ResponseEntity.ok(authResponseDto)));
            }
            return CompletableFuture.completedFuture((badRequestException("Something went wrong")));
        }catch(AuthenticationException e){
            return CompletableFuture.completedFuture
                    ((new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED)));
        }
    }


    //delete
    @Async("userServiceTaskExecutor")
    public CompletableFuture<ResponseEntity<?>> deleteUser(long userId) {
        try{
            UserEntity user = userUtils.findById(userId);
            s3Service.deleteFile(user.getImage());
            userUtils.delete(user);
            return CompletableFuture.completedFuture((ResponseEntity.ok("Account deleted successfully")));
        }catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @Async("userServiceTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<?>> editUser(long userId, EditUserDto editUserDto) {
        try {
            if(!userUtils.checkHelper(editUserDto)){
                return CompletableFuture.completedFuture((badRequestException("you sent an empty data to change")));
            }
            if (!isStrongPassword(editUserDto.getPassword())) {
                return CompletableFuture.completedFuture((ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Password must be at least 8 characters" +
                                " long and contain at least one uppercase" +
                                " letter, one lowercase letter, one number," +
                                " and one special character.")));
            }

            UserEntity user = userUtils.findById(userId);
            userUtils.editHelper(user, editUserDto);
            userUtils.save(user);
            return CompletableFuture.completedFuture((ResponseEntity.ok("user updated successfully")));
        } catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }


    @Async("userServiceTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<?>> verifyUser(String email) {
        try{
            UserEntity userEntity = userUtils.findUserByEmail(email);
            userEntity.setVerificationStatus(true);
            userUtils.save(userEntity);
            return CompletableFuture.completedFuture((ResponseEntity.ok("User has been verified")));
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture((notFoundException(e)));
        }catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }

    @Async("userServiceTaskExecutor")
    public CompletableFuture<ResponseEntity<?>> sendVerificationLink(long userId, String token) {
        try{
            UserEntity user = userUtils.findById(userId);
            if(user.isVerificationStatus()){
                return CompletableFuture.completedFuture((badRequestException("This account has verified already")));
            }

            String subject = "Email Verification From Tourism Project";
            String message = "Please Click to the link to verify your account: "+verifyLink+user.getEmail()+"/"+token;

            userUtils.sendMessageToEmail(
                    userUtils.prepareTheMessageEmail(user.getEmail(), subject, message)
            );

            return CompletableFuture.completedFuture((ResponseEntity.ok("Verification email sent")));
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture((notFoundException(e)));
        } catch (Exception e){
            return CompletableFuture.completedFuture((serverErrorException(e)));
        }
    }
}