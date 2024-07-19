package com.hackathon.backend.services.user;

import com.hackathon.backend.config.TwilioConfig;
import com.hackathon.backend.dto.userDto.*;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.security.JWTGenerator;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.package_.PackageEvaluationUtils;
import com.hackathon.backend.utilities.user.UserUtils;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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
    private final S3Service s3Service;
    private final TwilioConfig twilioConfig;
    private final HotelEvaluationUtils hotelEvaluationUtils;
    private final PackageEvaluationUtils packageEvaluationUtils;

    @Value("${VERIFY_LINK_TO_USER}")
    private String verifyLink;

    @Autowired
    public UserService(AuthenticationManager authenticationManager,
                       UserUtils userUtils,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JWTGenerator jwtGenerator,
                       S3Service s3Service,
                       TwilioConfig twilioConfig,
                       HotelEvaluationUtils hotelEvaluationUtils,
                       PackageEvaluationUtils packageEvaluationUtils) {
        this.authenticationManager = authenticationManager;
        this.userUtils = userUtils;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.s3Service = s3Service;
        this.twilioConfig = twilioConfig;
        this.hotelEvaluationUtils = hotelEvaluationUtils;

        this.packageEvaluationUtils = packageEvaluationUtils;
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

            UserEntity userEntity = new UserEntity(
                    registerUserDto.getUsername(),
                    registerUserDto.getEmail(),
                    passwordEncoder.encode(registerUserDto.getPassword()),
                    registerUserDto.getImage(),
                    registerUserDto.getFullName(),
                    registerUserDto.getCountry(),
                    registerUserDto.getPhoneNumber(),
                    registerUserDto.getAddress(),
                    registerUserDto.getDateOfBirth(),
                    role
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
            if(user.getHotelEvaluations() != null){
                for(HotelEvaluationEntity hotelEvaluation:user.getHotelEvaluations()){
                    hotelEvaluationUtils.delete(hotelEvaluation);
                }
            }
            if(user.getPackageEvaluations() != null){
                for(PackageEvaluationEntity packageEvaluation:user.getPackageEvaluations()){
                    packageEvaluationUtils.delete(packageEvaluation);
                }
            }
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

            String verificationUrl = verifyLink + "/" + user.getEmail() + "/" + token;
            String subject = "Email Verification From Tourism Project";
            String message = "<html><body>"
                    + "<h1>Email Verification</h1>"
                    + "<p>Please click the link below to verify your account:</p>"
                    + "<a href=\"" + verificationUrl + "\">Verify Email</a>"
                    + "</body></html>";

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

    @Async("userServiceTaskExecutor")
    public CompletableFuture<ResponseEntity<?>> getUserDetails(long userId){
        try{
            UserEntity user = userUtils.findById(userId);
            UserDto userDto = new UserDto(
                    user.getUsername(),
                    user.getEmail(),
                    user.getImage(),
                    user.isVerificationStatus(),
                    user.getFullName(),
                    user.getCountry(),
                    user.getPhoneNumber(),
                    user.getAddress(),
                    user.getDateOfBirth()
            );
            return CompletableFuture.completedFuture(ResponseEntity.ok(userDto));
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture(notFoundException(e));
        }catch (Exception e){
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @Async("userServiceTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<?>> editUserDetails(long userId,
                                                                EditUserDetailsDto editUserDetailsDto) {
        try{
            if(!userUtils.checkHelper(editUserDetailsDto)){
                return CompletableFuture.completedFuture(notFoundException("you sent an empty data to change"));
            }
            UserEntity user = userUtils.findById(userId);
            userUtils.editHelper(user, editUserDetailsDto);
            userUtils.save(user);
            return CompletableFuture.completedFuture(ResponseEntity.ok("User Details edited successfully"));
        }catch (EntityNotFoundException e){
            return CompletableFuture.completedFuture(notFoundException(e));
        }catch (Exception e){
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    public void sendSms(String phoneNumber){
        twilioConfig.sendSms(phoneNumber);
    }

    public boolean verifyCode(String phoneNumber, String code) {
        return twilioConfig.checkVerificationCode(phoneNumber, code);
    }
}