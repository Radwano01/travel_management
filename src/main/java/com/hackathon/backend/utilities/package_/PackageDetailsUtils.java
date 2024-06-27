package com.hackathon.backend.utilities.package_;


import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PackageDetailsUtils {

    private final PackageDetailsRepository packageDetailsRepository;

    private final S3Service s3Service;

    @Autowired
    public PackageDetailsUtils(PackageDetailsRepository packageDetailsRepository,
                               S3Service s3Service) {
        this.packageDetailsRepository = packageDetailsRepository;
        this.s3Service = s3Service;
    }

    public void save(PackageDetailsEntity packageDetails) {
        packageDetailsRepository.save(packageDetails);
    }

    public List<PackageDetailsEntity> findAll() {
        return packageDetailsRepository.findAll();
    }

    public PackageDetailsEntity findById(int packageDetailsId) {
        return packageDetailsRepository.findById(packageDetailsId)
                .orElseThrow(()-> new EntityNotFoundException("Package id not found"));
    }

    public void delete(PackageDetailsEntity packageDetails) {
        packageDetailsRepository.delete(packageDetails);
    }

    public boolean checkHelper(EditPackageDetailsDto editPackageDetailsDto){
        return  editPackageDetailsDto.getImageOne() != null ||
                editPackageDetailsDto.getImageTwo() != null ||
                editPackageDetailsDto.getImageThree() != null ||
                editPackageDetailsDto.getDescription() != null;
    }

    public void editHelper(PackageDetailsEntity packageDetails,
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
