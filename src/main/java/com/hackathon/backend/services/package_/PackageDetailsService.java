package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.PackageDetailsDto;
import com.hackathon.backend.dto.packageDto.PackageDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import com.hackathon.backend.repositories.package_.PackageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PackageDetailsService {

    private final PackageDetailsRepository packageDetailsRepository;
    private final PackageRepository packageRepository;

    @Autowired
    public PackageDetailsService(PackageDetailsRepository packageDetailsRepository,
                                 PackageRepository packageRepository) {
        this.packageDetailsRepository = packageDetailsRepository;
        this.packageRepository = packageRepository;
    }

    public ResponseEntity<?> getSinglePackageDetails(int packageId) {
        try{
            PackageEntity packageEntity = packageRepository.findById(packageId)
                    .orElseThrow(()-> new EntityNotFoundException("Package id not found"));
            PackageDetailsEntity packageDetails = packageEntity.getPackageDetails();
            PackageDetailsDto packageDetailsDto = new PackageDetailsDto(
                    packageDetails.getId(),
                    packageDetails.getImageOne(),
                    packageDetails.getImageTwo(),
                    packageDetails.getImageThree(),
                    packageDetails.getDescription()
            );
            PackageDto packageDto = new PackageDto(
                    packageEntity.getId(),
                    packageEntity.getPackageName(),
                    packageEntity.getPrice(),
                    packageEntity.getRate(),
                    packageEntity.getMainImage(),
                    packageDetailsDto,
                    packageDetails.getRoadmaps(),
                    packageDetails.getBenefits()
            );
            return ResponseEntity.ok(packageDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editPackageDetails(int packageDetailsId,
                                                PackageDetailsDto packageDetailsDto) {
        try{
            PackageDetailsEntity packageDetails = packageDetailsRepository.findById(packageDetailsId)
                    .orElseThrow(()-> new EntityNotFoundException("Package id not found"));
            editHelper(packageDetails, packageDetailsDto);
            packageDetailsRepository.save(packageDetails);
            return ResponseEntity.ok("Package details edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(PackageDetailsEntity packageDetails,
                            PackageDetailsDto packageDetailsDto) {
        if(packageDetailsDto.getImageOne() != null){
            packageDetails.setImageOne(packageDetails.getImageOne());
        }
        if(packageDetailsDto.getImageTwo() != null){
            packageDetails.setImageTwo(packageDetails.getImageTwo());
        }
        if(packageDetailsDto.getImageThree() != null){
            packageDetails.setImageThree(packageDetails.getImageThree());
        }
        if(packageDetailsDto.getDescription() != null){
            packageDetails.setDescription(packageDetails.getDescription());
        }
    }
}
