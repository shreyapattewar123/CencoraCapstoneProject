package com.taj.TajHotel.controller;

import com.taj.TajHotel.dto.ApiResponseDTO;
import com.taj.TajHotel.entity.Booking;
import com.taj.TajHotel.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")

public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ApiResponseDTO> saveBookings(@PathVariable Long roomId,
                                                       @PathVariable Long userId,
                                                       @RequestBody Booking bookingRequest) {


        ApiResponseDTO apiResponseDTO =bookingService.saveBooking(roomId, userId, bookingRequest);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);

    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponseDTO> getAllBookings() {
        ApiResponseDTO apiResponseDTO = bookingService.getAllBookings();
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<ApiResponseDTO> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        ApiResponseDTO apiResponseDTO = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ApiResponseDTO> cancelBooking(@PathVariable Long bookingId) {
        ApiResponseDTO apiResponseDTO = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(apiResponseDTO.getStatusCode()).body(apiResponseDTO);
    }


}
