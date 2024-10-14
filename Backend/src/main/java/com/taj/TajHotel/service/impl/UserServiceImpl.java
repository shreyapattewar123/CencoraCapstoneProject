
// new code after adding the exception handler
package com.taj.TajHotel.service.impl;

import com.taj.TajHotel.dto.ApiResponseDTO;
import com.taj.TajHotel.dto.LoginRequestDTO;
import com.taj.TajHotel.dto.UserDTO;
import com.taj.TajHotel.entity.User;
import com.taj.TajHotel.repo.UserRepository;
import com.taj.TajHotel.service.UserService;
import com.taj.TajHotel.utils.AppUtils;
import com.taj.TajHotel.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ApiResponseDTO register(User user) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(user.getEmail() + " Already Exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        UserDTO userDTO = AppUtils.mapUserEntityToUserDTO(savedUser);
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setUser(userDTO);

        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO login(LoginRequestDTO loginRequestDTO) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new NoSuchElementException("User Not Found"));

        String token = jwtUtils.generateToken(user);
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setToken(token);
        apiResponseDTO.setRole(user.getRole());
        apiResponseDTO.setExpirationTime("7 Days");
        apiResponseDTO.setMessage("successful");

        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO getAllUsers() {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = AppUtils.mapUserListEntityToUserListDTO(userList);
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setMessage("successful");
        apiResponseDTO.setUserList(userDTOList);

        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO getUserBookingHistory(String userId) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new NoSuchElementException("User Not Found"));
        UserDTO userDTO = AppUtils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setMessage("successful");
        apiResponseDTO.setUser(userDTO);

        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO deleteUser(String userId) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new NoSuchElementException("User Not Found"));
        userRepository.deleteById(Long.valueOf(userId));
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setMessage("successful");

        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO getUserById(String userId) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new NoSuchElementException("User Not Found"));
        UserDTO userDTO = AppUtils.mapUserEntityToUserDTO(user);
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setMessage("successful");
        apiResponseDTO.setUser(userDTO);

        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO getMyInfo(String email) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User Not Found"));
        UserDTO userDTO = AppUtils.mapUserEntityToUserDTO(user);
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setMessage("successful");
        apiResponseDTO.setUser(userDTO);

        return apiResponseDTO;
    }
}