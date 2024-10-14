package com.taj.TajHotel.controller;

import com.taj.TajHotel.dto.LoginRequestDTO;
import com.taj.TajHotel.dto.ApiResponseDTO;
import com.taj.TajHotel.entity.User;
import com.taj.TajHotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(@RequestBody User user) {
        ApiResponseDTO apiResponseDTO = userService.register(user);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        ApiResponseDTO apiResponseDTO = userService.login(loginRequestDTO);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }
}
