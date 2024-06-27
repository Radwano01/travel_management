package com.hackathon.backend.utilities.package_;

import com.hackathon.backend.dto.packageDto.EditPackageDto;
import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PackageUtils {

    private final PackageRepository packageRepository;

    private final S3Service s3Service;

    @Autowired
    public PackageUtils(PackageRepository packageRepository,
                        S3Service s3Service) {
        this.packageRepository = packageRepository;
        this.s3Service = s3Service;
    }

    public void save(PackageEntity packageEntity) {
        packageRepository.save(packageEntity);
    }

    public List<GetEssentialPackageDto> findPackagesByCountryId(int countryId) {
        return packageRepository.findPackagesByCountryId(countryId);
    }

    public PackageEntity findById(int packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(()-> new EntityNotFoundException("package id not found"));
    }

    public void deleteById(int packageId) {
        packageRepository.deleteById(packageId);
    }

    public void delete(PackageEntity packageEntity) {
        packageRepository.delete(packageEntity);
    }

    public boolean checkHelper(EditPackageDto editPackageDto){
        return  editPackageDto.getPackageName() != null ||
                editPackageDto.getMainImage() != null ||
                editPackageDto.getPrice() != null ||
                editPackageDto.getRate() != null;
    }

    public void editHelper(PackageEntity packageEntity,
                            EditPackageDto editPackageDto) {
        if(editPackageDto.getPackageName() != null){
            packageEntity.setPackageName(editPackageDto.getPackageName());
        }
        if(editPackageDto.getMainImage() != null){
            s3Service.deleteFile(packageEntity.getMainImage());
            String packageMainImageName = s3Service.uploadFile(editPackageDto.getMainImage());
            packageEntity.setMainImage(packageMainImageName);
        }
        if(editPackageDto.getPrice() > 0){
            packageEntity.setPrice(editPackageDto.getPrice());
        }
        if(editPackageDto.getRate() > 0){
            packageEntity.setRate(editPackageDto.getRate());
        }
    }
}
