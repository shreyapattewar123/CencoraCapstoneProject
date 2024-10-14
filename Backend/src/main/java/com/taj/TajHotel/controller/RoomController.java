package com.taj.TajHotel.controller;

import com.taj.TajHotel.dto.ApiResponseDTO;
import com.taj.TajHotel.service.BookingService;
import com.taj.TajHotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private BookingService bookingService;


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponseDTO> addNewRoom(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription
    ) {

        if (photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null || roomType.isBlank()) {
            ApiResponseDTO apiResponseDTO = new ApiResponseDTO();
            apiResponseDTO.setStatusCode(400);
            apiResponseDTO.setMessage("Please provide values for all fields(photo, roomType,roomPrice)");
            return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
        }
        ApiResponseDTO apiResponseDTO = roomService.addNewRoom(photo, roomType, roomPrice, roomDescription);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseDTO> getAllRooms() {
        ApiResponseDTO apiResponseDTO = roomService.getAllRooms();
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @GetMapping("/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<ApiResponseDTO> getRoomById(@PathVariable Long roomId) {
        ApiResponseDTO apiResponseDTO = roomService.getRoomById(roomId);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<ApiResponseDTO> getAvailableRooms() {
        ApiResponseDTO apiResponseDTO = roomService.getAllAvailableRooms();
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<ApiResponseDTO> getAvailableRoomsByDateAndType(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String roomType
    ) {
        if (checkInDate == null || roomType == null || roomType.isBlank() || checkOutDate == null) {
            ApiResponseDTO apiResponseDTO = new ApiResponseDTO();
            apiResponseDTO.setStatusCode(400);
            apiResponseDTO.setMessage("Please provide values for all fields(checkInDate, roomType,checkOutDate)");
            return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
        }
        ApiResponseDTO apiResponseDTO = roomService.getAvailableRoomsByDataAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponseDTO> updateRoom(@PathVariable Long roomId,
                                                     @RequestParam(value = "photo", required = false) MultipartFile photo,
                                                     @RequestParam(value = "roomType", required = false) String roomType,
                                                     @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
                                                     @RequestParam(value = "roomDescription", required = false) String roomDescription

    ) {
        ApiResponseDTO apiResponseDTO = roomService.updateRoom(roomId, roomDescription, roomType, roomPrice, photo);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponseDTO> deleteRoom(@PathVariable Long roomId) {
        ApiResponseDTO apiResponseDTO = roomService.deleteRoom(roomId);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);

    }


}
