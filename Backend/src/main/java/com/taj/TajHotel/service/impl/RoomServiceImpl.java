
////this is the new code after the exception handler...
//
//

package com.taj.TajHotel.service.impl;

import com.taj.TajHotel.dto.ApiResponseDTO;
import com.taj.TajHotel.dto.RoomDTO;
import com.taj.TajHotel.entity.Room;
import com.taj.TajHotel.repo.BookingRepository;
import com.taj.TajHotel.repo.RoomRepository;
import com.taj.TajHotel.service.RoomService;
import com.taj.TajHotel.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public ApiResponseDTO addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        try {
            // Convert the MultipartFile to byte array
            byte[] imageBytes = photo.getBytes();

            // Create a new Room entity
            Room room = new Room();
            room.setRoomPhoto(imageBytes);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);

            // Save the Room entity to the database
            Room savedRoom = roomRepository.save(room);

            // Map the saved Room entity to RoomDTO
            RoomDTO roomDTO = AppUtils.mapRoomEntityToRoomDTO(savedRoom);

            // Set the response details
            apiResponseDTO.setStatusCode(200);
            apiResponseDTO.setMessage("successful");
            apiResponseDTO.setRoom(roomDTO);

        } catch (IOException e) {
            apiResponseDTO.setStatusCode(500);
            apiResponseDTO.setMessage("Error processing room photo: " + e.getMessage());
        } catch (Exception e) {
            apiResponseDTO.setStatusCode(500);
            apiResponseDTO.setMessage("Error saving a room: " + e.getMessage());
        }
        return apiResponseDTO;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public ApiResponseDTO getAllRooms() {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = AppUtils.mapRoomListEntityToRoomListDTO(roomList);
            apiResponseDTO.setStatusCode(200);
            apiResponseDTO.setMessage("successful");
            apiResponseDTO.setRoomList(roomDTOList);

        } catch (Exception e) {
            apiResponseDTO.setStatusCode(500);
            apiResponseDTO.setMessage("Error retrieving rooms: " + e.getMessage());
        }
        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO deleteRoom(Long roomId) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        try {
            roomRepository.findById(roomId).orElseThrow(() -> new NoSuchElementException("Room Not Found"));
            roomRepository.deleteById(roomId);
            apiResponseDTO.setStatusCode(200);
            apiResponseDTO.setMessage("successful");

        } catch (NoSuchElementException e) {
            apiResponseDTO.setStatusCode(404);
            apiResponseDTO.setMessage(e.getMessage());
        } catch (Exception e) {
            apiResponseDTO.setStatusCode(500);
            apiResponseDTO.setMessage("Error deleting room: " + e.getMessage());
        }
        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        try {
            byte[] imageBytes = null;
            if (photo != null && !photo.isEmpty()) {
                imageBytes = photo.getBytes();
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new NoSuchElementException("Room Not Found"));
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            if (imageBytes != null) room.setRoomPhoto(imageBytes);

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = AppUtils.mapRoomEntityToRoomDTO(updatedRoom);

            apiResponseDTO.setStatusCode(200);
            apiResponseDTO.setMessage("successful");
            apiResponseDTO.setRoom(roomDTO);

        } catch (NoSuchElementException e) {
            apiResponseDTO.setStatusCode(404);
            apiResponseDTO.setMessage(e.getMessage());
        } catch (Exception e) {
            apiResponseDTO.setStatusCode(500);
            apiResponseDTO.setMessage("Error updating room: " + e.getMessage());
        }
        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO getRoomById(Long roomId) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new NoSuchElementException("Room Not Found"));
            RoomDTO roomDTO = AppUtils.mapRoomEntityToRoomDTOPlusBookings(room);
            apiResponseDTO.setStatusCode(200);
            apiResponseDTO.setMessage("successful");
            apiResponseDTO.setRoom(roomDTO);

        } catch (NoSuchElementException e) {
            apiResponseDTO.setStatusCode(404);
            apiResponseDTO.setMessage(e.getMessage());
        } catch (Exception e) {
            apiResponseDTO.setStatusCode(500);
            apiResponseDTO.setMessage("Error retrieving room: " + e.getMessage());
        }
        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = AppUtils.mapRoomListEntityToRoomListDTO(availableRooms);
            apiResponseDTO.setStatusCode(200);
            apiResponseDTO.setMessage("successful");
            apiResponseDTO.setRoomList(roomDTOList);

        } catch (Exception e) {
            apiResponseDTO.setStatusCode(500);
            apiResponseDTO.setMessage("Error retrieving available rooms: " + e.getMessage());
        }
        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO getAllAvailableRooms() {
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = AppUtils.mapRoomListEntityToRoomListDTO(roomList);
            apiResponseDTO.setStatusCode(200);
            apiResponseDTO.setMessage("successful");
            apiResponseDTO.setRoomList(roomDTOList);

        } catch (Exception e) {
            apiResponseDTO.setStatusCode(500);
            apiResponseDTO.setMessage("Error retrieving available rooms: " + e.getMessage());
        }
        return apiResponseDTO;
    }
}