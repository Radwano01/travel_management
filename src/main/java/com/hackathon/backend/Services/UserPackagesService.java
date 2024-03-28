package com.hackathon.backend.Services;


import com.hackathon.backend.Dto.UserTodosDto.UserTodosDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Entities.*;
import com.hackathon.backend.RelationShips.RoomEntity;
import com.hackathon.backend.RelationShips.TodoListEntity;
import com.hackathon.backend.RelationShips.VisaEntity;
import com.hackathon.backend.Repositories.*;
import com.hackathon.backend.Utilities.UserFromToken;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserPackagesService {

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
            int userId = userFromToken.getUserIdFromToken(token);
            boolean userVerification = userFromToken.getUserVerificationFromToken(token);
            if (!userVerification) {
                return new ResponseEntity<>("User is Not Verified", HttpStatus.BAD_REQUEST);
            }
            PackageEntity packageEntity = packageRepository.findById(paymentDto.getPaymentId())
                    .orElseThrow(()-> new EntityNotFoundException("Package Id is Not Found"));

            HotelEntity hotelEntity = hotelRepository.findByCountry(packageEntity.getCountry())
                    .orElseThrow(()-> new EntityNotFoundException("Hotel: Country is Not Found"));
            Optional<RoomEntity> filteredRoom = hotelEntity.getRooms().stream()
                    .filter((room)-> room.getStatus().equals("empty")).findFirst();

            PlaneEntity planeEntity = planeRepository.findByAirportLand(packageEntity.getCountry().getCountry())
                    .orElseThrow(()-> new EntityNotFoundException("Room: Country is Not Found"));
            Optional<VisaEntity> filteredVisa = planeEntity.getVisas().stream()
                    .filter((visa)-> visa.getStatus().equals("empty")).findFirst();

            if(filteredRoom.isPresent() && filteredVisa.isPresent()){
                UserPackagesEntity userPackagesEntity = new UserPackagesEntity();
                userPackagesEntity.setUserId(userId);
                userPackagesEntity.setPackageID(packageEntity.getId());
                RoomEntity filRoom = filteredRoom.get();
                VisaEntity filVisa = filteredVisa.get();
                filVisa.setUserId(userId);
                filRoom.setUserId(userId);
                userPackagesRepository.save(userPackagesEntity);
                roomRepository.save(filRoom);
                visaRepository.save(filVisa);

                List<TodoListEntity> todoListEntity = packageEntity.getTodos();
                for(TodoListEntity todoList: todoListEntity){
                    UserTodosEntity userTodosEntity = new UserTodosEntity();
                    userTodosEntity.setUserId(userId);
                    userTodosEntity.setTodosId(todoList.getId());
                    userTodosRepository.save(userTodosEntity);
                }
                return new ResponseEntity<>("DONE", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("There is No Empty Rooms", HttpStatus.NOT_FOUND);
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

    public ResponseEntity<?> getAvailableTodos(int userID){
        try{
            List<UserTodosEntity> userTodosEntity = userTodosRepository.findAllByUserId(userID)
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
    public ResponseEntity<?> setTodosCompleted(int id){
        try{
            UserTodosEntity todo = userTodosRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Todo is Not Found!"));
            todo.setStatus(true);
            userTodosRepository.save(todo);
            return new ResponseEntity<>("Todo Completed Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
