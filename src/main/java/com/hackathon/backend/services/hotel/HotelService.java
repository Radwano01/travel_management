package com.hackathon.backend.services.hotel;

import com.hackathon.backend.dto.hotelDto.CreateHotelDto;
import com.hackathon.backend.dto.hotelDto.EditHotelDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.utilities.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class HotelService {

    private final PlaceRepository placeRepository;
    private final HotelRepository hotelRepository;
    private final S3Service s3Service;

    @Autowired
    public HotelService(PlaceRepository placeRepository,
                        HotelRepository hotelRepository,
                        S3Service s3Service){
        this.placeRepository = placeRepository;
        this.hotelRepository = hotelRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public ResponseEntity<String> createHotel(int placeId, @NonNull CreateHotelDto createHotelDto) {
        PlaceEntity place = getPlaceById(placeId);

        HotelEntity hotel = prepareANDSetHotelData(place, createHotelDto);

        prepareANDSetRoomDetailsData(hotel, createHotelDto);

        updateHotelInDB(hotel);

        return ResponseEntity.ok(hotel.toString());
    }

    private PlaceEntity getPlaceById(int placeId){
        return placeRepository.findById(placeId)
                .orElseThrow(()-> new EntityNotFoundException("Not such place has this id"));
    }

    private void updateHotelInDB(HotelEntity hotel) {
        hotelRepository.save(hotel);
    }

    private HotelEntity prepareANDSetHotelData(PlaceEntity place, CreateHotelDto createHotelDto){
        String hotelImageName = s3Service.uploadFile(createHotelDto.getMainImage());

        return new HotelEntity(
                createHotelDto.getHotelName(),
                hotelImageName,
                createHotelDto.getDescription(),
                createHotelDto.getHotelRoomsCount(),
                createHotelDto.getAddress(),
                createHotelDto.getRate(),
                place
        );
    }

    private void prepareANDSetRoomDetailsData(HotelEntity hotel, CreateHotelDto createHotelDto) {
        String roomDetailsImageNameOne = s3Service.uploadFile(createHotelDto.getImageOne());
        String roomDetailsImageNameTwo = s3Service.uploadFile(createHotelDto.getImageTwo());
        String roomDetailsImageNameThree = s3Service.uploadFile(createHotelDto.getImageThree());
        String roomDetailsImageNameFour = s3Service.uploadFile(createHotelDto.getImageFour());

        RoomDetailsEntity roomDetails = new RoomDetailsEntity(
                roomDetailsImageNameOne,
                roomDetailsImageNameTwo,
                roomDetailsImageNameThree,
                roomDetailsImageNameFour,
                createHotelDto.getRoomDescription(),
                createHotelDto.getPrice(),
                hotel
        );

        hotel.setRoomDetails(roomDetails);
    }

    public ResponseEntity<List<GetHotelDto>> getHotels(int placeId, int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(placeRepository.findHotelByPlaceId(placeId, pageRequest).getContent());
    }

    @Transactional
    public ResponseEntity<String> editHotel(int placeId, long hotelId, EditHotelDto editHotelDto){
        if(!checkIfSentEmptyData(editHotelDto)){
            return badRequestException("you sent an empty data to change");
        }

        PlaceEntity place = getPlaceById(placeId);

        HotelEntity hotel = getHotelFromPlaceByHotelId(place, hotelId);

        updateToNewData(hotel, editHotelDto);

        placeRepository.save(place);

        return ResponseEntity.ok(hotel.toString());
    }

    private void updateToNewData(HotelEntity hotel, EditHotelDto editHotelDto) {
        if (editHotelDto.getHotelName() != null) {
            hotel.setHotelName(editHotelDto.getHotelName());
        }
        if (editHotelDto.getMainImage() != null) {
            s3Service.deleteFile(hotel.getMainImage());
            String hotelMainImageName = s3Service.uploadFile(editHotelDto.getMainImage());
            hotel.setMainImage(hotelMainImageName);
        }
        if (editHotelDto.getDescription() != null) {
            hotel.setDescription(editHotelDto.getDescription());
        }
        if (editHotelDto.getHotelRoomsCount() != null) {
            hotel.setHotelRoomsCount(editHotelDto.getHotelRoomsCount());
        }
        if (editHotelDto.getAddress() != null) {
            hotel.setAddress(editHotelDto.getAddress());
        }
        if (editHotelDto.getRate() != null && editHotelDto.getRate() > 0 && editHotelDto.getRate() <= 5) {
            hotel.setRate(editHotelDto.getRate());
        }
    }

    private HotelEntity getHotelFromPlaceByHotelId(PlaceEntity place, long hotelId) {
        return place.getHotels().stream()
                .filter(h -> h.getId() == hotelId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Hotel with ID " + hotelId + " not found in place"));
    }

    @Transactional
    public ResponseEntity<String> deleteHotel(int placeId, long hotelId) {
        PlaceEntity place = getPlaceById(placeId);

        HotelEntity hotel = getHotelFromPlaceByHotelId(place, hotelId);

        deleteHotelANDRoomDetailsImages(hotel);

        removeHotelANDRoomFeatures(hotel.getRoomDetails());

        deleteHotelDetailsFromHotelANDSaveEmptyFeatures(hotel);

        removeHotelFromPlace(place, hotel);

        placeRepository.save(place);

        return ResponseEntity.ok("Hotel deleted Successfully");
    }
    private void removeHotelANDRoomFeatures(RoomDetailsEntity roomDetails){
        if (roomDetails.getHotelFeatures() != null){
            roomDetails.getHotelFeatures().clear();
        }

        if(roomDetails.getRoomFeatures() != null){
            roomDetails.getRoomFeatures().clear();
        }
    }

    private void deleteHotelANDRoomDetailsImages(HotelEntity hotel){
        s3Service.deleteFile(hotel.getMainImage());

        s3Service.deleteFile(hotel.getRoomDetails().getImageOne());
        s3Service.deleteFile(hotel.getRoomDetails().getImageTwo());
        s3Service.deleteFile(hotel.getRoomDetails().getImageThree());
        s3Service.deleteFile(hotel.getRoomDetails().getImageFour());
    }

    private void deleteHotelDetailsFromHotelANDSaveEmptyFeatures(HotelEntity hotel) {
        hotel.setRoomDetails(null);

        updateHotelInDB(hotel);
    }

    private void removeHotelFromPlace(PlaceEntity placeFromCountry, HotelEntity hotel) {
        placeFromCountry.getHotels().remove(hotel);
    }
}
