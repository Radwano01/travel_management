package com.hackathon.backend.dto.userTodosDto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTodosDto {

    private Long id;
    private String todos;
    private boolean status;
}
