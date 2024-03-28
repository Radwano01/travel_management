package com.hackathon.backend.Services;


import com.hackathon.backend.Dto.UserTodosDto.UserTodosDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Entities.*;
import com.hackathon.backend.RelationShips.RoomEntity;
import com.hackathon.backend.RelationShips.TodoListEntity;
import com.hackathon.backend.RelationShips.VisaEntity;
import com.hackathon.backend.Repositories.*;
import com.hackathon.backend.Utilities.UserFromToken;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserPackagesService {

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    private final UserFromToken userFromToken;
    private final PackageRepository packageRepository;
    private final UserPackagesRepository userPackagesRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final PlaneRepository planeRepository;
    private final VisaRepository visaRepository;
    private final UserTodosRepository userTodosRepository;
    private final TodoListRepository todoListRepository;
    private final CoinsRepository coinsRepository;


    @Autowired
    public UserPackagesService(UserFromToken userFromToken,
                               PackageRepository packageRepository,
                               UserPackagesRepository userPackagesRepository,
                               RoomRepository roomRepository,
                               HotelRepository hotelRepository,
                               PlaneRepository planeRepository,
                               VisaRepository visaRepository,
                               UserTodosRepository userTodosRepository, TodoListRepository todoListRepository,
                               CoinsRepository coinsRepository) {
        this.userFromToken = userFromToken;
        this.packageRepository = packageRepository;
        this.userPackagesRepository = userPackagesRepository;
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.planeRepository = planeRepository;
        this.visaRepository = visaRepository;
        this.userTodosRepository = userTodosRepository;
        this.todoListRepository = todoListRepository;
        this.coinsRepository = coinsRepository;
    }

    public ResponseEntity<?> payment(String token, PaymentDto paymentDto) {
        try {
            Stripe.apiKey = stripeSecretKey;
            PaymentIntentCreateParams.Builder paramsBuilder = new PaymentIntentCreateParams.Builder()
                    .setCurrency("USD")
                    .setAmount(1000L)
                    .addPaymentMethodType("card")
                    .setPaymentMethod(paymentDto.getPaymentIntent())
                    .setConfirm(true)
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .setErrorOnRequiresAction(true);

            PaymentIntent paymentIntent = PaymentIntent.create(paramsBuilder.build());
            if(paymentIntent.getStatus().equals("succeeded")) {
                int userId = userFromToken.getUserIdFromToken(token);
                boolean userVerification = userFromToken.getUserVerificationFromToken(token);
                if (!userVerification) {
                    return new ResponseEntity<>("User is Not Verified", HttpStatus.BAD_REQUEST);
                }
                PackageEntity packageEntity = packageRepository.findById(paymentDto.getPaymentId())
                        .orElseThrow(() -> new EntityNotFoundException("Package Id is Not Found"));

                HotelEntity hotelEntity = hotelRepository.findByCountry(packageEntity.getCountry())
                        .orElseThrow(() -> new EntityNotFoundException("Hotel: Country is Not Found"));
                Optional<RoomEntity> filteredRoom = hotelEntity.getRooms().stream()
                        .filter((room) -> room.getStatus().equals("empty")).findFirst();

                PlaneEntity planeEntity = planeRepository.findByAirportLand(packageEntity.getCountry().getCountry())
                        .orElseThrow(() -> new EntityNotFoundException("Room: Country is Not Found"));
                Optional<VisaEntity> filteredVisa = planeEntity.getVisas().stream()
                        .filter((visa) -> visa.getStatus().equals("empty")).findFirst();

                if (filteredRoom.isPresent() && filteredVisa.isPresent()) {
                    UserPackagesEntity userPackagesEntity = new UserPackagesEntity();
                    userPackagesEntity.setUserId(userId);
                    userPackagesEntity.setPackageID(packageEntity.getId());

                    RoomEntity filRoom = filteredRoom.get();
                    VisaEntity filVisa = filteredVisa.get();
                    filVisa.setUserId(userId);
                    filRoom.setUserId(userId);

                    CoinsEntity userEarnedCoins = coinsRepository.findByUserId(userId);
                    CoinsEntity coins = new CoinsEntity();
                    coins.setUserId(userId);
                    if(userEarnedCoins != null){
                        coins.setCoins(userEarnedCoins.getCoins());
                    }else{
                        coins.setCoins(0);
                    }
                    userPackagesRepository.save(userPackagesEntity);
                    roomRepository.save(filRoom);
                    visaRepository.save(filVisa);
                    coinsRepository.save(coins);

                    List<TodoListEntity> todoListEntity = packageEntity.getTodos();
                    for (TodoListEntity todoList : todoListEntity) {
                        UserTodosEntity userTodosEntity = new UserTodosEntity();
                        userTodosEntity.setUserId(userId);
                        userTodosEntity.setTodosId(todoList.getId());
                        userTodosRepository.save(userTodosEntity);
                    }
                    return new ResponseEntity<>("DONE", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("There is No Empty Rooms", HttpStatus.NOT_FOUND);
                }
            }else{
                return new ResponseEntity<>("Payment Failed!", HttpStatus.PAYMENT_REQUIRED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getSingleUserPackages(String token){
        try{
            int userId = userFromToken.getUserIdFromToken(token);
            UserPackagesEntity userPackages = userPackagesRepository.findByUserId(userId)
                    .orElseThrow(()-> new EntityNotFoundException("User has No Packages!"));
            return new ResponseEntity<>(userPackages, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAvailableTodos(String token){
        try{
            int userId = userFromToken.getUserIdFromToken(token);
            List<UserTodosEntity> userTodosEntity = userTodosRepository.findAllByUserId(userId)
                    .orElseThrow(()-> new EntityNotFoundException("User has No Todos to do!"));
            List<UserTodosDto> Dto = new ArrayList<>();
            for(UserTodosEntity todos:userTodosEntity){
                UserTodosDto todosDto = new UserTodosDto();
                todosDto.setId(todos.getId());
                Optional<TodoListEntity> todo = todoListRepository.findById(todos.getId());
                todosDto.setTodos(todo.get().getTodolist());
                todosDto.setStatus(todosDto.isStatus());
                Dto.add(todosDto);
            }
            return new ResponseEntity<>(Dto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> setTodosCompleted(int id, String token){
        try{
            int userId = userFromToken.getUserIdFromToken(token);
            UserTodosEntity todo = userTodosRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Todo is Not Found!"));
            TodoListEntity todoList = todoListRepository.findById(todo.getTodosId())
                    .orElseThrow(()-> new EntityNotFoundException("Todo Id is Not Found!"));
            CoinsEntity userCoins = coinsRepository.findByUserId(userId);
            int totalCoins = userCoins.getCoins()+todoList.getCoins();
            userCoins.setCoins(totalCoins);
            todo.setStatus(true);
            userTodosRepository.save(todo);
            return new ResponseEntity<>("Todo Completed Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getSingleUserCoins(String token){
        try{
            int userId = userFromToken.getUserIdFromToken(token);
            CoinsEntity userCoins = coinsRepository.findByUserId(userId);
            return new ResponseEntity<>(userCoins.getCoins(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
