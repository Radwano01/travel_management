package com.hackathon.backend.repositories.country;

import com.hackathon.backend.dto.countryDto.GetCountryDto;
import com.hackathon.backend.dto.countryDto.GetCountryWithCountryDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CountryRepository extends JpaRepository<CountryEntity,Integer> {
    boolean existsByCountry(String country);
    @Query("SELECT new com.hackathon.backend.dto.countryDto.GetCountryDto(c.id, c.country, c.mainImage) FROM CountryEntity c")
    List<GetCountryDto> findAllCountries();

    @Query("SELECT new com.hackathon.backend.dto.countryDto.GetCountryWithCountryDetailsDto" +
            "(c.id, c.country, c.mainImage, d.imageOne, d.imageTwo, d.imageThree, d.description)" +
            " FROM CountryEntity c JOIN c.countryDetails d WHERE c.id = :countryId")
    Optional<GetCountryWithCountryDetailsDto> findCountryWithCountryDetailsByCountryId(int countryId);

    @Query("SELECT new com.hackathon.backend.dto.packageDto.GetEssentialPackageDto" +
            "(p.id, p.packageName, p.mainImage, p.price)" +
            " FROM CountryEntity c JOIN c.packages p WHERE c.id = :countryId")
    List<GetEssentialPackageDto> findPackagesByCountryId(int countryId);

    @Query("SELECT p FROM CountryEntity c JOIN c.packages p WHERE c.id = :countryId AND p.id = :packageId")
    Optional<PackageEntity> findPackageByCountryIdAndPackageId(int countryId, int packageId);

    @Query("SELECT new com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto" +
            "(p.id,p.place,p.mainImage)" +
            "FROM CountryEntity c JOIN c.places p WHERE c.id = :countryId")
    List<GetEssentialPlaceDto> findEssentialPlacesDataByCountryId(int countryId);

    @Query("SELECT p FROM CountryEntity c JOIN c.places p WHERE c.id = :countryId AND p.id = :placeId")
    Optional<PlaceEntity> findPlaceByCountryIdANDPlaceId(int countryId, int placeId);
}
