package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.EditRoomDetailsDto;
import com.hackathon.backend.dto.hotelDto.GetRoomDetailsDto;
import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.GetRoomFeaturesDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.services.hotel.impl.RoomDetailsServiceImpl;
import com.hackathon.backend.utilities.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomDetailsServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private RoomDetailsRepository roomDetailsRepository;

    @InjectMocks
    private RoomDetailsServiceImpl roomDetailsServiceImpl;

    private HotelEntity hotel;
    private RoomDetailsEntity roomDetails;

    private List<GetHotelFeaturesDto> hotelFeaturesList;
    private List<GetRoomFeaturesDto> roomFeaturesList;

    @BeforeEach
    void setUp() {
        hotelFeaturesList = List.of(
                new GetHotelFeaturesDto(1, "Free WiFi"),
                new GetHotelFeaturesDto(2, "Swimming Pool")
        );

        // Mock data for GetRoomFeaturesDto
        roomFeaturesList = List.of(
                new GetRoomFeaturesDto(1, "Air Conditioning"),
                new GetRoomFeaturesDto(2, "Room Service")
        );

        roomDetails = new RoomDetailsEntity();
        roomDetails.setImageOne("imageOne.jpg");
        roomDetails.setImageTwo("imageTwo.jpg");
        roomDetails.setImageThree("imageThree.jpg");
        roomDetails.setImageFour("imageFour.jpg");
        roomDetails.setDescription("Room description");
        roomDetails.setPrice(100);

        hotel = new HotelEntity();
        hotel.setId(1L);
        hotel.setHotelName("Test Hotel");
        hotel.setAddress("123 Test Street");
        hotel.setRate(4);
        hotel.setRoomDetails(roomDetails);
    }

    @AfterEach
    void tearDown(){
        hotelRepository.deleteAll();
    }

    @Test
    void getHotelRoomDetailsByHotelId() {
        // given
        long hotelId = 1L;

        //behavior
        when(hotelRepository.findById(hotelId)).thenReturn(java.util.Optional.ofNullable(hotel));

        //when
        ResponseEntity<GetRoomDetailsDto> response = roomDetailsServiceImpl.getHotelRoomDetailsByHotelId(hotelId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GetRoomDetailsDto roomDetailsDto = response.getBody();
        assertNotNull(roomDetailsDto);
        assertEquals(hotel.getHotelName(), roomDetailsDto.getHotelName());
        assertEquals(roomDetails.getImageOne(), roomDetailsDto.getImageOne());
    }

    @Test
    void getHotelFeaturesFromRoomDetails_ShouldReturnHotelFeatures() {
        // given
        long hotelId = 1L;

        // behavior
        when(roomDetailsRepository.findRoomDetailsHotelFeaturesByHotelId(hotelId)).thenReturn(hotelFeaturesList);

        // when
        ResponseEntity<List<GetHotelFeaturesDto>> response = roomDetailsServiceImpl.getHotelFeaturesFromRoomDetails(hotelId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Free WiFi", response.getBody().get(0).getHotelFeature());
        assertEquals("Swimming Pool", response.getBody().get(1).getHotelFeature());

        // verify
        verify(roomDetailsRepository, times(1)).findRoomDetailsHotelFeaturesByHotelId(hotelId);
    }

    @Test
    void getRoomFeaturesFromRoomDetails_ShouldReturnRoomFeatures() {
        // given
        long hotelId = 1L;

        // behavior
        when(roomDetailsRepository.findRoomDetailsRoomFeaturesByHotelId(hotelId)).thenReturn(roomFeaturesList);

        // when
        ResponseEntity<List<GetRoomFeaturesDto>> response = roomDetailsServiceImpl.getRoomFeaturesFromRoomDetails(hotelId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Air Conditioning", response.getBody().get(0).getRoomFeature());
        assertEquals("Room Service", response.getBody().get(1).getRoomFeature());

        // verify
        verify(roomDetailsRepository, times(1)).findRoomDetailsRoomFeaturesByHotelId(hotelId);
    }

    @Test
    void getHotelRoomDetailsByHotelId_HotelNotFound() {
        // given
        long hotelId = 1L;

        //behavior
        when(hotelRepository.findById(hotelId)).thenReturn(java.util.Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> roomDetailsServiceImpl.getHotelRoomDetailsByHotelId(hotelId));
    }

    @Test
    void editRoomDetails() {
        // //given
        long hotelId = 1L;
        EditRoomDetailsDto editRoomDetailsDto = new EditRoomDetailsDto(
                new MockMultipartFile("imageOne", "updatedImageOne.jpg", "image/jpeg", "new-image-content".getBytes()),
                null,
                null,
                null,
                "Updated description",
                200
        );


        //behavior
        when(hotelRepository.findById(hotelId)).thenReturn(java.util.Optional.ofNullable(hotel));
        when(s3Service.uploadFile(editRoomDetailsDto.getImageOne())).thenReturn("updatedImageOne.jpg");

        // when
        ResponseEntity<String> response = roomDetailsServiceImpl.editRoomDetails(hotelId, editRoomDetailsDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updatedImageOne.jpg", hotel.getRoomDetails().getImageOne());
        assertEquals(200, hotel.getRoomDetails().getPrice());
        assertEquals("Updated description", hotel.getRoomDetails().getDescription());

        verify(s3Service, times(1)).deleteFile("imageOne.jpg");
        verify(s3Service, times(1)).uploadFile(editRoomDetailsDto.getImageOne());
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    void editRoomDetails_WithEmptyData_ShouldReturnBadRequest() {
        // given
        long hotelId = 1L;
        EditRoomDetailsDto editRoomDetailsDto = new EditRoomDetailsDto();

        // when
        ResponseEntity<String> response = roomDetailsServiceImpl.editRoomDetails(hotelId, editRoomDetailsDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("you sent an empty data to change", response.getBody());
    }

    @Test
    void editRoomDetails_HotelNotFound() {
        // given
        long hotelId = 1L;
        EditRoomDetailsDto editRoomDetailsDto = new EditRoomDetailsDto(
                new MockMultipartFile("imageOne", "updatedImageOne.jpg", "image/jpeg", "new-image-content".getBytes()),
                null,
                null,
                null,
                "Updated description",
                200
        );

        //behavior
        when(hotelRepository.findById(hotelId)).thenReturn(java.util.Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> roomDetailsServiceImpl.editRoomDetails(hotelId, editRoomDetailsDto));
    }
}
