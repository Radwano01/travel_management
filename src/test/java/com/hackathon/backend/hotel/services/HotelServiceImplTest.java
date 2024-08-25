package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.CreateHotelDto;
import com.hackathon.backend.dto.hotelDto.EditHotelDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.services.hotel.impl.HotelServiceImpl;
import com.hackathon.backend.utilities.S3Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    PlaceRepository placeRepository;

    @Mock
    HotelRepository hotelRepository;

    @Mock
    S3Service s3Service;

    @InjectMocks
    HotelServiceImpl hotelServiceImpl;

    PlaceEntity place;
    HotelEntity hotel;

    CreateHotelDto createHotelDto;

    EditHotelDto editHotelDto;

    @BeforeEach
    void setUp() {
        createHotelDto = new CreateHotelDto(
                "Random Hotel Name", // hotelName
                new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", "random-image-content".getBytes()),
                "This is a randomly generated hotel description.",
                120,
                "123 Random Street, Random City, Random Country",
                4,
                new MockMultipartFile("imageOne", "imageOne.jpg", "image/jpeg", "random-image-content".getBytes()),
                new MockMultipartFile("imageTwo", "imageTwo.jpg", "image/jpeg", "random-image-content".getBytes()),
                new MockMultipartFile("imageThree", "imageThree.jpg", "image/jpeg", "random-image-content".getBytes()),
                new MockMultipartFile("imageFour", "imageFour.jpg", "image/jpeg", "random-image-content".getBytes()),
                "This is a description for the room in the random hotel.",
                250
        );

        editHotelDto = new EditHotelDto(
                "Updated Hotel Name",
                new MockMultipartFile("mainImage", "updatedMainImage.jpg", "image/jpeg", "updated-image-content".getBytes()),
                "Updated description",
                150,
                "456 Updated Street, Updated City, Updated Country",
                400,
                5
        );

        hotel = new HotelEntity(
                createHotelDto.getHotelName(),
                "mainImage",
                createHotelDto.getDescription(),
                createHotelDto.getHotelRoomsCount(),
                createHotelDto.getAddress(),
                createHotelDto.getRate(),
                place
        );
        hotel.setId(1);


        place = new PlaceEntity();
        place.setId(1);
        place.getHotels().add(hotel);
    }

    @AfterEach
    void tearDown() {
        placeRepository.deleteAll();
        hotelRepository.deleteAll();
    }

    @Test
    void createHotel() {
        //given
        int placeId = 1;

        //behavior
        when(placeRepository.findById(placeId)).thenReturn(Optional.ofNullable(place));

        //when
        ResponseEntity<String> response = hotelServiceImpl.createHotel(placeId, createHotelDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(s3Service, times(1)).uploadFile(createHotelDto.getMainImage());
        verify(s3Service, times(1)).uploadFile(createHotelDto.getImageOne());
        verify(s3Service, times(1)).uploadFile(createHotelDto.getImageTwo());
        verify(s3Service, times(1)).uploadFile(createHotelDto.getImageThree());
    }

    @Test
    void getHotels() {
        // given
        int placeId = 1;
        int page = 0;
        int size = 10;
        GetHotelDto getHotelDto = new GetHotelDto();
        getHotelDto.setHotelName(hotel.getHotelName());
        List<GetHotelDto> hotelDtos = List.of(getHotelDto);
        Page<GetHotelDto> pageImpl = new PageImpl<>(hotelDtos, PageRequest.of(page, size), hotelDtos.size());

        // behavior
        when(placeRepository.findHotelByPlaceId(placeId, PageRequest.of(page, size)))
                .thenReturn(pageImpl);

        // when
        ResponseEntity<List<GetHotelDto>> response = hotelServiceImpl.getHotels(placeId, page, size);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(hotel.getHotelName(), response.getBody().get(0).getHotelName());
    }



    @Test
    void editHotel() {
        // given
        int placeId = 1;
        long hotelId = 1L;

        // behavior
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        // when
        ResponseEntity<String> response = hotelServiceImpl.editHotel(placeId, hotelId, editHotelDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Updated Hotel Name"));
        assertTrue(response.getBody().contains("Updated description"));

        verify(s3Service, times(1)).uploadFile(editHotelDto.getMainImage());
    }

    @Test
    void editHotel_withEmptyData_shouldReturnBadRequest() {
        // Given
        int placeId = 1;
        long hotelId = 1;
        EditHotelDto editHotelDto = new EditHotelDto();

        // When
        ResponseEntity<String> response = hotelServiceImpl.editHotel(placeId, hotelId, editHotelDto);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }



    @Test
    void deleteHotel() {
        // given
        int placeId = 1;
        long hotelId = 1L;

        // Initialize the RoomDetailsEntity for the hotel
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        roomDetails.setImageOne("testImageOne.png");
        roomDetails.setImageTwo("testImageTwo.png");
        roomDetails.setImageThree("testImageThree.png");
        hotel.setRoomDetails(roomDetails);

        // behavior
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        // when
        ResponseEntity<String> response = hotelServiceImpl.deleteHotel(placeId, hotelId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(place.getHotels().contains(hotel));
        assertEquals("Hotel deleted Successfully", response.getBody());

        // Verify that the images were deleted using the S3 service
        verify(s3Service, times(1)).deleteFile(hotel.getMainImage());
        verify(s3Service, times(1)).deleteFile(roomDetails.getImageOne());
        verify(s3Service, times(1)).deleteFile(roomDetails.getImageTwo());
        verify(s3Service, times(1)).deleteFile(roomDetails.getImageThree());
    }


}