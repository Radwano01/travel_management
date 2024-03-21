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
@RequestMapping(path = "${package.controller.path}")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping(path = "${package.create.package.path}")
    public ResponseEntity<?> createPackage(@RequestBody PackageDto packageDto){
        return packageService.createPackage(packageDto);
    }

    @PostMapping(path = "${package.create.benefit.path}")
    public ResponseEntity<?> createBenefitForPackage(@RequestBody BenefitDto benefitDto){
        return packageService.createBenefitForPackage(benefitDto);
    }

    @PostMapping(path = "${package.create.roadmap.path}")
    public ResponseEntity<?> createRoadmapForPackage(@RequestBody PackageRoadmapDto roadmapDto) {
        return packageService.createRoadmapForPackage(roadmapDto);
    }

    @PostMapping(path = "${package.create.todo.path}")
    public ResponseEntity<?> createTodoForPackage(@RequestBody PackageTodosDto todosDto){
        return packageService.createTodosForPackage(todosDto);
    }

    @GetMapping(path = "${package.get.packages.path}")
    public ResponseEntity<?> getAllPackages(){
        return packageService.getAllPackages();
    }

    @GetMapping(path = "${package.get.packages.from.country.path}")
    public ResponseEntity<?> getAllPackagesFromCountry(@RequestBody PackageDto packageDto){
        return packageService.getAllPackagesFromCountry(packageDto);
    }

    @GetMapping(path = "${package.get.benefits.path}")
    public ResponseEntity<?> getAllBenefit(){
        return packageService.getAllBenefit();
    }

    @GetMapping(path = "${package.get.roadmaps.path}")
    public ResponseEntity<?> getAllRoadmapsFromCountry(){
        return packageService.getAllRoadmap();
    }

    @GetMapping(path = "${package.get.todos.path}")
    public ResponseEntity<?> getAllTodosFromCountry(){
        return packageService.getAllTodos();
    }

    @DeleteMapping(path = "${package.delete.package.path}")
    public ResponseEntity<?> deletePackage(@PathVariable int id){
        return packageService.deletePackage(id);
    }

    @DeleteMapping(path = "${package.delete.benefit.path}")
    public ResponseEntity<?> deleteBenefit(@PathVariable int id){
        return packageService.deleteBenefit(id);
    }

    @DeleteMapping(path = "${package.delete.roadmap.path}")
    public ResponseEntity<?> deleteRoadmap(@PathVariable("id") int id){
        return packageService.deleteRoadmap(id);
    }

    @DeleteMapping(path = "${package.delete.todo.path}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") int id){
        return packageService.deleteTodos(id);
    }

    @PutMapping(path = "${package.edit.package.path}")
    public ResponseEntity<?> editPackage(@PathVariable int id, @RequestBody PackageDto packageDto){
        return packageService.editPackage(id,packageDto);
    }

    @PutMapping(path = "${package.edit.benefit.path}")
    public ResponseEntity<?> editBenefit(@PathVariable int id, @RequestBody BenefitDto benefitDto){
        return packageService.editBenefit(id, benefitDto);
    }

    @PutMapping(path = "${package.edit.roadmap.path}")
    public ResponseEntity<?> editRoadmap(@PathVariable("id") int id,@RequestBody PackageRoadmapDto roadmapDto){
        return packageService.editRoadmap(id, roadmapDto);
    }

    @PutMapping(path = "${package.edit.todo.path}")
    public ResponseEntity<?> editTodo(@PathVariable("id") int id,@RequestBody PackageTodosDto todosDto){
        return packageService.editTodos(id, todosDto);
    }
}

