package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.CreatePackageDto;
import com.hackathon.backend.dto.packageDto.EditPackageDto;
import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.utilities.S3Service;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PackageService {

    private final CountryRepository countryRepository;
    private final S3Service s3Service;

    @Autowired
    public PackageService(CountryRepository countryRepository,
                          S3Service s3Service){
        this.countryRepository = countryRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public ResponseEntity<String> createPackage(int countryId, @NonNull CreatePackageDto createPackageDto){
        CountryEntity country = getCountryById(countryId);

        PackageEntity packageEntity = preparePackageEntity(createPackageDto, country);

        prepareANDSetPackageDetailsData(createPackageDto, packageEntity);

        country.getPackages().add(packageEntity);

        countryRepository.save(country);

        return ResponseEntity.ok(packageEntity.toString());
    }

    private CountryEntity getCountryById(int countryId){
        return countryRepository.findById(countryId)
                .orElseThrow(()-> new EntityNotFoundException("No such country has this id."));
    }

    private PackageEntity preparePackageEntity(CreatePackageDto createPackageDto, CountryEntity country){
        String packageMainImageName = s3Service.uploadFile(createPackageDto.getMainImage());

        return new PackageEntity(
                createPackageDto.getPackageName(),
                createPackageDto.getPrice(),
                packageMainImageName,
                createPackageDto.getRate(),
                country
        );
    }

    private void prepareANDSetPackageDetailsData(CreatePackageDto createPackageDto, PackageEntity packageEntity){
        String packageDetailsImageOneName = s3Service.uploadFile(createPackageDto.getImageOne());
        String packageDetailsImageTwoName = s3Service.uploadFile(createPackageDto.getImageTwo());
        String packageDetailsImageThreeName = s3Service.uploadFile(createPackageDto.getImageThree());

        PackageDetailsEntity packageDetails = new PackageDetailsEntity(
                packageDetailsImageOneName,
                packageDetailsImageTwoName,
                packageDetailsImageThreeName,
                createPackageDto.getDescription(),
                packageEntity
        );

        packageEntity.setPackageDetails(packageDetails);
    }

    public ResponseEntity<List<GetEssentialPackageDto>> getPackagesByCountry(int countryId){
        return ResponseEntity.ok(countryRepository.findPackagesByCountryId(countryId));
    }

    private PackageEntity findPackageByIdFromCountry(int countryId, int packageId){
        return countryRepository.findPackageByCountryIdAndPackageId(countryId, packageId)
                .orElseThrow(()-> new EntityNotFoundException("No such package has this id from country"));
    }

    @Transactional
    public ResponseEntity<String> editPackage(int countryId, int packageId, EditPackageDto editPackageDto){
        if(!checkIfSentEmptyData(editPackageDto)){
            return badRequestException("you sent an empty data to change");
        }

        CountryEntity country = getCountryById(countryId);

        PackageEntity packageEntity = findPackageByIdFromCountry(countryId, packageId);

        updateToNewData(packageEntity, editPackageDto);

        countryRepository.save(country);

        return ResponseEntity.ok(packageEntity.toString());
    }

    private void updateToNewData(PackageEntity packageEntity, EditPackageDto editPackageDto) {
        if(editPackageDto.getPackageName() != null){
            packageEntity.setPackageName(editPackageDto.getPackageName());
        }
        if(editPackageDto.getMainImage() != null){
            s3Service.deleteFile(packageEntity.getMainImage());
            String packageMainImageName = s3Service.uploadFile(editPackageDto.getMainImage());
            packageEntity.setMainImage(packageMainImageName);
        }
        if(editPackageDto.getPrice() != null && editPackageDto.getPrice() > 0){
            packageEntity.setPrice(editPackageDto.getPrice());
        }
        if(editPackageDto.getRate() != null && editPackageDto.getRate() > 0){
            packageEntity.setRate(editPackageDto.getRate());
        }
    }

    @Transactional
    public ResponseEntity<String> deletePackage(int countryId, int packageId){
        CountryEntity country = getCountryById(countryId);

        PackageEntity packageEntity = findPackageByIdFromCountry(countryId, packageId);

        removeBenefitsANDRoadmapsFromPackageDetails(packageEntity.getPackageDetails());

        deletePackageANDPackageDetailsImages(packageEntity);

        deletePackageANDPackageDetailsFromDB(country, packageEntity);

        return ResponseEntity.ok("Package deleted successfully");
    }

    private void deletePackageANDPackageDetailsFromDB(CountryEntity country, PackageEntity packageEntity) {
        country.getPackages().remove(packageEntity);
        countryRepository.save(country);
    }

    private void removeBenefitsANDRoadmapsFromPackageDetails(PackageDetailsEntity packageDetails){
        if (packageDetails.getBenefits() != null){
            packageDetails.getBenefits().clear();
        }

        if(packageDetails.getRoadmaps() != null){
            packageDetails.getRoadmaps().clear();
        }
    }

    private void deletePackageANDPackageDetailsImages(PackageEntity packageEntity){
        s3Service.deleteFile(packageEntity.getPackageDetails().getImageOne());
        s3Service.deleteFile(packageEntity.getPackageDetails().getImageTwo());
        s3Service.deleteFile(packageEntity.getPackageDetails().getImageThree());


        s3Service.deleteFile(packageEntity.getMainImage());
    }
}
