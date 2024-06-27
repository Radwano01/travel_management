package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.EditPackageDto;
import com.hackathon.backend.dto.packageDto.PostPackageDto;
import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import com.hackathon.backend.dto.packageDto.GetPackageDto;
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

import static com.hackathon.backend.utilities.ErrorUtils.*;

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
                                           @NonNull PostPackageDto postPackageDto){
        try{
            CountryEntity country = countryUtils.findCountryById(countryId);

            String packageMainImageName = s3Service.uploadFile(postPackageDto.getMainImage());

            PackageEntity packageEntity = new PackageEntity(
                    postPackageDto.getPackageName(),
                    postPackageDto.getPrice(),
                    packageMainImageName,
                    postPackageDto.getRate(),
                    country
            );

            packageUtils.save(packageEntity);
            country.getPackages().add(packageEntity);
            countryUtils.save(country);

            String packageDetailsImageOneName = s3Service.uploadFile(postPackageDto.getImageOne());
            String packageDetailsImageTwoName = s3Service.uploadFile(postPackageDto.getImageTwo());
            String packageDetailsImageThreeName = s3Service.uploadFile(postPackageDto.getImageThree());

            PackageDetailsEntity packageDetails = new PackageDetailsEntity(
                    packageDetailsImageOneName,
                    packageDetailsImageTwoName,
                    packageDetailsImageThreeName,
                    postPackageDto.getDescription(),
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
            List<GetEssentialPackageDto> packages = packageUtils
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
                                         EditPackageDto editPackageDto){
        try{
            if(!packageUtils.checkHelper(editPackageDto)){
                return badRequestException("you sent an empty data to change");
            }
            PackageEntity packageEntity = packageUtils.findById(packageId);
            packageUtils.editHelper(packageEntity, editPackageDto);
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

            String[] ls = new String[]{
                    packageDetails.getImageOne(),
                    packageDetails.getImageTwo(),
                    packageDetails.getImageThree()
            };
            s3Service.deleteFiles(ls);
            packageDetailsUtils.delete(packageDetails);
            s3Service.deleteFile(packageEntity.getMainImage());
            packageUtils.delete(packageEntity);
            return ResponseEntity.ok("Package deleted successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }
}
