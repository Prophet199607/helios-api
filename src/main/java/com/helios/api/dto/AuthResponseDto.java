package com.helios.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthResponseDto {
    public Long loggedUserId;
    public String token;
    public UserResponseDto user;
    List<String> roles;
}
