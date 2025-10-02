package com.CptFranck.BookingService.controller;

import com.CptFranck.BookingService.dto.BookingRequest;
import com.CptFranck.BookingService.dto.BookingResponse;
import com.CptFranck.BookingService.service.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/booking")
    public BookingResponse createBooking(@RequestBody BookingRequest request){
        return bookingService.createBooking(request);
    }

    @GetMapping("/inventory/venue/{venueId}")
    public @ResponseBody VenueInventoryResponse inventoryGetAllResponses(@PathVariable("venueId") Long venueId){
        return inventoryService.getVenueInformation(venueId);
    }
}
