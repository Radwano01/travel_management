package com.hackathon.backend.Entities;


import com.hackathon.backend.RelationShips.BenefitEntity;
import com.hackathon.backend.RelationShips.RoadmapEntity;
import com.hackathon.backend.RelationShips.TodoListEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "package")
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String packageName;

    @ManyToOne
    @JoinColumn(name = "country")
    private CountryEntity country;
    private String description;
    private Integer price;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "package_benefit",
            joinColumns = @JoinColumn(name = "package_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "benefit_id", referencedColumnName = "id"))
    private List<BenefitEntity> benefits = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "package_roadmap", joinColumns = @JoinColumn(name = "package_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roadmap_id", referencedColumnName = "id"))
    private List<RoadmapEntity> roadmaps = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "package_todos", joinColumns = @JoinColumn(name = "package_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "todos_id", referencedColumnName = "id"))
    private List<TodoListEntity> todos = new ArrayList<>();



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


    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


    public List<BenefitEntity> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<BenefitEntity> benefits) {
        this.benefits = benefits;
    }

    public List<RoadmapEntity> getRoadmaps() {
        return roadmaps;
    }

    public void setRoadmaps(List<RoadmapEntity> roadmaps) {
        this.roadmaps = roadmaps;
    }

    public List<TodoListEntity> getTodos() {
        return todos;
    }

    public void setTodos(List<TodoListEntity> todos) {
        this.todos = todos;
    }
}
