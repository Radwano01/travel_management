package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.CreateHotelDto;
import com.hackathon.backend.dto.hotelDto.EditHotelDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.List;

public interface HotelService {

    ResponseEntity<String> createHotel(int placeId, @NonNull CreateHotelDto createHotelDto);

    ResponseEntity<List<GetHotelDto>> getHotels(int placeId, int page, int size);

    ResponseEntity<String> editHotel(int placeId, long hotelId, EditHotelDto editHotelDto);

    ResponseEntity<String> deleteHotel(int placeId, long hotelId);
}
