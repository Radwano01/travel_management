package com.hackathon.backend.Dto.HotelDto;

import com.hackathon.backend.Entities.CountryEntity;

public class HotelDto {

    private int id;
    private String hotelName;
    private String country;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
