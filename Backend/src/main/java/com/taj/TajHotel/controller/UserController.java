package com.taj.TajHotel.controller;

import com.taj.TajHotel.dto.ApiResponseDTO;
import com.taj.TajHotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponseDTO> getAllUsers() {
        ApiResponseDTO apiResponseDTO = userService.getAllUsers();
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<ApiResponseDTO> getUserById(@PathVariable("userId") String userId) {
        ApiResponseDTO apiResponseDTO = userService.getUserById(userId);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponseDTO> deleteUSer(@PathVariable("userId") String userId) {
        ApiResponseDTO apiResponseDTO = userService.deleteUser(userId);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @GetMapping("/get-logged-in-profile-info")
    public ResponseEntity<ApiResponseDTO> getLoggedInUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ApiResponseDTO apiResponseDTO = userService.getMyInfo(email);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @GetMapping("/get-user-bookings/{userId}")
    public ResponseEntity<ApiResponseDTO> getUserBookingHistory(@PathVariable("userId") String userId) {
        ApiResponseDTO apiResponseDTO = userService.getUserBookingHistory(userId);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }


}