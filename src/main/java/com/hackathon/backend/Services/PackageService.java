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
import java.util.Optional;

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

    public ResponseEntity<?> createPackage(int countryID,PackageDto packageDto) {
        try{
            boolean existsPackage = packageRepository.existsByPackageName(packageDto.getPackageName());
            CountryEntity country = countryRepository.findById(countryID)
                    .orElseThrow(()-> new EntityNotFoundException("Package Country is Not Found"));
            if(!existsPackage){
                PackageEntity packageEntity = new PackageEntity();
                packageEntity.setPackageName(packageDto.getPackageName());
                packageEntity.setTitle(packageDto.getTitle());
                packageEntity.setPackageImage(packageDto.getPackageImage());
                packageEntity.setImageOne(packageDto.getImageOne());
                packageEntity.setImageTwo(packageDto.getImageTwo());
                packageEntity.setImageThree(packageDto.getImageThree());
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
           PackageEntity packageEntity = getPackageById(packageID);
            BenefitEntity benefitEntity = new BenefitEntity();
            benefitEntity.setBenefit(benefitDto.getBenefit());
            benefitRepository.save(benefitEntity);
            packageEntity.getBenefits().add(benefitEntity);
            packageRepository.save(packageEntity);
            return new ResponseEntity<>("Benefit created successfully for package", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> createRoadmapForPackage(int packageID,PackageRoadmapDto roadmapDto){
        try{

            PackageEntity packageEntity = getPackageById(packageID);
            RoadmapEntity roadmapEntity = new RoadmapEntity();
            roadmapEntity.setRoadmap(roadmapDto.getRoadmap());
            roadmapRepository.save(roadmapEntity);
            packageEntity.getRoadmaps().add(roadmapEntity);
            packageRepository.save(packageEntity);
            return new ResponseEntity<>("Roadmap Created Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> createTodosForPackage(int packageID, PackageTodosDto todosDto){
        try{
            PackageEntity packageEntity = getPackageById(packageID);
            TodoListEntity todoListEntity = new TodoListEntity();
            todoListEntity.setTodolist(todosDto.getTodos());
            todoListEntity.setCoins(todosDto.getCoins());
            todoListRepository.save(todoListEntity);
            packageEntity.getTodos().add(todoListEntity);
            return new ResponseEntity<>("Todos created",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllPackages(){
        try{
            List<PackageEntity> packageEntityList = packageRepository.findAll();
            List<PackageForCategoryDto> dto = new ArrayList<>();
            for (PackageEntity pack : packageEntityList) {
                PackageForCategoryDto packageForCategoryDto = new PackageForCategoryDto();
                packageForCategoryDto.setId(pack.getId());
                packageForCategoryDto.setTitle(pack.getTitle());
                packageForCategoryDto.setPackageName(pack.getPackageName());
                packageForCategoryDto.setPackageImage(pack.getPackageImage());
                packageForCategoryDto.setCountry(pack.getCountry().getCountry());
                dto.add(packageForCategoryDto);
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getSinglePackage(int packageID) {
        try{
            PackageEntity packageEntity = getPackageById(packageID);
            PackageDto packageDto = new PackageDto();
            packageDto.setId(packageEntity.getId());
            packageDto.setPackageName(packageEntity.getPackageName());
            packageDto.setTitle(packageEntity.getTitle());
            packageDto.setPackageImage(packageEntity.getPackageImage());
            packageDto.setImageOne(packageEntity.getImageOne());
            packageDto.setImageTwo(packageEntity.getImageTwo());
            packageDto.setImageThree(packageEntity.getImageThree());
            packageDto.setCountry(packageEntity.getCountry().getCountry());
            packageDto.setDescription(packageEntity.getDescription());
            packageDto.setPrice(packageEntity.getPrice());
            packageDto.setBenefit(packageEntity.getBenefits());
            packageDto.setTodo(packageEntity.getTodos());
            packageDto.setRoadmap(packageEntity.getRoadmaps());

            return new ResponseEntity<>(packageDto, HttpStatus.OK);
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
                packageForCategoryDto.setTitle(pack.getTitle());
                packageForCategoryDto.setPackageName(pack.getPackageName());
                packageForCategoryDto.setPackageImage(pack.getPackageImage());
                packageForCategoryDto.setCountry(pack.getCountry().getCountry());

                dto.add(packageForCategoryDto);
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
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
    public ResponseEntity<?> editPackage(int packageID,PackageDto packageDto){
        try{
            PackageEntity packageEntity = getPackageById(packageID);

            CountryEntity countryEntity = countryRepository.findByCountry(packageDto.getCountry())

                    .orElseThrow(()-> new EntityNotFoundException("Country is Not Found"));
            if(packageDto.getPackageName() != null){
                packageEntity.setPackageName(packageDto.getPackageName());
            }
            if (packageDto.getTitle() != null) {
                packageEntity.setTitle(packageDto.getTitle());
            }
            if (packageDto.getPackageImage() != null) {
                packageEntity.setPackageImage(packageDto.getPackageImage());
            }
            if(packageDto.getImageOne() != null){
                packageEntity.setImageOne(packageDto.getImageOne());
            }
            if(packageDto.getImageTwo() != null){
                packageEntity.setImageTwo(packageDto.getImageTwo());
            }
            if(packageDto.getImageThree() != null){
                packageEntity.setImageThree(packageDto.getImageThree());
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

    public ResponseEntity<?> deletePackage(int packageID) {
        try{
            packageRepository.deleteById(packageID);
            return new ResponseEntity<>("Package deleted successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteBenefit(int packageID, int benefitID) {
        try{
            PackageEntity packageEntity = getPackageById(packageID);
            Optional<BenefitEntity> benefitEntity = packageEntity.getBenefits().stream()
                    .filter(benefit-> benefit.getId() == benefitID).findFirst();
            if(benefitEntity.isPresent()){
                BenefitEntity benefit = benefitEntity.get();
                packageEntity.getBenefits().remove(benefit);
                benefitRepository.delete(benefit);
                return new ResponseEntity<>("benefit Deleted Successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Benefit is Not Found", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteRoadmap(int packageID,int roadmapID){
        try{
            PackageEntity packageEntity = getPackageById(packageID);
            Optional<RoadmapEntity> roadmapEntity = packageEntity.getRoadmaps().stream()
                    .filter(roadmap-> roadmap.getId() == roadmapID).findFirst();
            if(roadmapEntity.isPresent()){
                RoadmapEntity roadmap = roadmapEntity.get();
                packageEntity.getRoadmaps().remove(roadmap);
                roadmapRepository.delete(roadmap);
                return new ResponseEntity<>("Roadmap deleted Successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Roadmap is Not Found", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteTodos(int packageID,int todoID){
        try{
            PackageEntity packageEntity = getPackageById(packageID);
            Optional<TodoListEntity> todoListEntity = packageEntity.getTodos().stream()
                    .filter(todo-> todo.getId()==todoID).findFirst();
            if(todoListEntity.isPresent()){
                TodoListEntity todos = todoListEntity.get();
                packageEntity.getTodos().remove(todos);
                todoListRepository.delete(todos);
                return new ResponseEntity<>("Todos Deleted Successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Todos is Not Found", HttpStatus.NOT_FOUND);
            }
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


    private PackageEntity getPackageById(int packageID){
        return packageRepository.findById(packageID)
                .orElseThrow(() -> new EntityNotFoundException("Package Not Found"));
    }
}