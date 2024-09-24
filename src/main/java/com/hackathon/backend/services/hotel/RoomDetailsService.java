package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.EditRoomDetailsDto;
import com.hackathon.backend.dto.hotelDto.GetRoomDetailsDto;
import org.springframework.http.ResponseEntity;

public interface RoomDetailsService {

    ResponseEntity<GetRoomDetailsDto> getHotelRoomDetailsByHotelId(long hotelId);

    ResponseEntity<String> editRoomDetails(long hotelId, EditRoomDetailsDto editRoomDetailsDto);

    ResponseEntity<?> getHotelFeaturesFromRoomDetails(long hotelId);

    ResponseEntity<?> getRoomFeaturesFromRoomDetails(long hotelId);
}
