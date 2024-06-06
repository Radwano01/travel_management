package com.hackathon.backend.services.package_;

import com.hackathon.backend.controllers.package_.PostP;
import com.hackathon.backend.dto.packageDto.EssentialPackageDto;
import com.hackathon.backend.dto.packageDto.PackageDetailsDto;
import com.hackathon.backend.dto.packageDto.PackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.PackageEvaluationUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.package_.features.BenefitUtils;
import com.hackathon.backend.utilities.package_.features.RoadmapUtils;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PackageService {

    private final PackageUtils packageUtils;
    private final PackageDetailsUtils packageDetailsUtils;
    private final CountryUtils countryUtils;
    private final RoadmapUtils roadmapUtils;
    private final BenefitUtils benefitUtils;
    private final PackageEvaluationUtils packageEvaluationUtils;
    private final S3Service s3Service;

    @Autowired
    public PackageService(PackageUtils packageUtils, PackageDetailsUtils packageDetailsUtils,
                          CountryUtils countryUtils, RoadmapUtils roadmapUtils,
                          BenefitUtils benefitUtils, PackageEvaluationUtils packageEvaluationUtils,
                          S3Service s3Service){
        this.packageUtils = packageUtils;
        this.packageDetailsUtils = packageDetailsUtils;
        this.countryUtils = countryUtils;
        this.roadmapUtils = roadmapUtils;
        this.benefitUtils = benefitUtils;
        this.packageEvaluationUtils = packageEvaluationUtils;
        this.s3Service = s3Service;
    }

    public ResponseEntity<?> createPackage(int countryId,
                                           @NonNull PostP p){
        try{
            CountryEntity country = countryUtils.findCountryById(countryId);

            String packageMainImageName = s3Service.uploadFile(p.getMainImage());

            PackageEntity packageEntity = new PackageEntity(
                    p.getPackageName(),
                    p.getPrice(),
                    packageMainImageName,
                    country
            );

            packageUtils.save(packageEntity);
            country.getPackages().add(packageEntity);
            countryUtils.save(country);

            String packageDetailsImageOneName = s3Service.uploadFile(p.getImageOne());
            String packageDetailsImageTwoName = s3Service.uploadFile(p.getImageTwo());
            String packageDetailsImageThreeName = s3Service.uploadFile(p.getImageThree());

            PackageDetailsEntity packageDetails = new PackageDetailsEntity(
                    packageDetailsImageOneName,
                    packageDetailsImageTwoName,
                    packageDetailsImageThreeName,
                    p.getDescription(),
                    packageEntity
            );

            packageDetailsUtils.save(packageDetails);
            packageEntity.setPackageDetails(packageDetails);
            packageUtils.save(packageEntity);
            return ResponseEntity.ok("Package and package Details created successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getPackagesByCountry(int countryId){
        try{
            List<EssentialPackageDto> packages = packageUtils
                    .findPackagesByCountryId(countryId);
            return ResponseEntity.ok(packages);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editPackage(int packageId,
                                         PackageDto packageDto){
        try{
            PackageEntity packageEntity = packageUtils.findById(packageId);
            editHelper(packageEntity, packageDto);
            packageUtils.save(packageEntity);
            return ResponseEntity.ok("Package edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deletePackage(int packageId){
        try{
            PackageEntity packageEntity = packageUtils.findById(packageId);
            PackageDetailsEntity packageDetails = packageEntity.getPackageDetails();

            for(PackageEvaluationEntity evaluation:packageEntity.getPackageEvaluations()){
                packageEvaluationUtils.delete(evaluation);
            }

            for(RoadmapEntity roadmap:packageEntity.getPackageDetails().getRoadmaps()){
                packageDetails.getRoadmaps().remove(roadmap);
                roadmapUtils.save(roadmap);
            }

            for(BenefitEntity benefit:packageEntity.getPackageDetails().getBenefits()){
                packageDetails.getBenefits().remove(benefit);
                benefitUtils.save(benefit);
            }

            packageDetailsUtils.delete(packageDetails);
            packageUtils.delete(packageEntity);
            return ResponseEntity.ok("Package deleted successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(PackageEntity packageEntity,
                            PackageDto packageDto) {
        if(packageDto.getPackageName() != null){
            packageEntity.setPackageName(packageDto.getPackageName());
        }
        if(packageDto.getMainImage() != null){
            packageEntity.setMainImage(packageDto.getMainImage());
        }
        if(packageDto.getPrice() > 0){
            packageEntity.setPrice(packageDto.getPrice());
        }
        if(packageDto.getRate() > 0){
            packageEntity.setRate(packageDto.getRate());
        }
    }
}
