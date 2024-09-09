package com.hackathon.backend.services.user.impl;

import com.hackathon.backend.config.TwilioConfig;
import com.hackathon.backend.dto.userDto.*;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.hotel.RoomBookingEntity;
import com.hackathon.backend.entities.package_.PackageBookingEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsBookingEntity;
import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.HotelEvaluationRepository;
import com.hackathon.backend.repositories.hotel.RoomBookingRepository;
import com.hackathon.backend.repositories.package_.PackageBookingRepository;
import com.hackathon.backend.repositories.package_.PackageEvaluationRepository;
import com.hackathon.backend.repositories.plane.PlaneSeatsBookingRepository;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.security.JWTGenerator;
import com.hackathon.backend.services.user.UserService;
import com.hackathon.backend.utilities.S3Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.libs.MyLib.isStrongPassword;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final S3Service s3Service;
    private final TwilioConfig twilioConfig;
    private final HotelEvaluationRepository hotelEvaluationRepository;
    private final PackageEvaluationRepository packageEvaluationRepository;
    private final RoomBookingRepository roomBookingRepository;
    private final PackageBookingRepository packageBookingRepository;
    private final PlaneSeatsBookingRepository planeSeatsBookingRepository;

    @Value("${VERIFY_LINK_TO_USER}")
    private String verifyLink;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           JavaMailSender javaMailSender,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JWTGenerator jwtGenerator,
                           S3Service s3Service,
                           TwilioConfig twilioConfig,
                           HotelEvaluationRepository hotelEvaluationRepository,
                           PackageEvaluationRepository packageEvaluationRepository,
                           RoomBookingRepository roomBookingRepository,
                           PackageBookingRepository packageBookingRepository,
                           PlaneSeatsBookingRepository planeSeatsBookingRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.s3Service = s3Service;
        this.twilioConfig = twilioConfig;
        this.hotelEvaluationRepository = hotelEvaluationRepository;
        this.packageEvaluationRepository = packageEvaluationRepository;
        this.roomBookingRepository = roomBookingRepository;
        this.packageBookingRepository = packageBookingRepository;
        this.planeSeatsBookingRepository = planeSeatsBookingRepository;
    }

    @Async("userServiceTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> registerUser(@NonNull RegisterUserDto registerUserDto) {
        if(isStrongPassword(registerUserDto.getPassword())){
            return CompletableFuture.completedFuture(badRequestException("Password is weak"));
        }

        ResponseEntity<String> checkIfEmailANDUsernameExistResult =
                checkIfEmailANDUsernameAreAlreadyExist(registerUserDto.getEmail(), registerUserDto.getUsername());
        if(!checkIfEmailANDUsernameExistResult.getStatusCode().equals(HttpStatus.OK)){
            return CompletableFuture.completedFuture(checkIfEmailANDUsernameExistResult);
        }

        UserEntity userEntity = prepareUserEntity(registerUserDto);

        userRepository.save(userEntity);

        return CompletableFuture.completedFuture(ResponseEntity.ok(userEntity.toString()));
    }

    private UserEntity prepareUserEntity(RegisterUserDto registerUserDto) {
        return new UserEntity(
                registerUserDto.getUsername(),
                registerUserDto.getEmail(),
                passwordEncoder.encode(registerUserDto.getPassword()),
                registerUserDto.getFullName(),
                registerUserDto.getCountry(),
                (registerUserDto.getPhoneNumber().isEmpty() ? null : registerUserDto.getPhoneNumber()),
                registerUserDto.getAddress(),
                registerUserDto.getDateOfBirth(),
                findRoleByRole()
        );
    }

    //login
    @Async("userServiceTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> loginUser(LoginUserDto loginUserDto) {
        Authentication authentication = authenticateUser(loginUserDto.getUsername(), loginUserDto.getPassword());

        String token = jwtGenerator.generateToken(authentication);

        UserEntity user = findUserByUsername(loginUserDto.getUsername());

        if(user != null){
            EssentialUserDto essentialUserDto = new EssentialUserDto(
                    user.getId(),
                    user.getUsername(),
                    user.getImage(),
                    user.isVerificationStatus()
            );

            return CompletableFuture.completedFuture
                    (ResponseEntity.ok(new AuthResponseDto(token,essentialUserDto)));
        }

        return null;
    }

    //delete
    @Async("userServiceTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> deleteUser(long userId) {
        UserEntity user = getUserById(userId);

        removeUserFromHotelANDPackageEvaluation(userId);
        removeUserFromRoomANDPackageANDPlaneSeatBooking(userId);

        if(user.getImage() != null) {
            s3Service.deleteFile(user.getImage());
        }

        userRepository.delete(user);

        return CompletableFuture.completedFuture(ResponseEntity.ok("Account deleted successfully"));
    }

    private void removeUserFromHotelANDPackageEvaluation(long userId) {
        HotelEvaluationEntity hotelEvaluation = hotelEvaluationRepository.findHotelEvaluationByUserId(userId);
        if (hotelEvaluation != null) {
            hotelEvaluation.setUser(null);
            hotelEvaluationRepository.save(hotelEvaluation);
        }

        PackageEvaluationEntity packageEvaluation = packageEvaluationRepository.findPackageEvaluationByUserId(userId);
        if (packageEvaluation != null) {
            packageEvaluation.setUser(null);
            packageEvaluationRepository.save(packageEvaluation);
        }
    }

    private void removeUserFromRoomANDPackageANDPlaneSeatBooking(long userId) {
        List<RoomBookingEntity> roomBookingEntities = roomBookingRepository.findByUserId(userId);
        if (roomBookingEntities != null) {
            for (RoomBookingEntity roomBookingEntity : roomBookingEntities) {
                roomBookingEntity.setUser(null);
            }
        }

        List<PackageBookingEntity> packageBookingEntities = packageBookingRepository.findByUserId(userId);
        if (packageBookingEntities != null) {
            for (PackageBookingEntity packageBookingEntity : packageBookingEntities) {
                packageBookingEntity.setUser(null);
            }
        }

        List<PlaneSeatsBookingEntity> planeFlightsEntities = planeSeatsBookingRepository.findByUserId(userId);
        if (planeFlightsEntities != null) {
            for (PlaneSeatsBookingEntity planeFlights : planeFlightsEntities) {
                planeFlights.setUser(null);
            }
        }
    }

    @Override
    public CompletableFuture<ResponseEntity<?>> getUserIdByEmail(@NonNull String email) {
        return CompletableFuture.completedFuture(ResponseEntity.ok(getUserIdByEmailFromDB(email)));
    }

    private Object getUserIdByEmailFromDB(String email) {
        return userRepository.findUserIdByEmail(email);
    }

    @Async("userServiceTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> editUserPassword(long userId, EditUserDto editUserDto) {
        if(!checkIfSentEmptyData(editUserDto)){
            return CompletableFuture.completedFuture(badRequestException("you sent an empty data to change"));
        }

        if(isStrongPassword(editUserDto.getPassword())){
            return CompletableFuture.completedFuture(badRequestException("password is weak"));
        }

        UserEntity user = getUserById(userId);

        updateToNewData(user, editUserDto);

        userRepository.save(user);

        return CompletableFuture.completedFuture(ResponseEntity.ok(user.toString()));
    }


    @Async("userServiceTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> verifyUser(String email) {
        UserEntity userEntity = findUserByEmail(email);

        userEntity.setVerificationStatus(true);

        userRepository.save(userEntity);

        return CompletableFuture.completedFuture(ResponseEntity.ok("User has been verified"));
    }

    @Async("userServiceTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> sendVerificationLink(long userId, String token) throws MessagingException {
        UserEntity user = getUserById(userId);

        ResponseEntity<String> checkResult = checkIfUserAlreadyVerified(user);
        if(!checkResult.getStatusCode().equals(HttpStatus.OK)){
            return CompletableFuture.completedFuture(checkResult);
        }

        sendVerificationMessage(user.getEmail(), token);

        return CompletableFuture.completedFuture(ResponseEntity.ok("Verification email sent"));
    }

    private void sendVerificationMessage(String email, String token) throws MessagingException {
        String verificationUrl = verifyLink + "/" + email + "/" + token;
        String subject = "Email Verification From Tourism Project";
        String message = "<html><body>"
                + "<h1>Email Verification</h1>"
                + "<p>Please click the link below to verify your account:</p>"
                + "<a href=\"" + verificationUrl + "\">Verify Email</a>"
                + "</body></html>";

        sendMessageToEmail(
                prepareTheMessageEmail(email, subject, message)
        );
    }

    private ResponseEntity<String> checkIfUserAlreadyVerified(UserEntity user){
        if(user.isVerificationStatus()){
            return badRequestException("This account has verified already");
        }
        return ResponseEntity.ok("OK");
    }

    @Async("userServiceTaskExecutor")
    @Override
    public CompletableFuture<ResponseEntity<?>> getUserDetails(long userId){
        return CompletableFuture.completedFuture(ResponseEntity.ok(userRepository.findUserDetailsById(userId)));
    }

    @Async("userServiceTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> editUserDetails(long userId, EditUserDetailsDto editUserDetailsDto) {
        if(!checkIfSentEmptyData(editUserDetailsDto)){
            return CompletableFuture.completedFuture(badRequestException("you sent an empty data to change"));
        }

        UserEntity user = getUserById(userId);

        updateToNewData(user, editUserDetailsDto);

        userRepository.save(user);

        return CompletableFuture.completedFuture(ResponseEntity.ok(user.toString()));
    }

    @Override
    public void sendSms(String phoneNumber) {
        twilioConfig.sendSMS(phoneNumber);
    }

    @Override
    public boolean verifyCode(String phoneNumber, String code) {
        return twilioConfig.checkVerificationCode(phoneNumber, code);
    }

    @Override
    public boolean verifyCode(long userId, String phoneNumber, String code) {
        UserEntity user = getUserById(userId);

        boolean result = twilioConfig.checkVerificationCode(phoneNumber, code);

        if(result){
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);

            return true;
        }

        return false;
    }

    private UserEntity getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User id not found"));
    }

    private MimeMessage prepareTheMessageEmail(String email, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(message, true);
        return mimeMessage;
    }

    private void sendMessageToEmail(MimeMessage mimeMessage){
        javaMailSender.send(mimeMessage);
    }

    private Authentication authenticateUser(String username, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private ResponseEntity<String> checkIfEmailANDUsernameAreAlreadyExist(String email, String username){
        if (existsByEmail(email) || existsUsernameByUsername(username)) {
            return alreadyValidException("Email or Username is already valid");
        }
        return ResponseEntity.ok("OK");
    }

    private RoleEntity findRoleByRole(){
        return roleRepository.findByRole("USER")
                .orElseThrow(()-> new EntityNotFoundException("Role not found"));
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean existsUsernameByUsername(String username) {
        return userRepository.existsUsernameByUsername(username);
    }

    private UserEntity findUserByUsername(String usernameFromJWT) {
        return userRepository.findUserByUsername(usernameFromJWT)
                .orElseThrow(()-> new EntityNotFoundException("username not found"));
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("Email not found"));
    }

    private void updateToNewData(UserEntity user, EditUserDto editUserDto) {
        if (editUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(editUserDto.getPassword()));
        }
    }

    private void updateToNewData(UserEntity user, EditUserDetailsDto editUserDetailsDto){
        if (editUserDetailsDto.getFullName() != null) {
            user.setFullName(editUserDetailsDto.getFullName());
        }
        if (editUserDetailsDto.getCountry() != null) {
            user.setCountry(editUserDetailsDto.getCountry());
        }
        if (editUserDetailsDto.getPhoneNumber() != null) {
            user.setPhoneNumber(editUserDetailsDto.getPhoneNumber());
        }
        if (editUserDetailsDto.getAddress() != null) {
            user.setAddress(editUserDetailsDto.getAddress());
        }
        if (editUserDetailsDto.getDateOfBirth() != null) {
            user.setDateOfBirth(editUserDetailsDto.getDateOfBirth());
        }
        if (editUserDetailsDto.getImage() != null && !editUserDetailsDto.getImage().isEmpty()) {
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                s3Service.deleteFile(user.getImage());
            }
            user.setImage(s3Service.uploadFile(editUserDetailsDto.getImage()));
        }
    }
}