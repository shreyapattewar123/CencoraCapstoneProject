package com.taj.TajHotel.service;

import com.taj.TajHotel.dto.LoginRequestDTO;
import com.taj.TajHotel.dto.ApiResponseDTO;
import com.taj.TajHotel.entity.User;

public interface UserService {
    ApiResponseDTO register(User user);

    ApiResponseDTO login(LoginRequestDTO loginRequestDTO);

    ApiResponseDTO getAllUsers();

    ApiResponseDTO getUserBookingHistory(String userId);

    ApiResponseDTO deleteUser(String userId);

    ApiResponseDTO getUserById(String userId);

    ApiResponseDTO getMyInfo(String email);

}
