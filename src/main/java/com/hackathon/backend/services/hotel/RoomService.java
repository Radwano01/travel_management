package com.hackathon.backend.services.hotel;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class RoomService {
    private final HotelUtils hotelUtils;
    private final RoomUtils roomUtils;

    @Autowired
    public RoomService(HotelUtils hotelUtils,
                       RoomUtils roomUtils){
        this.hotelUtils = hotelUtils;
        this.roomUtils = roomUtils;
    }

    @Transactional
    public ResponseEntity<?> addRoom(long hotelId) {
        try {
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            if(hotel.getRooms().size() > hotel.getHotelRoomsCount()){
                return badRequestException("You reached the maximum rooms count");
            }
            RoomEntity roomEntity = new RoomEntity(hotel);
            roomUtils.save(roomEntity);
            hotel.getRooms().add(roomEntity);
            hotelUtils.save(hotel);
            return ResponseEntity.ok("Room created successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }


    //button click to change status from frontend :)
    @Transactional
    public ResponseEntity<?> editRoom(long hotelId, long roomId) {
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            Optional<RoomEntity> roomEntity = hotel.getRooms().stream()
                    .filter((room)-> room.getId() == roomId)
                    .findFirst();
            if(roomEntity.isPresent()){
                if(roomEntity.get().isStatus()) {
                    roomEntity.get().setStatus(false);
                }
                roomEntity.get().setStatus(true);
            }
            return ResponseEntity.ok("Room id status updated to: "+roomEntity.get().isStatus());
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteRooms(long hotelId, long roomId){
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            Optional<RoomEntity> foundRoom = hotel.getRooms().stream()
                    .filter((room)-> room.getId() == roomId)
                    .findFirst();
            if(foundRoom.isEmpty()){
                return notFoundException("Room id not found in Hotel Id: "+hotelId);
            }
            hotel.getRooms().remove(foundRoom.get());
            roomUtils.delete(foundRoom.get());
            hotelUtils.save(hotel);
            return ResponseEntity.ok("Room deleted successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
