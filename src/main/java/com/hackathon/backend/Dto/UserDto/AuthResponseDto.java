package com.hackathon.backend.Dto.UserDto;

public class AuthResponseDto {

    private final String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponseDto(String accessToken){
        this.accessToken = accessToken;
    }
    public String getTokenType() {
        return tokenType;
    }
    public String getAccessToken(){
        return accessToken;
    }
}
