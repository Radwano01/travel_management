package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.PackageDto.BenefitDto;
import com.hackathon.backend.Dto.PackageDto.PackageDto;
import com.hackathon.backend.Dto.PackageDto.PackageRoadmapDto;
import com.hackathon.backend.Dto.PackageDto.PackageTodosDto;
import com.hackathon.backend.Services.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/v1/package")
public class PackageController {

    private final PackageService packageService;


    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }



    @PostMapping(path = "/create-new-package")
    public ResponseEntity<?> createPackage(@RequestBody PackageDto packageDto){
        return packageService.createPackage(packageDto);
    }
    @PostMapping(path = "/create-new-benefit-for-package")
    public  ResponseEntity<?> createBenefitForPackage(@RequestBody BenefitDto benefitDto){
        return packageService.createBenefitForPackage(benefitDto);
    }
    @PostMapping(path = "/create-new-roadmap-for-package")
    public ResponseEntity<?> createRoadmapForPackage(@RequestBody PackageRoadmapDto roadmapDto) {
        return packageService.createRoadmapForPackage(roadmapDto);
    }
    @PostMapping(path = "/create-new-todo-for-package")
    public ResponseEntity<?> createTodoForPackage(@RequestBody PackageTodosDto todosDto){
        return packageService.createTodosForPackage(todosDto);
    }




    @GetMapping(path = "/get-all-packages")
    public ResponseEntity<?> getAllPackages(){
        return packageService.getAllPackages();
    }
    @GetMapping(path = "/get-all-packages/from-country")
    public ResponseEntity<?> getAllPackagesFromCountry(@RequestBody PackageDto packageDto){
        return packageService.getAllPackagesFromCountry(packageDto);
    }
    @GetMapping(path = "/get-all-benefits")
    public ResponseEntity<?> getAllBenefit(){
        return packageService.getAllBenefit();
    }
    @GetMapping(path = "/get-all-roadmaps")
    public ResponseEntity<?> getAllRoadmapsFromCountry(){
        return packageService.getAllRoadmap();
    }
    @GetMapping(path = "/get-all-todos")
    public ResponseEntity<?> getAllTodosFromCountry(){
        return packageService.getAllTodos();
    }






    @DeleteMapping(path = "/delete-package/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable int id){
        return packageService.deletePackage(id);
    }
    @DeleteMapping(path = "/delete-benefit/{id}")
    public ResponseEntity<?> deleteBenefit(@PathVariable int id){
        return packageService.deleteBenefit(id);
    }
    @DeleteMapping(path = "/delete-roadmap/{id}")
    public ResponseEntity<?> deleteRoadmap(@PathVariable("id") int id){
        return packageService.deleteRoadmap(id);
    }
    @DeleteMapping(path = "/delete-todo/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") int id){
        return packageService.deleteTodos(id);
    }




    @PutMapping(path = "/edit-package/{id}")
    public ResponseEntity<?> editPackage(@PathVariable int id, @RequestBody PackageDto packageDto){
        return packageService.editPackage(id,packageDto);
    }
    @PutMapping(path = "/edit-benefit/{id}")
    public ResponseEntity<?> editBenefit(@PathVariable int id, @RequestBody BenefitDto benefitDto){
        return packageService.editBenefit(id, benefitDto);
    }
    @PutMapping(path = "/edit-roadmap/{id}")
    public ResponseEntity<?> editRoadmap(@PathVariable("id") int id,@RequestBody PackageRoadmapDto roadmapDto){
        return packageService.editRoadmap(id, roadmapDto);
    }
    @PutMapping(path = "/edit-todo/{id}")
    public ResponseEntity<?> editTodo(@PathVariable("id") int id,@RequestBody PackageTodosDto todosDto){
        return packageService.editTodos(id, todosDto);
    }
}
