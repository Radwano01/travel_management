//package com.hackathon.backend.country.services;
//
//import com.hackathon.backend.dto.countryDto.CountryDetailsDto;
//import com.hackathon.backend.entities.country.CountryDetailsEntity;
//import com.hackathon.backend.entities.country.CountryEntity;
//import com.hackathon.backend.entities.country.PlaceEntity;
//import com.hackathon.backend.repositories.country.CountryDetailsRepository;
//import com.hackathon.backend.repositories.country.CountryRepository;
//import com.hackathon.backend.services.country.CountryDetailsService;
//import com.hackathon.backend.utilities.country.CountryUtils;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//class CountryDetailsServiceTest {
//
//    @Mock
//    private CountryUtils countryUtils;
//    @Mock
//    private CountryDetailsRepository countryDetailsRepository;
//
//    @Mock
//    private CountryRepository countryRepository;
//
//    private CountryDetailsService countryDetailsService;
//
//    @BeforeEach
//    void setUp() {
//        countryDetailsService = new CountryDetailsService(
//                countryRepository,
//                countryDetailsRepository,
//                countryUtils);
//    }
//
//    @AfterEach
//    void tearDown() {
//        countryDetailsRepository.deleteAll();
//        countryRepository.deleteAll();
//    }
//
//    @Test
//    void getSingleCountryDetails() {
//        //given
//        int countryId = 1;
//
//        CountryEntity country = new CountryEntity();
//        country.setCountry("United Kingdom");
//
//        PlaceEntity place = new PlaceEntity();
//        place.setPlace("London");
//        country.getPlaces().add(place);
//
//        CountryDetailsEntity countryDetails = new CountryDetailsEntity();
//        countryDetails.setId(countryId);
//        countryDetails.setImageOne("image_one");
//        countryDetails.setImageTwo("image_two");
//        countryDetails.setImageThree("image_three");
//        countryDetails.setCountry(country);
//
//        country.setCountryDetails(countryDetails);
//        when(countryUtils.findCountryById(countryId)).thenReturn((country));
//        //when
//        ResponseEntity<?> response = countryDetailsService.getSingleCountryDetails(countryId);
//        //then
//
//        assertNotNull(response.getBody());
//        CountryDetailsDto countryDetailsDto = (CountryDetailsDto) response.getBody();
//
//        assertEquals(country.getCountryDetails().getId(), countryDetailsDto.getId());
//        assertEquals(country.getCountryDetails().getImageOne(), countryDetailsDto.getImageOne());
//        assertEquals(country.getCountryDetails().getImageTwo(), countryDetailsDto.getImageTwo());
//        assertEquals(country.getCountryDetails().getImageThree(), countryDetailsDto.getImageThree());
//        assertEquals(country.getCountryDetails().getDescription(), countryDetailsDto.getDescription());
//        assertEquals(country.getCountryDetails().getCountry().getCountry(), countryDetailsDto.getCountry());
//        assertEquals(country.getPlaces(), countryDetailsDto.getPlaces());
//    }
//
//    @Test
//    void editCountryDetails() {
//        //given
//        int countryDetailsId = 1;
//        CountryDetailsDto countryDetailsDto = new CountryDetailsDto();
//        countryDetailsDto.setImageOne("image_one");
//        countryDetailsDto.setImageTwo("image_two");
//        countryDetailsDto.setImageThree("image_three");
//        countryDetailsDto.setDescription("description");
//        countryDetailsDto.setCountry("United Kingdom");
//
//        when(countryDetailsRepository.findById(countryDetailsId)).thenReturn(Optional.of(new CountryDetailsEntity()));
//        //when
//        ResponseEntity<?> response = countryDetailsService.editCountryDetails(countryDetailsId,countryDetailsDto);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//}