package com.taj.TajHotel.service;

import com.taj.TajHotel.dto.ApiResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    ApiResponseDTO addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);

    List<String> getAllRoomTypes();

    ApiResponseDTO getAllRooms();

    ApiResponseDTO deleteRoom(Long roomId);

    ApiResponseDTO updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo);

    ApiResponseDTO getRoomById(Long roomId);

    ApiResponseDTO getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    ApiResponseDTO getAllAvailableRooms();
}
