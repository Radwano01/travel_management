package com.hackathon.backend.services.package_.impl;

import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageANDPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.services.package_.PackageDetailsService;
import com.hackathon.backend.utilities.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PackageDetailsServiceImpl implements PackageDetailsService {

    private final PackageRepository packageRepository;
    private final S3Service s3Service;

    @Autowired
    public PackageDetailsServiceImpl(PackageRepository packageRepository,
                                     S3Service s3Service) {
        this.packageRepository = packageRepository;
        this.s3Service = s3Service;
    }

    @Override
    public ResponseEntity<GetPackageANDPackageDetailsDto> getSinglePackageDetails(int packageId) {
        PackageEntity packageEntity = findPackageById(packageId);

        List<GetRoadmapDto> roadmapDtos = packageEntity.getPackageDetails().getRoadmaps().stream()
                .map(roadmap -> new GetRoadmapDto(roadmap.getId(), roadmap.getRoadmap()))
                .collect(Collectors.toList());

        List<GetBenefitDto> benefitDtos = packageEntity.getPackageDetails().getBenefits().stream()
                .map(benefit -> new GetBenefitDto(benefit.getId(), benefit.getBenefit()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new GetPackageANDPackageDetailsDto(
                    packageEntity.getId(),
                    packageEntity.getPackageName(),
                    packageEntity.getPrice(),
                    packageEntity.getRate(),
                    packageEntity.getMainImage(),
                    packageEntity.getPackageDetails().getImageOne(),
                    packageEntity.getPackageDetails().getImageTwo(),
                    packageEntity.getPackageDetails().getImageThree(),
                    packageEntity.getPackageDetails().getDescription(),
                    roadmapDtos,
                    benefitDtos
                )
        );
    }


    private PackageEntity findPackageById(int packageId){
        return packageRepository.findById(packageId)
                .orElseThrow(()-> new EntityNotFoundException("No such package has this id"));
    }

    @Transactional
    @Override
    public ResponseEntity<String> editPackageDetails(int packageId,
                                                     EditPackageDetailsDto editPackageDetailsDto) {
        if(!checkIfSentEmptyData(editPackageDetailsDto)){
            return badRequestException("you sent an empty data to change");
        }

        PackageEntity packageEntity = findPackageById(packageId);

        updateToNewData(packageEntity.getPackageDetails(), editPackageDetailsDto);

        packageRepository.save(packageEntity);

        return ResponseEntity.ok(packageEntity.getPackageDetails().toString());
    }

    private void updateToNewData(PackageDetailsEntity packageDetails,
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
