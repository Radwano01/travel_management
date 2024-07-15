package com.hackathon.backend.dto.userDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EditUserDetailsDto {
    private String fullName;
    private String country;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String image;

    public EditUserDetailsDto(String fullName,
                              String country,
                              String phoneNumber,
                              String address,
                              LocalDate dateOfBirth,
                              String image) {
        this.fullName = fullName;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.image = image;
    }
}
