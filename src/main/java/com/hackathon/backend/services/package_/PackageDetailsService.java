package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PackageDetailsService {

    private final PackageDetailsUtils packageDetailsUtils;
    private final PackageUtils packageUtils;
    private final S3Service s3Service;

    @Autowired
    public PackageDetailsService(PackageDetailsUtils packageDetailsUtils,
                                 PackageUtils packageUtils,
                                 S3Service s3Service) {
        this.packageDetailsUtils = packageDetailsUtils;
        this.packageUtils = packageUtils;
        this.s3Service = s3Service;
    }

    public ResponseEntity<?> getSinglePackageDetails(int packageId) {
        try{
            PackageEntity packageEntity = packageUtils.findById(packageId);
            PackageDetailsEntity packageDetails = packageEntity.getPackageDetails();
            GetPackageDetailsDto getPackageDetailsDto = new GetPackageDetailsDto(
                    packageDetails.getId(),
                    packageDetails.getImageOne(),
                    packageDetails.getImageTwo(),
                    packageDetails.getImageThree(),
                    packageDetails.getDescription()
            );
            GetPackageDto getPackageDto = new GetPackageDto(
                    packageEntity.getId(),
                    packageEntity.getPackageName(),
                    packageEntity.getPrice(),
                    packageEntity.getRate(),
                    packageEntity.getMainImage(),
                    getPackageDetailsDto,
                    packageDetails.getRoadmaps(),
                    packageDetails.getBenefits()
            );
            return ResponseEntity.ok(getPackageDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editPackageDetails(int packageDetailsId,
                                                EditPackageDetailsDto editPackageDetailsDto) {
        try{
            PackageDetailsEntity packageDetails = packageDetailsUtils.findById(packageDetailsId);
            editHelper(packageDetails, editPackageDetailsDto);
            packageDetailsUtils.save(packageDetails);
            return ResponseEntity.ok("Package details edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(PackageDetailsEntity packageDetails,
                            EditPackageDetailsDto editPackageDetailsDto) {
        if(editPackageDetailsDto.getImageOne() != null){
            s3Service.deleteFile(packageDetails.getImageOne());
            String packageDetailsImageOneName = s3Service.uploadFile(editPackageDetailsDto.getImageOne());
            packageDetails.setImageOne(packageDetailsImageOneName);
        }
        if(editPackageDetailsDto.getImageTwo() != null){
            s3Service.deleteFile(packageDetails.getImageTwo());
            String packageDetailsImageTwoName = s3Service.uploadFile(editPackageDetailsDto.getImageTwo());
            packageDetails.setImageTwo(packageDetailsImageTwoName);
        }
        if(editPackageDetailsDto.getImageThree() != null){
            s3Service.deleteFile(packageDetails.getImageThree());
            String packageDetailsImageThreeName = s3Service.uploadFile(editPackageDetailsDto.getImageThree());
            packageDetails.setImageThree(packageDetailsImageThreeName);
        }
        if(editPackageDetailsDto.getDescription() != null){
            packageDetails.setDescription(editPackageDetailsDto.getDescription());
        }
    }
}
