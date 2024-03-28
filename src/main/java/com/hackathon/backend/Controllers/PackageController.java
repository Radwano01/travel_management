package com.hackathon.backend.Controllers;

import com.hackathon.backend.Dto.PackageDto.BenefitDto;
import com.hackathon.backend.Dto.PackageDto.PackageDto;
import com.hackathon.backend.Dto.PackageDto.PackageRoadmapDto;
import com.hackathon.backend.Dto.PackageDto.PackageTodosDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Services.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@Controller
@RequestMapping(path = "${PACKAGE_API_PATH}")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping(path = "${PACKAGE_CREATE_PACKAGE_PATH}")
    public ResponseEntity<?> createPackage(@PathVariable("countryID") int countryID, @RequestBody PackageDto packageDto){
        return packageService.createPackage(countryID,packageDto);
    }

    @PostMapping(path = "${PACKAGE_CREATE_BENEFIT_PATH}")
    public ResponseEntity<?> createBenefitForPackage(@PathVariable("packageID") int packageID, @RequestBody BenefitDto benefitDto){
        return packageService.createBenefitForPackage(packageID,benefitDto);
    }

    @PostMapping(path = "${PACKAGE_CREATE_ROADMAP_PATH}")
    public ResponseEntity<?> createRoadmapForPackage(@PathVariable("packageID") int packageID, @RequestBody PackageRoadmapDto roadmapDto) {
        return packageService.createRoadmapForPackage(packageID,roadmapDto);
    }

    @PostMapping(path = "${PACKAGE_CREATE_TODO_PATH}")
    public ResponseEntity<?> createTodoForPackage(@PathVariable("packageID") int packageID, @RequestBody PackageTodosDto todosDto){
        return packageService.createTodosForPackage(packageID,todosDto);
    }

    @GetMapping(path = "${PACKAGE_GET_PACKAGES_PATH}")
    public ResponseEntity<?> getAllPackages(){
        return packageService.getAllPackages();
    }


    @GetMapping(path = "${PACKAGE_GET_SINGLE_PATH}")
    public ResponseEntity<?> getSinglePackage(@PathVariable("packageID") int packageID){
        return packageService.getSinglePackage(packageID);
    }

    @GetMapping(path = "${PACKAGE_GET_BENEFITS_PATH}")
    public ResponseEntity<?> getAllBenefit(){
        return packageService.getAllBenefit();
    }

    @GetMapping(path = "${PACKAGE_GET_ROADMAPS_PATH}")
    public ResponseEntity<?> getAllRoadmapsFromCountry(){
        return packageService.getAllRoadmap();
    }

    @GetMapping(path = "${PACKAGE_GET_TODOS_PATH}")
    public ResponseEntity<?> getAllTodosFromCountry(){
        return packageService.getAllTodos();
    }

    @GetMapping(path = "${PACKAGE_GET_IDS_FROM_COUNTRY_ID_PATH}")
    public ResponseEntity<?> getPackagesID(@PathVariable("countryID") int countryID){
        return packageService.getAllPackagesID(countryID);
    }

    @DeleteMapping(path = "${PACKAGE_DELETE_PACKAGE_PATH}")
    public ResponseEntity<?> deletePackage(@PathVariable("packageID") int packageID){
        return packageService.deletePackage(packageID);
    }

    @DeleteMapping(path = "${PACKAGE_DELETE_BENEFIT_PATH}")
    public ResponseEntity<?> deleteBenefit(@PathVariable("packageID") int packageID,@PathVariable("benefitID") int benefitID){
        return packageService.deleteBenefit(packageID,benefitID);
    }

    @DeleteMapping(path = "${PACKAGE_DELETE_ROADMAP_PATH}")
    public ResponseEntity<?> deleteRoadmap(@PathVariable("packageID") int packageID,@PathVariable("roadmapID") int roadmapID){
        return packageService.deleteRoadmap(packageID,roadmapID);
    }

    @DeleteMapping(path = "${PACKAGE_DELETE_TODO_PATH}")
    public ResponseEntity<?> deleteTodo(@PathVariable("packageID") int packageID,@PathVariable("todoID") int todoID){
        return packageService.deleteTodos(packageID,todoID);
    }

    @PutMapping(path = "${PACKAGE_EDIT_PACKAGE_PATH}")
    public ResponseEntity<?> editPackage(@PathVariable("packageID") int packageID, @RequestBody PackageDto packageDto){
        return packageService.editPackage(packageID,packageDto);
    }

    @PutMapping(path = "${PACKAGE_EDIT_BENEFIT_PATH}")
    public ResponseEntity<?> editBenefit(@PathVariable("benefitID") int benefitID, @RequestBody BenefitDto benefitDto){
        return packageService.editBenefit(benefitID, benefitDto);
    }

    @PutMapping(path = "${PACKAGE_EDIT_ROADMAP_PATH}")
    public ResponseEntity<?> editRoadmap(@PathVariable("roadmapID") int roadmapID, @RequestBody PackageRoadmapDto roadmapDto){
        return packageService.editRoadmap(roadmapID, roadmapDto);
    }

    @PutMapping(path = "${PACKAGE_EDIT_TODO_PATH}")
    public ResponseEntity<?> editTodo(@PathVariable("todoID") int todoID, @RequestBody PackageTodosDto todosDto){
        return packageService.editTodos(todoID, todosDto);
    }

}
