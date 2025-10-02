package com.CptFranck.BookingService.service;

import com.CptFranck.BookingService.dto.BookingRequest;
import com.CptFranck.BookingService.dto.BookingResponse;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    public BookingResponse createBooking(final BookingRequest bookingRequest) {
        return BookingResponse.builder().build();
    }
}
