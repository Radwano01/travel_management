package com.hackathon.backend.Services;

import com.hackathon.backend.Dto.PackageDto.*;
import com.hackathon.backend.Dto.payment.PaymentDto;
import com.hackathon.backend.Entities.*;
import com.hackathon.backend.RelationShips.BenefitEntity;
import com.hackathon.backend.RelationShips.RoadmapEntity;
import com.hackathon.backend.RelationShips.TodoListEntity;
import com.hackathon.backend.Repositories.*;
import com.hackathon.backend.Utilities.UserFromToken;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final CountryRepository countryRepository;
    private final BenefitRepository benefitRepository;
    private final RoadmapRepository roadmapRepository;
    private final TodoListRepository todoListRepository;
    private final UserFromToken userFromToken;

    public PackageService(PackageRepository packageRepository,
                          CountryRepository countryRepository,
                          BenefitRepository benefitRepository,
                          RoadmapRepository roadmapRepository,
                          TodoListRepository todoListRepository,
                          UserFromToken userFromToken) {
        this.packageRepository = packageRepository;
        this.countryRepository = countryRepository;
        this.benefitRepository = benefitRepository;
        this.roadmapRepository = roadmapRepository;
        this.todoListRepository = todoListRepository;
        this.userFromToken = userFromToken;
    }

    public ResponseEntity<?> createPackage(PackageDto packageDto) {
        try{
            boolean existsPackage = packageRepository.existsByPackageName(packageDto.getPackageName());
            CountryEntity country = countryRepository.findByCountry(packageDto.getCountry())
                    .orElseThrow(()-> new EntityNotFoundException("Package Country is Not Found"));
            if(!existsPackage){
                PackageEntity packageEntity = new PackageEntity();
                packageEntity.setPackageName(packageDto.getPackageName());
                packageEntity.setCountry(country);
                packageEntity.setPrice(packageDto.getPrice());
                packageEntity.setDescription(packageDto.getDescription());
                packageRepository.save(packageEntity);
                return new ResponseEntity<>("Package Created Successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Package is already Valid", HttpStatus.FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> createBenefitForPackage(int packageID, BenefitDto benefitDto) {
        try {
            boolean existsBenefit = benefitRepository.existsByBenefit(benefitDto.getBenefit());
            if (!existsBenefit) {
                PackageEntity packageEntity = packageRepository.findById(packageID)
                        .orElseThrow(()-> new EntityNotFoundException("Package Id is Not Found"));

                BenefitEntity benefitEntity = new BenefitEntity();
                benefitEntity.setBenefit(benefitDto.getBenefit());
                benefitRepository.save(benefitEntity);

                packageEntity.getBenefits().add(benefitEntity);
                packageRepository.save(packageEntity);

                return new ResponseEntity<>("Benefit created successfully for package", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Benefit already exists", HttpStatus.FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> createRoadmapForPackage(int packageID,PackageRoadmapDto roadmapDto){
        try{
            boolean existsRoadmap = roadmapRepository.existsByRoadmap(roadmapDto.getRoadmap());
            PackageEntity packageEntity = packageRepository.findById(packageID)
                    .orElseThrow(()-> new EntityNotFoundException("Package Id is Not Found"));
            if(!existsRoadmap){
                RoadmapEntity roadmapEntity = new RoadmapEntity();
                roadmapEntity.setRoadmap(roadmapDto.getRoadmap());
                roadmapRepository.save(roadmapEntity);

                packageEntity.getRoadmaps().add(roadmapEntity);
                packageRepository.save(packageEntity);
                return new ResponseEntity<>("Roadmap Created Successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Road map Already Valid", HttpStatus.FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> createTodosForPackage(int packageID, PackageTodosDto todosDto){
        try{
            boolean existsTodos = todoListRepository.existsByTodos(todosDto.getTodos());
            PackageEntity packageEntity = packageRepository.findById(packageID)
                    .orElseThrow(()-> new EntityNotFoundException("Package Id is Not Found"));
            if(!existsTodos){
                TodoListEntity todoListEntity = new TodoListEntity();
                todoListEntity.setTodolist(todosDto.getTodos());
                todoListEntity.setCoins(todosDto.getCoins());
                todoListRepository.save(todoListEntity);

                packageEntity.getTodos().add(todoListEntity);
                return new ResponseEntity<>("Todos created",HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Todos Already Valid", HttpStatus.FOUND);
            }

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllPackages(){
        try{
            List<PackageEntity> packageEntityList = packageRepository.findAll();
            List<PackageDto> dto = new ArrayList<>();
            for(PackageEntity pack:packageEntityList){

                PackageDto packageDto = new PackageDto();
                packageDto.setId(pack.getId());
                packageDto.setPackageName(pack.getPackageName());
                packageDto.setCountry((pack.getCountry().getCountry()));
                packageDto.setDescription(pack.getDescription());
                packageDto.setPrice(pack.getPrice());
                packageDto.setBenefit(pack.getBenefits());
                packageDto.setTodo(pack.getTodos());
                packageDto.setRoadmap(pack.getRoadmaps());
                dto.add(packageDto);

            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getSinglePackage(int packageID) {
        try{
            PackageEntity packageEntity = packageRepository.findById(packageID)
                    .orElseThrow(()-> new EntityNotFoundException("Package Id is Not Found: "+packageID));
            return new ResponseEntity<>(packageEntity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllBenefit(){
        try{
            List<BenefitEntity> benefitEntities = benefitRepository.findAll();
            return new ResponseEntity<>(benefitEntities, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> getAllRoadmap(){
        try {
            List<RoadmapEntity> roadmapEntities = roadmapRepository.findAll();
            return new ResponseEntity<>(roadmapEntities, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> getAllTodos(){
        try {
            List<TodoListEntity> todoListEntities = todoListRepository.findAll();
            return new ResponseEntity<>(todoListEntities, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editPackage(int id,PackageDto packageDto){
        try{
            PackageEntity packageEntity = packageRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Package is Not Found"));

            CountryEntity countryEntity = countryRepository.findByCountry(packageDto.getCountry())
                    .orElseThrow(()-> new EntityNotFoundException("Country is Not Found"));
            if(packageDto.getPackageName() != null){
                packageEntity.setPackageName(packageDto.getPackageName());
            }
            if(packageDto.getCountry() != null){
                packageEntity.setCountry(countryEntity);
            }
            if(packageDto.getDescription() != null){
                packageEntity.setDescription(packageDto.getDescription());
            }
            if(packageDto.getPrice() != null){
                packageEntity.setPrice(packageDto.getPrice());
            }
            return new ResponseEntity<>("Package updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> editBenefit(int benefitID,BenefitDto benefitDto){
        try{
            BenefitEntity benefitEntity = benefitRepository.findById(benefitID)
                    .orElseThrow(()-> new EntityNotFoundException("Benefit Id is Not Found"));
            if(benefitDto.getBenefit() != null){
                benefitEntity.setBenefit(benefitDto.getBenefit());
            }
            return new ResponseEntity<>("Benefit updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> editRoadmap(int roadmapID, PackageRoadmapDto roadmapDto){
        try{
            RoadmapEntity roadmapEntity = roadmapRepository.findById(roadmapID)
                    .orElseThrow(()-> new EntityNotFoundException("Roadmap Id Not Found"));

            if(roadmapDto.getRoadmap() != null){
                roadmapEntity.setRoadmap(roadmapDto.getRoadmap());
            }
            roadmapRepository.save(roadmapEntity);
            return new ResponseEntity<>("Roadmap updated Successfully",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> editTodos(int todoID, PackageTodosDto todosDto){
        try{
            TodoListEntity todoListEntity = todoListRepository.findById(todoID)
                    .orElseThrow(()-> new EntityNotFoundException("Todos Id Not Found"));

            if(todosDto.getTodos() != null){
                todoListEntity.setTodolist(todosDto.getTodos());
            }
            if(todosDto.getCoins() != null){
                todoListEntity.setCoins(todosDto.getCoins());
            }
            todoListRepository.save(todoListEntity);
            return new ResponseEntity<>("Todos updated Successfully",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    public ResponseEntity<?> deletePackage(int id) {
        try{
            packageRepository.deleteById(id);
            return new ResponseEntity<>("Package deleted successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteBenefit(int id) {
        try{
            benefitRepository.deleteById(id);
            return new ResponseEntity<>("benefit deleted successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteRoadmap(int id){
        try{
            roadmapRepository.deleteById(id);
            return new ResponseEntity<>("Roadmap Deleted Successfully",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteTodos(int id){
        try{
            todoListRepository.deleteById(id);
            return new ResponseEntity<>("Todos Deleted Successfully",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> payment(PaymentDto paymentDto) {
        try{
            String token = paymentDto.getAccessToken();
            int userId = userFromToken.getUserIdFromToken(token);
            boolean userVerification = userFromToken.getUserVerificationFromToken(token);
            PackageEntity packageEntity = packageRepository.findById(paymentDto.getPaymentId())
                    .orElseThrow(()-> new EntityNotFoundException("Package Id is Not Found: "+paymentDto.getPaymentId()));
            if(userVerification){

            }

            return null;
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllPackagesID(int countryID) {
        try {
            List<PackageEntity> packageEntity = packageRepository.findAllByCountryId(countryID);
            List<PackageForCategoryDto> dto = new ArrayList<>();

            if (packageEntity.isEmpty()) {
                return new ResponseEntity<>("Country Has No Packages", HttpStatus.NOT_FOUND);
            }
            for (PackageEntity pack : packageEntity) {
                PackageForCategoryDto packageForCategoryDto = new PackageForCategoryDto();
                packageForCategoryDto.setId(pack.getId());
                packageForCategoryDto.setPackageName(pack.getPackageName());
                packageForCategoryDto.setCountry(pack.getCountry().getCountry());
                packageForCategoryDto.setDescription(pack.getDescription());
                dto.add(packageForCategoryDto);
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
