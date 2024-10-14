
//new code after adding exceptionhandler for  bookingserviceImpl ...

package com.taj.TajHotel.service.impl;

import com.taj.TajHotel.dto.BookingDTO;
import com.taj.TajHotel.dto.ApiResponseDTO;
import com.taj.TajHotel.entity.Booking;
import com.taj.TajHotel.entity.Room;
import com.taj.TajHotel.entity.User;
import com.taj.TajHotel.repo.BookingRepository;
import com.taj.TajHotel.repo.RoomRepository;
import com.taj.TajHotel.repo.UserRepository;
import com.taj.TajHotel.service.BookingService;
import com.taj.TajHotel.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ApiResponseDTO saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new IllegalArgumentException("Check in date must come after check out date");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("Room Not Found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User Not Found"));

        List<Booking> existingBookings = room.getBookings();
        if (!roomIsAvailable(bookingRequest, existingBookings)) {
            throw new IllegalArgumentException("Room not Available for selected date range");
        }

        bookingRequest.setRoom(room);
        bookingRequest.setUser(user);
        String bookingConfirmationCode = AppUtils.generateRandomConfirmationCode(10);
        bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
        bookingRepository.save(bookingRequest);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setMessage("successful");
        apiResponseDTO.setBookingConfirmationCode(bookingConfirmationCode);
        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO findBookingByConfirmationCode(String confirmationCode) {
        Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new NoSuchElementException("Booking Not Found"));

        BookingDTO bookingDTO = AppUtils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setMessage("successful");
        apiResponseDTO.setBooking(bookingDTO);
        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO getAllBookings() {
        List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<BookingDTO> bookingDTOList = AppUtils.mapBookingListEntityToBookingListDTO(bookingList);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setMessage("successful");
        apiResponseDTO.setBookingList(bookingDTOList);
        return apiResponseDTO;
    }

    @Override
    public ApiResponseDTO cancelBooking(Long bookingId) {
        bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking Does Not Exist"));

        bookingRepository.deleteById(bookingId);

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();
        apiResponseDTO.setStatusCode(200);
        apiResponseDTO.setMessage("successful");
        return apiResponseDTO;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}