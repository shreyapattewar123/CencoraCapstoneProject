package com.taj.TajHotel.service;

import com.taj.TajHotel.dto.ApiResponseDTO;
import com.taj.TajHotel.entity.Booking;

public interface BookingService {

    ApiResponseDTO saveBooking(Long roomId, Long userId, Booking bookingRequest);

    ApiResponseDTO findBookingByConfirmationCode(String confirmationCode);

    ApiResponseDTO getAllBookings();

    ApiResponseDTO cancelBooking(Long bookingId);

}