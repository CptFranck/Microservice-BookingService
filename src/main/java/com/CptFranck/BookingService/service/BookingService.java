package com.CptFranck.BookingService.service;

import com.CptFranck.BookingService.dto.BookingRequest;
import com.CptFranck.BookingService.dto.BookingResponse;
import com.CptFranck.BookingService.entity.CustomerEntity;
import com.CptFranck.BookingService.repository.CustomerRepository;
import com.CptFranck.dto.BookingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingService {

    private final CustomerRepository customerRepository;

    private final KafkaTemplate<String, BookingEvent> bookingEventKafkaTemplate;

    public BookingService(CustomerRepository customerRepository, KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.bookingEventKafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest request) {
        final CustomerEntity customer = customerRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not Found !"));

        final BookingEvent bookingEvent = createBookingEvent(request, customer);
        log.info("Emit booking event : {}", bookingEvent);

        bookingEventKafkaTemplate.send("booking-event", bookingEvent);

        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .build();
    }

    private BookingEvent createBookingEvent(final BookingRequest request, final CustomerEntity customer) {
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .build();
    }
}
