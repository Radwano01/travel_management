package com.hackathon.backend.Dto.PlaneDto;

public class PlaneDto {

    private int id;

    private String planeName;
    private Integer sitsCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public Integer getSitsCount() {
        return sitsCount;
    }

    public void setSitsCount(Integer sitsCount) {
        this.sitsCount = sitsCount;
    }
}
