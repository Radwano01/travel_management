package com.hackathon.backend.Dto.PackageDto;

import com.hackathon.backend.Entities.CountryEntity;
import com.hackathon.backend.RelationShips.BenefitEntity;
import com.hackathon.backend.RelationShips.RoadmapEntity;
import com.hackathon.backend.RelationShips.TodoListEntity;

import java.util.List;

public class PackageDto {

    private int id;
    private String title;
    private String packageName;
    private String packageImage;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private CountryEntity country;
    private String description;
    private List<BenefitEntity> benefit;
    private List<TodoListEntity> todo;
    private List<RoadmapEntity> roadmap;
    private Integer price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BenefitEntity> getBenefit() {
        return benefit;
    }

    public void setBenefit(List<BenefitEntity> benefit) {
        this.benefit = benefit;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<TodoListEntity> getTodo() {
        return todo;
    }

    public void setTodo(List<TodoListEntity> todo) {
        this.todo = todo;
    }

    public List<RoadmapEntity> getRoadmap() {
        return roadmap;
    }

    public void setRoadmap(List<RoadmapEntity> roadmap) {
        this.roadmap = roadmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    public String getImageOne() {
        return imageOne;
    }

    public void setImageOne(String imageOne) {
        this.imageOne = imageOne;
    }

    public String getImageTwo() {
        return imageTwo;
    }

    public void setImageTwo(String imageTwo) {
        this.imageTwo = imageTwo;
    }

    public String getImageThree() {
        return imageThree;
    }

    public void setImageThree(String imageThree) {
        this.imageThree = imageThree;
    }
}
