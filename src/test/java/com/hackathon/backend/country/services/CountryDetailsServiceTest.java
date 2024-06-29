package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.EditCountryDetailsDto;
import com.hackathon.backend.dto.countryDto.GetCountryDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.services.country.CountryDetailsService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryDetailsServiceTest {

    @Mock
    private CountryUtils countryUtils;

    @Mock
    private CountryDetailsUtils countryDetailsUtils;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private CountryDetailsService countryDetailsService;

    @Test
    void getSingleCountryDetails() {
        // given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);
        CountryDetailsEntity countryDetails = new CountryDetailsEntity(
                "testImageOne",
                "testImageTwo",
                "testImageThree",
                "testDesc",
                country
        );
        country.setCountryDetails(countryDetails);

        // behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        // when
        ResponseEntity<?> response = countryDetailsService.getSingleCountryDetails(countryId);
        GetCountryDetailsDto getCountryDetailsDto = (GetCountryDetailsDto) response.getBody();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert getCountryDetailsDto != null;
        assertEquals(countryDetails.getImageOne(), getCountryDetailsDto.getImageOne());
        assertEquals(countryDetails.getImageTwo(), getCountryDetailsDto.getImageTwo());
        assertEquals(countryDetails.getImageThree(), getCountryDetailsDto.getImageThree());
        assertEquals(countryDetails.getDescription(), getCountryDetailsDto.getDescription());
        assertEquals(country.getCountry(), getCountryDetailsDto.getCountry());
        assertEquals(country.getMainImage(), getCountryDetailsDto.getCountryMainImage());
    }

    @Test
    void editCountryDetails() {
        //given
        EditCountryDetailsDto dto = new EditCountryDetailsDto();
        dto.setDescription("New Description");

        CountryDetailsEntity countryDetails = new CountryDetailsEntity();
        CountryEntity country = new CountryEntity();
        country.setCountryDetails(countryDetails);

        //behavior
        when(countryDetailsUtils.checkHelper(dto)).thenReturn(true);
        when(countryUtils.findCountryById(1)).thenReturn(country);

        //when
        ResponseEntity<?> response = countryDetailsService.editCountryDetails(1, dto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(countryDetailsUtils).checkHelper(dto);
        verify(countryUtils).findCountryById(1);
        verify(countryDetailsUtils).editHelper(countryDetails, dto);
        verify(countryUtils).save(country);
        verify(countryDetailsUtils).save(countryDetails);
    }
}
